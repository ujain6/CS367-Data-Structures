import java.lang.Comparable;
import java.util.Random;

/**
 * The Packet structure which includes a sequence number, a checksum
 * (true/false), and the delivered data.
 *
 */
public class SimplePacket implements Comparable<SimplePacket> {
	private int seq;
	private boolean checksum;
	private byte[] data;
	private int pred;

	/**
	 * Constructs a SimplePacket with sequence number, checksum validation, and
	 * the data.
	 * 
	 * @param seq
	 *            the sequence number starting from 1
	 * @param checksum
	 *            true or false to indicate the validation of its checksum
	 * @param buf
	 *            the delivered data in the packet
	 */
	public SimplePacket(int seq, boolean checksum, byte[] buf) {
		this.seq = seq;
		this.checksum = checksum;
		data = buf;
		Random rand = new Random();
		pred = seq + rand.nextInt(5);
	}

	public int getSeq() {
		return seq;
	}

	public boolean isValidCheckSum() {
		return checksum;
	}

	public byte[] getData() {
		return data;
	}

	public int getPred() {
		return pred;
	}

	@Override
	public int compareTo(SimplePacket pkt) {
		if (this.seq == pkt.getSeq()) {
			if (this.checksum == pkt.isValidCheckSum()) {
				return 0;
			} else if (this.checksum) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return pkt.getPred() - this.pred;
		}
	}

}
