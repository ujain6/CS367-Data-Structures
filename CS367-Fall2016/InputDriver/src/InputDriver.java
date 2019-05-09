import static javax.crypto.Cipher.getInstance;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.util.LinkedList;

/**
 * The driver class for generating and simulating the input queue.
 * THIS CLASS REQUIRES NETWORK CONNECTION TO SIMULATE THE TRANSMISSION OF IMAGES.
 */
public class InputDriver {
    private final static int PACKET_SIZE = 65536;
    private final static int MAX_NUM_PACKETS = 10000;
    private final static String KEY = "SecretP2SecretP2"; // 128 bit key
    private final static String IV = "RandomInitVector"; // 16 bytes IV
    private final static String SECRET = 
        "9EMDN/lCpcgr0X0RMQf5HHnhdz0aS51sZbsB4sFPlD/UEal763BHDIoGgX1xNSOF6nVRdPPLs0dR16x5Fmy4lw==";
    private SimplePacket[] pkts;
    private LinkedList<SimplePacket> queue_to_receive;
    private HashMap<Integer, SimplePacket> map;
    private HashSet<Integer> lost_set;
    private Random rand;
    private boolean header;
    private boolean terminator;
    private boolean lost_packet;
    private boolean corrupt;

    private InputDriver(String path) throws IOException {
        map = new HashMap<Integer, SimplePacket>();
        queue_to_receive = new LinkedList<SimplePacket>();
        lost_set = new HashSet<Integer>();
        rand = new Random();
        terminator = true;
        lost_packet = true;
        corrupt = false;
        generatePackets(path);
    }

    /**
     * Constructs a InputDriver.
     * 
     * @param path
     *            the file path for generating the input queue
     * @param header
     * @throws IOException
     */
    public InputDriver(String path, boolean header) throws IOException {
        this(path);
        this.header = header;
        if (!header) {
            this.corruptHeader();
        }
        blendBadPackets();
    }

    private static byte[] decrypt() {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(DatatypeConverter.parseBase64Binary(SECRET));
            return original;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private void generatePackets(String path) throws IOException {
    	if (path.equals("secret0.jpg")) {
    		lost_packet = false;
    	} else if (path.equals("secret5.jpg")) {
    		lost_packet = false;
    		corrupt = true;
    	}
        pkts = splitFile(path);
    }

    private void blendBadPackets() {
        PriorityQueue<SimplePacket> prio_queue = new PriorityQueue<SimplePacket>();
        for (int i = pkts.length - 1; i >= 0; i--) {
            SimplePacket cur = pkts[i];
            int dup_num = 0;
            // generate duplicate packets
            while (random(3)) {
                byte[] buf = new byte[cur.getData().length];
                System.arraycopy(cur.getData(), 0, buf, 0, buf.length);
                SimplePacket dup = new SimplePacket(cur.getSeq(), true, buf);
                prio_queue.add(dup);
                ++dup_num;
            }
            
            // generate missing packets
            if (random(4) && dup_num == 0 && lost_packet) {
            	lost_set.add(cur.getSeq());
            } else {
            	// the last arrived pkt with some probability to be bad checksum
                byte[] buf = new byte[cur.getData().length];
                System.arraycopy(cur.getData(), 0, buf, 0, buf.length);
                SimplePacket cppkt = new SimplePacket(cur.getSeq(), random(4) ? false : true, buf);
                prio_queue.add(cppkt);
            }
        }

        // insert all the rest packets in the dup stack
        while (!prio_queue.isEmpty()) {
            queue_to_receive.push(prio_queue.poll());
        }

        // corrupt bad packets or dup packets
        Iterator<SimplePacket> iter = queue_to_receive.iterator();
        while (iter.hasNext()) {
            SimplePacket p = iter.next();
            Integer seq = p.getSeq();
            if (!p.isValidCheckSum()) {
                corruptSinglePacket(p);
            } else if (p.getSeq() != 1 && corrupt) {
            	corruptSinglePacket(p);
            }
            if (map.containsKey(seq)) {
                corruptSinglePacket(map.get(seq));
            }
            map.put(seq, p);
        }
    }

    private SimplePacket[] splitFile(String filename) throws IOException {
        URL url = new URL(new String(decrypt()) + filename);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        byte[][] buf_chunks = new byte[MAX_NUM_PACKETS][];
        int packetCnt = 0;
        SimplePacket[] queue;
        InputStream in = null;
        BufferedInputStream buf_in = null;
        try {
            in = url.openStream();
            buf_in = new BufferedInputStream(in);
            buf_chunks[0] = new byte[PACKET_SIZE];
            int b = 0;
            int offset = 0;
            int len = PACKET_SIZE;
            while ((b = buf_in.read(buf_chunks[packetCnt], offset, len)) != -1) {
                if (b < len) {
                    offset += b;
                    len -= b;
                } else if (b == len) {
                    offset = 0;
                    len = PACKET_SIZE;
                    buf_chunks[++packetCnt] = new byte[PACKET_SIZE];
                }
            }
            if (offset != 0) {
                ++packetCnt;
            }
            queue = new SimplePacket[packetCnt];
            for (int i = 0; i < packetCnt; i++) {
                queue[i] = new SimplePacket(i + 1, true, buf_chunks[i]);
            }
            in.close();
            buf_in.close();
        } finally {
            urlConnection.disconnect();
        }

        return queue;
    }

    /**
     * Do not use this method. Use {@link Receiver#askForNextPacket()} instead.
     * @return next packet to pass into receiving queue
     */
    protected SimplePacket getNextPacket() {
        try {
            return queue_to_receive.pop();
        } catch (NoSuchElementException e) {
        	if (!lost_packet && terminator) {
        		terminator = false;
            	return new SimplePacket(-pkts.length, true, null);
        	}
            // decide to lose the terminator or not
            if (random(6) && !lost_set.contains(0) && terminator) {
            	terminator = false;
            	lost_set.add(0);
            } else if (!lost_set.contains(0) && terminator){
            	terminator = false;
            	return new SimplePacket(-pkts.length, true, null);
            } else if (lost_set.contains(0) && terminator) {
            	terminator = false;
            	lost_set.remove(0);
            	return new SimplePacket(-pkts.length, true, null);
            }
            return null;
        }
    }

    /**
     * Do not use this method. Use
     * {@link Receiver#askForRetransmit(SimplePacket)} instead.
     * 
     * @return true if the packet is invalid checksum that must be resent;
     *         otherwise, false
     * @throws IllegalArgumentException
     *             when the requested packet is null or it has invalid sequence
     *             number
     */
    protected boolean resendPacket(SimplePacket pkt) {
    	if (pkt == null) {
    		throw new IllegalArgumentException("The requested packet to resend cannot be null.");
    	}
        int seq = pkt.getSeq();
        if (seq <= 0) {
    		throw new IllegalArgumentException("The requested sequence number cannot be less than 1.");
    	}
        int frame = 5;
        if (!pkt.isValidCheckSum()) {
            SimplePacket correctPkt = pkts[seq - 1];
            int remaining = queue_to_receive.size();
            int interval = remaining < frame ? rand.nextInt(remaining + 1) : rand.nextInt(frame + 1);
            
            // corrupt the packet with small probability
            byte[] buf = new byte[correctPkt.getData().length];
            System.arraycopy(correctPkt.getData(), 0, buf, 0, buf.length);
            if (random(5)) {    	
                 SimplePacket cppkt = new SimplePacket(correctPkt.getSeq(), false, buf);
                 corruptSinglePacket(cppkt);
                 queue_to_receive.add(interval, cppkt);
            } else {
            	SimplePacket cppkt = new SimplePacket(correctPkt.getSeq(), true, buf);
                queue_to_receive.add(interval, cppkt);
            }
            return true;
        }
        return false;
    }
    
	/**
	 * Do not use this method. Use {@link Receiver#askForMissingPacket(int)}
	 * instead.
	 * 
	 * @return true if the packet is truly missing in the receiving queue;
	 *         otherwise, false.
	 * @throws IllegalArgumentException
	 *             when seq is negative
	 * @throws RuntimeException
	 *             when requesting End of Streaming Notification Packet while
	 *             receiving queue is not empty
	 */
    protected boolean resendMissingPacket(int seq) {
    	if (seq < 0) {
    		throw new IllegalArgumentException("The requested sequence number cannot be negative.");
    	}
    	int frame = 3;
    	if (lost_set.contains(seq)) {
			if (seq == 0) {
				if (queue_to_receive.size() != 0) {
					throw new RuntimeException(
							"The End of Stream Notification cannot be accessed when queue is not empty.");
				}
				terminator = true;	
			} else {
				SimplePacket correctPkt = pkts[seq - 1];
				int remaining = queue_to_receive.size();
				int interval = remaining < frame ? rand.nextInt(remaining + 1) : rand.nextInt(frame + 1);
				// corrupt the packet with small probability
				byte[] buf = new byte[correctPkt.getData().length];
				System.arraycopy(correctPkt.getData(), 0, buf, 0, buf.length);
				if (random(5)) {
					SimplePacket cppkt = new SimplePacket(correctPkt.getSeq(), false, buf);
					corruptSinglePacket(cppkt);
					queue_to_receive.add(interval, cppkt);
				} else {
					SimplePacket cppkt = new SimplePacket(correctPkt.getSeq(), true, buf);
					queue_to_receive.add(interval, cppkt);
				}
				lost_set.remove(seq);
			}
            return true;
    	}
    	return false;
    }

	/**
	 * Do not use this method. Use {@link Receiver#validImageContent()} instead.
	 * 
	 * @param list the list buffer
	 * @return true if the list buffer has a valid image content;
	 *         otherwise, false
	 */
    protected boolean validFile(PacketLinkedList<SimplePacket> list) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PacketLinkedListIterator<SimplePacket> iter = list.iterator();
        while (iter.hasNext()) {
            SimplePacket pkt = iter.next();
            try {
                outStream.write(pkt.getData());
            } catch (IOException e) {
                System.out.println("Unable to open the image for unknown reason.");
            }
        }
        byte[] dest = outStream.toByteArray();

        outStream = new ByteArrayOutputStream();
        for (SimplePacket p : pkts) {
            try {
                outStream.write(p.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] src = outStream.toByteArray();
        if (src.length != dest.length)
            return false;
        for (int i = 0; i < src.length; i++) {
            if (src[i] != dest[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Do not use this method. Use {@link Receiver#validImageHeader()} instead.
     * 
     * @param p the header packet
     * @return true if this header packet is valid; otherwise, false
	 */
    protected boolean validHeader(SimplePacket p) {
        if (!header) {
            return false;
        }
        byte[] dest = p.getData();

        byte[] src = pkts[0].getData();
        if (dest.length < 10 || src.length < 10) {
            return false;
        }
        if (src.length != dest.length) {
            return false;
        }
        for (int i = 0; i < src.length; i++) {
            if (src[i] != dest[i]) {
                return false;
            }
        }
        return true;
    } 

    private boolean random(int base) {
        return rand.nextInt(base) == 0;
    }

    private void corruptHeader() {
        byte[] buf = pkts[0].getData();
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) 0xff;
        }

    }

    private void corruptSinglePacket(SimplePacket p) {
        byte[] buf = p.getData();
        for (int i = 0; i < buf.length; i++) {
            buf[i] &= 0x34;
        }
    }

}

