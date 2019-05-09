
///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  StudentCenter.java
// File:             PriorityQueue.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain (ujain6@wisc.edu)
// CS Login:         utkarsh
// Lecturer's Name:  James Skrentny
// 
//
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * PriorityQueue implemented as a Binary Heap backed by an array. Implements
 * QueueADT. Each entry in the PriorityQueue is of type PriorityQueueNode<E>.
 * First element is array[1]
 * 
 * @author CS367
 *
 * @param <E>
 */
public class PriorityQueue<E> implements QueueADT<PriorityQueueItem<E>> {
	private final int DEFAULT_CAPACITY = 100;

	// Number of elements in heap
	private int currentSize;

	/**
	 * The heap array. First element is array[1]
	 */
	private PriorityQueueItem<E>[] array;

	/**
	 * Construct an empty PriorityQueue.
	 */
	public PriorityQueue() {
		currentSize = 0;
		// the below code initializes the array.. similar code used for
		// expanding.
		array = (PriorityQueueItem<E>[]) Array.newInstance(PriorityQueueItem.class, DEFAULT_CAPACITY + 1);
	}

	/**
	 * Copy constructor
	 * 
	 * @param pq
	 *            PriorityQueue object to be copied
	 */
	public PriorityQueue(PriorityQueue<E> pq) {
		this.currentSize = pq.currentSize;
		this.array = Arrays.copyOf(pq.array, currentSize + 1);
	}

	/**
	 * Adds an item to this PriorityQueue. If array is full, double the array
	 * size.
	 * 
	 * @param item
	 *            object of type PriorityQueueItem<E>.
	 */
	@Override
	public void enqueue(PriorityQueueItem<E> item) {
		if (item == null) {

			throw new IllegalArgumentException();

		}

		// Check if array is full - double if necessary
		if (currentSize == array.length) {

			doubleArray();

		}

		// Check all nodes and find if one with equal priority exists.
		// Add to the existing node's list if it does

		boolean found = false;
		for (int i = 1; i <= currentSize; i++) {
			if (item.compareTo(array[i]) == 0) {

				// add all the students in the item's list to the list of the
				// node already in the priority queue
				while (!item.getList().isEmpty()) {

					array[i].getList().enqueue(item.getList().dequeue());

				}
				// Flag changes to true when a node already present in the queue
				// has the same priority as the incoming node.
				found = true;

			}
		}
		// Else create new node with value added to list and percolate it up
		if (!found) {

			array[currentSize + 1] = item;
			currentSize++;

			int child = currentSize;
			boolean check = false;

			// code to percolate up
			while (!check && currentSize >= 2) {

				int parent = child / 2;

				// if the parent is smaller than the child, swap
				if (array[child].compareTo(array[parent]) > 0) {

					// swap
					PriorityQueueItem<E> temp = array[child];
					array[child] = array[parent];
					array[parent] = temp;

				} else
					check = true;

				child = parent;

				if (child == 1)
					check = true;

			}
		}
	}

	/**
	 * Returns the number of items in this PriorityQueue.
	 * 
	 * @return the number of items in this PriorityQueue.
	 */
	public int size() {

		return currentSize;
	}

	/**
	 * Returns a PriorityQueueIterator. The iterator should return the
	 * PriorityQueueItems in order of decreasing priority.
	 * 
	 * @return iterator over the elements in this PriorityQueue
	 */
	public Iterator<PriorityQueueItem<E>> iterator() {

		return new PriorityQueueIterator<E>(this);
	}

	/**
	 * Returns the largest item in the priority queue.
	 * 
	 * @return the largest priority item.
	 * @throws NoSuchElementException
	 *             if empty.
	 */
	@Override
	public PriorityQueueItem<E> peek() {

		return array[1];
	}

	/**
	 * Removes and returns the largest item in the priority queue. Switch last
	 * element with removed element, and percolate down.
	 * 
	 * @return the largest item.
	 * @throws NoSuchElementException
	 *             if empty.
	 */
	@Override
	public PriorityQueueItem<E> dequeue() {

		PriorityQueueItem<E> itemToBeRemoved = null;

		// if there is only one item in the heap
		if (currentSize == 1) {

			itemToBeRemoved = array[1];
			array[1] = null;
			currentSize--;

		} else {

			// Remove first element
			itemToBeRemoved = array[1];
			// Replace with last element, percolate down
			array[1] = array[currentSize];

			// Delete the last node, in essence, set the last position to null
			array[currentSize] = null;

			currentSize--;

			// Percolate Down
			percolateDown(1);

		}
		return itemToBeRemoved;

	}

	/**
	 * Heapify Establish heap order property from an arbitrary arrangement of
	 * items. ie, initial array that does not satisfy heap property. Runs in
	 * linear time.
	 */
	private void buildHeap() {
		for (int i = currentSize / 2; i > 0; i--)
			percolateDown(i);
	}

	/**
	 * Make this PriorityQueue empty.
	 */
	public void clear() {
		array = (PriorityQueueItem<E>[]) Array.newInstance(PriorityQueueItem.class, DEFAULT_CAPACITY + 1);

		currentSize = 0;
	}

	/**
	 * Internal method to percolate down in the heap.
	 * <a href="https://en.wikipedia.org/wiki/Binary_heap#Extract">Wiki</a>}
	 * 
	 * @param hole
	 *            the index at which the percolate begins.
	 */
	private void percolateDown(int hole) {

		if (hole < 0) {
			throw new IllegalArgumentException();
		}

		int parent = hole;
		boolean flag = false;

		while (!flag) {

			// The left and right childs of the parent node
			int rightChild = (parent * 2) + 1;
			int leftChild = parent * 2;

			// True if both children present
			if (leftChild <= currentSize && rightChild <= currentSize) {

				// case when left child is bigger(higher priority) than right
				// child
				if (array[leftChild].compareTo(array[rightChild]) > 0) {

					// if leftChild has greater priority than parent
					if (array[leftChild].compareTo(array[parent]) > 0) {

						// swapping the parent with the left child
						PriorityQueueItem<E> temp = array[leftChild];
						array[leftChild] = array[parent];
						array[parent] = temp;

						// leftChild is the new parent
						parent = leftChild;
					}

					// if the priority of child is less that parent
					else {
						flag = true;
					}
				}

				// case when right child is bigger(higher priority) than left
				// child
				else if (array[rightChild].compareTo(array[leftChild]) > 0) {

					// if RightChild has greater priority than parent
					if (array[rightChild].compareTo(array[parent]) > 0) {

						// swapping the parent with the left child
						PriorityQueueItem<E> temp = array[rightChild];
						array[rightChild] = array[parent];
						array[parent] = temp;

						// rightChild is the new parent
						parent = rightChild;
					}

					// if the priority of child is less that parent
					else {
						flag = true;
					}
				}
			}

			// if only leftChild exists
			else if (leftChild <= currentSize) {

				// if leftChild has greater priority than parent
				if (array[leftChild].compareTo(array[parent]) > 0) {

					// replacing the leftChild with parent
					PriorityQueueItem<E> temp = array[leftChild];
					array[leftChild] = array[parent];
					array[parent] = temp;

					// setting leftChild as new parent
					parent = leftChild;
				}

				// if the priority of child is less that parent
				else {
					flag = true;
				}
			}

			// the parent node has no children
			else if (leftChild > currentSize && rightChild > currentSize) {
				flag = true;
			}

			// if there is no element at index of new parent
			if (parent >= currentSize) {
				flag = true;
			}
		}

	}

	/**
	 * Internal method to expand array.
	 */
	private void doubleArray() {
		PriorityQueueItem<E>[] newArray;

		newArray = (PriorityQueueItem<E>[]) Array.newInstance(PriorityQueueItem.class, array.length * 2);

		for (int i = 0; i < array.length; i++)
			newArray[i] = array[i];
		array = newArray;
	}

	@Override
	public boolean isEmpty() {
		if (currentSize == 0)
			return true;
		return false;
	}
}
