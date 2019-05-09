
///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  StudentCenter.java
// File:             Course.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain (ujain6@wisc.edu)
// CS Login:         utkarsh
// Lecturer's Name:  James Skrentny
// 
//
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Class to represent every Course in our Course Registration environment
 * 
 * @author CS367
 *
 */

public class Course {

	private String courseCode;
	private String name;

	// Number of students allowed in the course
	private int maxCapacity;

	// Number of students enrolled in the course
	private int classCount;

	// The PriorityQueue structure
	private PriorityQueue<Student> registrationQueue;

	// List of students who are finally enrolled in the course
	private List<Student> courseRoster;

	public Course(String classCode, String name, int maxCapacity) {

		courseCode = classCode;

		this.name = name;

		this.maxCapacity = maxCapacity;

		classCount = 0;

		registrationQueue = new PriorityQueue<Student>();

		courseRoster = new ArrayList<>();
	}

	/**
	 * Creates a new PriorityqueueItem - with appropriate priority(coins) and
	 * this student in the item's queue. Add this item to the registrationQueue.
	 * 
	 * @param student
	 *            the student
	 * @param coins
	 *            the number of coins the student has
	 */
	public void addStudent(Student student, int coins) {
		// This method is called from Studentcenter.java

		if (student.deductCoins(coins)) {

			// Enqueue a newly created PQItem.
			// This PQ item has the priority, i.e., the coins and the student
			// passed as the parameter in its queue.
			PriorityQueueItem<Student> pqItem = new PriorityQueueItem<Student>(coins);

			// add the student to the queue of this new incoming PQItem
			pqItem.add(student);

			// Add this pqItem to the course's priroity queue
			// Checking if a PriorityQueueItem with the same priority already
			// exists is done in the enqueue method. Iterator is used for this
			// purpose
			registrationQueue.enqueue(pqItem);
		}

	}

	/**
	 * Populates the courseRoster from the registration list. Use the
	 * PriorityQueueIterator for this task.
	 */
	public void processRegistrationList() {

		// PriorityQueueIterator used for this task.
		PriorityQueueIterator<Student> itr = (PriorityQueueIterator<Student>) registrationQueue.iterator();

		while (itr.hasNext()) {

			PriorityQueueItem<Student> itemToBeDequeued = itr.next();

			while (!itemToBeDequeued.getList().isEmpty()) {

				// To check that the number of students added don't exceed the
				// maximum capacity of the class
				if (classCount < maxCapacity) {

					Student studentSelected = itemToBeDequeued.getList().dequeue();

					courseRoster.add(studentSelected);
					classCount++;

				} else {
					break;

				}

			}

		}

	}

	public String getCourseName() {
		return name;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public List<Student> getCourseRegister() {
		return courseRoster;
	}
}
