
/**
 * A Single-linked linkedlist with a "dumb" header node (no data in the node),
 * but without a tail node. It implements ListADT<E> and returns
 * PacketLinkedListIterator when requiring a iterator.
 */
public class PacketLinkedList<E> implements ListADT<E> {
	
	private Listnode<E> head;
	private int numItems;

	/**
	 * Constructs a empty PacketLinkedList
	 */
	public PacketLinkedList() {
		
		//Linking to the header node
		head = new Listnode<E>(null);
		numItems = 0;
	}

	@Override
	public void add(E item) {

		if (item == null) {
			throw new IllegalArgumentException();
		}

		Listnode<E> newNode = new Listnode<E>(item);

		Listnode<E> curr = head;
		
		//Traverse to the last node in the list
		while (curr.getNext() != null) {

			curr = curr.getNext();

		}
		
		//Link the last node to the node to be added
		curr.setNext(newNode);
		numItems++;

	}

	@Override
	public void add(int pos, E item) {

		if (item == null) {
			throw new IllegalArgumentException();
		}
		
		if(pos < 0 || pos >= numItems){
			throw new IndexOutOfBoundsException();
		}
		
		Listnode<E> curr = head; 
		
		int counter = 0;
		
		//Traverse to the (n-1)th node, the node before the node at which the 
		//new node is to be inserted
		while (counter != pos - 1) {

			curr = curr.getNext();
			counter++;

		}

		Listnode<E> newNode = new Listnode<E>(item);
		
		newNode.setNext(curr.getNext());
		curr.setNext(newNode);
		numItems++;

	}

	@Override
	public boolean contains(E item) {

		if (item == null) {

			throw new IllegalArgumentException();
		}

		Listnode<E> curr = head.getNext();
		boolean found = false;
		while (curr.getNext() != null && !found) {

			if (curr.getData().equals(item)) {
				found = true;

			} else {
				curr = curr.getNext();

			}

		}

		return found;
	}

	@Override
	public E get(int pos) {
		Listnode<E> curr = head.getNext();
		int counter = 0;
		while (counter != pos) {

			curr = curr.getNext();
			counter++;

		}
		return curr.getData();
	}

	@Override
	public boolean isEmpty() {
		return numItems == 0;
	}

	@Override
	public E remove(int pos) {

		if(pos < 0 || pos > numItems){
			throw new IndexOutOfBoundsException();
		}
		Listnode<E> curr = head;
		
		Listnode<E> nodeToRemove = null;
		
		//Traverse to the (n-1)th node
		for(int i =0; i<pos; i++)	
			curr = curr.getNext();
		
		nodeToRemove = curr.getNext();
		
		// Set the next field of the nth node to the value in the next 
		//field of the nth node
		curr.setNext(curr.getNext().getNext());
		numItems--;

		return nodeToRemove.getData();	


	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	//This method makes the list "iterable".
	public PacketLinkedListIterator<E> iterator() {
		return new PacketLinkedListIterator<E>(head);
	}

}
