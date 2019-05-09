///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  StudentCenter.java
// File:             PriorityQueueItem.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain (ujain6@wisc.edu)
// CS Login:         utkarsh
// Lecturer's Name:  James Skrentny
// 
//
/**
 * 
 * Class to represent object stored at every entry in the PriorityQueue. ie, The
 * internal node structure of {@link PriorityQueue}
 * 
 * @author CS367
 *
 * @param <E>
 *            the generic type of the data content stored in the list
 */
public class PriorityQueueItem<E> implements Comparable<PriorityQueueItem<E>> {

	private int priority;
	private Queue<E> queue;

	public PriorityQueueItem(int priority) {

		this.priority = priority;
		queue = new Queue<E>();
	}

	public int getPriority() {

		return priority;
	}

	public Queue<E> getList() {

		return queue;
	}

	/**
	 * Add an item to the queue of this PriorityQueueItem object
	 * 
	 * @param item
	 *            item to add to the queue
	 */
	public void add(E item) {

		if (item == null) {
			throw new IllegalArgumentException();
		}

		queue.enqueue(item);
	}

	/**
	 * Compares this Node to another node on basis of priority
	 * 
	 * @param o
	 *            other node to compare to
	 * @return -1 if this node's priority is lesser, +1 if this nodes priority
	 *         is higher after, else 0 if priorities are the same.
	 */
	@Override
	public int compareTo(PriorityQueueItem<E> o) {

		
		if(o.getPriority() < priority){
			return 1;
		}
		else if(o.getPriority() > priority){
			return -1;
		}
		else{
			return 0;
		}
		
	}
}
