///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  StudentCenter.java
// File:             Queue.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain (ujain6@wisc.edu)
// CS Login:         utkarsh
// Lecturer's Name:  James Skrentny
// 
//
/**
 * An ordered collection of items, where items are added to the rear and removed
 * from the front.
 */
public class Queue<E> implements QueueADT<E> {

	private Listnode<E> head;
	private Listnode<E> tail;
	private int numItems;

	public Queue() {

		head = new Listnode<E>(null);
		tail = head;
		numItems = 0;

	}

	/**
	 * Adds an item to the rear of the queue.
	 * 
	 * @param item
	 *            the item to add to the queue.
	 * @throws IllegalArgumentException
	 *             if item is null.
	 */
	public void enqueue(E item) {

		if (item == null) {

			throw new IllegalArgumentException();
		}

		Listnode<E> newNode = new Listnode<E>(item);

		// If there is only the header node
		if (numItems == 0) {

			head.setNext(newNode);
			tail = tail.getNext();
			numItems++;

		} else {
			tail.setNext(newNode);
			tail = tail.getNext();
			numItems++;
		}

	}

	/**
	 * Removes an item from the front of the Queue and returns it.
	 * 
	 * @return the front item in the queue.
	 * @throws EmptyQueueException
	 *             if the queue is empty.
	 */
	public E dequeue() {

		if (numItems == 0) {

			throw new EmptyQueueException();

		}

		Listnode<E> nodeToRemove = head.getNext();

		head.setNext(head.getNext().getNext());

		numItems--;

		return nodeToRemove.getData();

	}

	/**
	 * Returns the item at front of the Queue without removing it.
	 * 
	 * @return the front item in the queue.
	 * @throws EmptyQueueException
	 *             if the queue is empty.
	 */
	public E peek() {

		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		return head.getNext().getData();
	}

	/**
	 * Returns true iff the Queue is empty.
	 * 
	 * @return true if queue is empty; otherwise false.
	 */
	public boolean isEmpty() {

		return numItems == 0;
	}

	/**
	 * Removes all items in the queue leaving an empty queue.
	 */
	public void clear() {

		head.setNext(null);
		tail = head;

	}

	/**
	 * Returns the number of items in the Queue.
	 * 
	 * @return the size of the queue.
	 */
	public int size() {
		return numItems;
	}

}
