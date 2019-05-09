///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  StudentCenter.java
// File:             PriorityQueueIterator.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain (ujain6@wisc.edu)
// CS Login:         utkarsh
// Lecturer's Name:  James Skrentny
// 
//
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PriorityQueueIterator<T> implements Iterator<PriorityQueueItem<T>> {

	private PriorityQueue<T> priorityQueue;

	/**
	 * Constructs a PriorityQueueIterator by utilizing a copy of the
	 * PriorityQueue. Hint : The local priorityQueue object need not be
	 * preserved, and hence you can use the dequeue() operation.
	 * 
	 * @param pq
	 */
	public PriorityQueueIterator(PriorityQueue<T> pq) {

		// This copies the contents of the passed parameter to the local object.
		priorityQueue = new PriorityQueue<>(pq);

	}

	/**
	 * Returns true if the iteration has more elements.
	 * 
	 * @return true/false
	 */
	@Override
	public boolean hasNext() {

		return priorityQueue.size() != 0;
	}

	/**
	 * Returns the next element in the iteration. The iterator should return the
	 * PriorityQueueItems in order of decreasing priority.
	 * 
	 * @return the next element in the iteration
	 * @throws NoSuchElementException
	 *             if the iteration has no more elements
	 */
	@Override
	public PriorityQueueItem<T> next() {
	
		
		if (priorityQueue.size() == 0) {
			throw new NoSuchElementException();
		}

		return priorityQueue.dequeue();
	}

	/**
	 * Unsupported in this iterator for this assignment
	 */
	@Override
	public void remove() {
		// Do not implement
		throw new UnsupportedOperationException();
	}

}
