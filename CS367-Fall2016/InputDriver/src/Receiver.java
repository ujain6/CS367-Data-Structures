///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            InputDriver
// Files:            Receiver.java, BadImageContentException.java, ImageDriver.
//					 java, BadImageHeaderException.java, InputDriver.java, 
//					 ListADT.java, Listnode.java, PacketLinkedList.java, 
//				     PacketLinkedListIterator.java, SimplePacket.java
//
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain
// Email:            ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  Jim Skrentny
//
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main class. It simulates a application (image viewer) receiver by
 * maintaining a list buffer. It collects packets from the queue of InputDriver
 * and arrange them properly, and then reconstructs the image file from its list
 * buffer.
 */
public class Receiver {
	private InputDriver input;
	private ImageDriver img;
	private PacketLinkedList<SimplePacket> list;

	/**
	 * Constructs a Receiver to obtain the image file transmitted.
	 * 
	 * @param file
	 *            the filename you want to receive
	 */
	public Receiver(String file) {
		try {
			input = new InputDriver(file, true);
		} catch (IOException e) {
			System.out.println("The file, " + file + ", isn't existed on the server.");
			System.exit(0);
		}
		img = new ImageDriver(input);
		//Initialize the list
		list = new PacketLinkedList<SimplePacket>();
	}

	/**
	 * Returns the PacketLinkedList buffer in the receiver
	 * 
	 * @return the PacketLinkedList object
	 */
	public PacketLinkedList<SimplePacket> getListBuffer() {
		return list;
	}

	/**
	 * Asks for retransmitting the packet. The new packet with the sequence
	 * number will arrive later by using {@link #askForNextPacket()}. Notice
	 * that ONLY packet with invalid checksum will be retransmitted.
	 * 
	 * @param pkt
	 *            the packet with bad checksum
	 * @return true if the requested packet is added in the receiving queue;
	 *         otherwise, false
	 */
	public boolean askForRetransmit(SimplePacket pkt) {
		return input.resendPacket(pkt);
	}

	/**
	 * Asks for retransmitting the packet with a sequence number. The requested
	 * packet will arrive later by using {@link #askForNextPacket()}. Notice
	 * that ONLY missing packet will be retransmitted. Pass seq=0 if the missing
	 * packet is the "End of Streaming Notification" packet.
	 * 
	 * @param seq
	 *            the sequence number of the requested missing packet
	 * @return true if the requested packet is added in the receiving queue;
	 *         otherwise, false
	 */
	public boolean askForMissingPacket(int seq) {
		return input.resendMissingPacket(seq);
	}

	/**
	 * Returns the next packet.
	 * 
	 * @return the next SimplePacket object; returns null if no more packet to
	 *         receive
	 */
	public SimplePacket askForNextPacket() {
		return input.getNextPacket();
	}

	/**
	 * Returns true if the maintained list buffer has a valid image content.
	 * Notice that when it returns false, the image buffer could either has a
	 * bad header, or just bad body, or both.
	 * 
	 * @return true if the maintained list buffer has a valid image content;
	 *         otherwise, false
	 */
	public boolean validImageContent() {
		return input.validFile(list);
	}

	/**
	 * Returns if the maintained list buffer has a valid image header
	 * 
	 * @return true if the maintained list buffer has a valid image header;
	 *         otherwise, false
	 */
	public boolean validImageHeader() {
		return input.validHeader(list.get(0));
	}

	/**
	 * Outputs the formatted content in the PacketLinkedList buffer. This is
	 * used for debugging the code.
	 */
	public void displayList() {

		PacketLinkedListIterator<SimplePacket> itr = list.iterator();

		while (itr.hasNext()) {
			SimplePacket packet = itr.next();


			if (packet.isValidCheckSum())
				System.out.print(packet.getSeq()  + ", ");
			else
				//square brackets to denote corrupted packets
				System.out.print("[" + packet.getSeq() + "]" + ", ");



		}
	}

	/**
	 * Reconstructs the file by arranging the {@link PacketLinkedList} in
	 * correct order. It uses {@link #askForNextPacket()} to get packets until
	 * no more packet to receive. It eliminates the duplicate packets and asks
	 * for retransmitting when getting a packet with invalid checksum.
	 */

	public void reconstructFile() {

		int totalPackets = 0;

		// A flag that turns true when the buffer receives the End-Of-Stream
		// packet.
		boolean flag = false;

		/*
		 *
		 *  Adds the packets while also removing any duplicates that might be
		 *  already present in the buffer
		 * 
		 */

		while (!flag) {

			SimplePacket packet = askForNextPacket();

			//If the packet is null,that means the packet received was EOS
			if (packet == null)
				flag = true;

			//otherwise...
			if (!flag) {

				int pos = 0;
				PacketLinkedListIterator<SimplePacket> itr = list.iterator();

				//Removes duplicate packets
				while (itr.hasNext()) {

					SimplePacket newPacket = itr.next();
					if (newPacket.getSeq() == packet.getSeq()) {
						list.remove(pos);
					}
					pos++;
				}

				//If the sequence is negative, that means we have recieved the
				//EOS packet, we stop adding packets and calculate the total
				//number of packets we were supposed to recieve
				if (packet.getSeq() < 0) {

					// The sequence number in EOS packet is negative, therefore
					// absolute value to get the number of packets
					totalPackets = Math.abs(packet.getSeq());

					//Flag turned true to signal the recieve to stop receiving
					//the packets
					flag = true;
				} 
				else
					//Finally adds the packet at the end of the list
					list.add(packet);
			}
		}


		/*
		 * 
		 * It is possible that the InputDriver never sends the End-Of-Stream 
		 * packet, therefore we ask for the missing EOS in case it happens
		 * 
		 */
		boolean flag2 = false;

		if (totalPackets == 0) {

			SimplePacket packet;

			//The sequence number for the EOS packet when we call the 
			//askForMissingPacket(int seq) is 0
			while (!flag2 && askForMissingPacket(0)) {

				packet = askForNextPacket();
				if (packet.getSeq() < 0) {

					totalPackets = Math.abs(packet.getSeq());
					flag2 = true;
				}
			}
		}



		/*
		 * 
		 * If we receive less packets than we were supposed to recieve for a 
		 * particular file, this means there are missing packets in the buffer.
		 * 
		 * The code below handles the missing packets and makes sure we get all
		 * the packets.
		 * 
		 */
		if (list.size() < totalPackets) {

			PacketLinkedListIterator<SimplePacket> itr2 = list.iterator();

			ArrayList<Integer> missingPackets = new ArrayList<Integer>();
			ArrayList<Integer> packetsInList = new ArrayList<Integer>();


			//Populates the list with the sequence numbers of the list
			while (itr2.hasNext()) {
				packetsInList.add(itr2.next().getSeq());
			}
			//Sorts it in ascending order
			packetsInList.sort(null);

			//Creates a list having integers from 1 to total number of packets
			for (int i = 1; i <= totalPackets; i++)
				missingPackets.add(i);

			//Removes the sequence numbers that are present in the list 
			//already.
			for (int i = 0; i < packetsInList.size(); i++)
				missingPackets.remove(packetsInList.get(i));

			//Asks for missing packets by calling askForMissingPacket()
			for (int i = 0; i < missingPackets.size(); i++) {

				SimplePacket packet;
				
				//Call the function
				if (askForMissingPacket(missingPackets.get(i))) {
					packet = askForNextPacket();
					
					
					if (packet.getSeq() == missingPackets.get(i)) {
						list.add(packet);
					} 

					else {
						
						PacketLinkedListIterator<SimplePacket> itr1 = 
																list.iterator();
						int pos = 0;
						
						
						while (itr1.hasNext()) {
							SimplePacket packet2 = itr1.next();
							
							if (packet2.getSeq() == packet.getSeq()) {
								
								list.remove(pos);
								
								list.add(packet);
							}
							pos++;
						}
					}
				}
			}
		}


		
		/*
		 * 
		 * This code removes packets with invalid checksums and replace them 
		 * with the latest packets having a valid checksum
		 * 
		 */
		PacketLinkedListIterator<SimplePacket> itr = list.iterator();

		int pos = 0;
		while (itr.hasNext()) {
			
			SimplePacket incomingPacket;
			SimplePacket packet = itr.next();

			//Runs as long as we don't get a valid copy of the packet
			while (!packet.isValidCheckSum()) {
				if (packet.getSeq() >= 0 && askForRetransmit(packet)) {
					incomingPacket = askForNextPacket();

					//If there is a duplicate packet already in the buffer, it
					//replaces it with the new packet having valid checksum
					if (incomingPacket.getSeq() == packet.getSeq() && 
							incomingPacket.isValidCheckSum()) {

						packet = incomingPacket;

						list.add(pos, packet);

						list.remove(pos + 1);
					}
				}
			}
			pos++;
		}


		/*
		 * 
		 * The code sorts the packets in the buffer in ascending order of their
		 * respective sequence numbers.
		 * 
		 */
		
		//A new list that has the sorted numbers.
		PacketLinkedList<SimplePacket> sortedList = 
				                          new PacketLinkedList<SimplePacket>();
		
		PacketLinkedListIterator<SimplePacket> itr2 = list.iterator();
		
		for (int i = 1; i <= list.size(); i++) {
			itr2 = list.iterator();

			while (itr2.hasNext()) {
				SimplePacket packet = itr2.next();
				
				
				if (packet.getSeq() == i) {
					sortedList.add(packet);
					//Breaks out of the while loop to run the code on the next
					//packet in the list.
					break;
				}
			}
		}
		
		list = sortedList;


	}

	/**
	 * Opens the image file by merging the content in the maintained list
	 * buffer.
	 */

	public void openImage() {
		try {
			img.openImage(list);
		}

		/*
		 * 
		 * throws BadImageHeaderException if the maintained list buffer has an
		 * invalid image header, throws BadImageContentException if the
		 * maintained list buffer has an invalid image content
		 * 
		 */
		catch (BrokenImageException e) {
			if (!validImageHeader())
				throw new BadImageHeaderException
								("The image is broken due to damaged header.");
			if (!validImageContent())
				throw new BadImageContentException
								("The image is broken due to corrupt content.");
		} catch (Exception e) {
			System.out.println("Please catch the proper Image-related Exception.");
			e.printStackTrace();
		}
	}

	/**
	 * Initiates a Receiver to reconstruct collected packets and open the Image
	 * file, which is specified by args[0].
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java Receiver [filename_on_server]");
			return;
		}
		Receiver recv = new Receiver(args[0]);
		recv.reconstructFile();
//		recv.displayList(); // use for debugging
		recv.openImage();
	}
}