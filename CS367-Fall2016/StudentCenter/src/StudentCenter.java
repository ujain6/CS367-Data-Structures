
///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            Student Centre
// Files:            Course.java, EmptyQueueException.java, Listnode.java, 
//					 PriorityQueue.java, PriorityQueueItem.java,
//					 PriorityQueueIterator.java, Queue.java, QueueADT.java,
//				     Student.java, StudentCenter.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain
// Email:            ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  James Skrentny
//
//
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * Student Center abstraction for our simulation. Execution starts here.
 * 
 * @author CS367
 *
 */
public class StudentCenter {

	private static int DEFAULT_POINTS = 100;
	// Global lists of all courses and students
	private static List<Course> courseList = new ArrayList<Course>();
	private static List<Student> studentList = new ArrayList<Student>();

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Bad invocation! Correct usage: " + "java StudentCentre <StudentCourseData file>"
					+ "<CourseRosters File> + <StudentCourseAssignments File>");
			System.exit(1);
		}

		boolean didInitialize = readData(args[0]);

		if (!didInitialize) {
			System.err.println("Failed to initialize the application!");
			System.exit(1);
		}

		generateAndWriteResults(args[1], args[2]);
	}

	/**
	 * 
	 * @param fileName
	 *            - input file. Has 3 sections - #Points/Student, #Courses, and
	 *            multiple #Student sections. See P3 page for more details.
	 * @return true on success, false on failure.
	 * 
	 */
	public static boolean readData(String fileName) {
		try {

			char mode = 'q';

			int numOfCoins = DEFAULT_POINTS;
			String studentName, studentID, courseCode = null;
			Student student = null;

			Scanner scanner = new Scanner(new File(fileName));
			String headerOrNot;

			while (scanner.hasNextLine()) {
				
				//Get the String by calling nextLine() and store it into a 
				//variable of string type
				headerOrNot = scanner.nextLine().trim();
				
				//Checks whether the first character of str is '#' or not by 
				//using str.charAt(0)
				
				//If yes...
				if (headerOrNot.charAt(0) == '#') {

					// if it is #Points/Student
					if (headerOrNot.charAt(1) == 'P') {

						// set case = A;
						mode = 'A';

						// else if #Courses
					} else if (headerOrNot.charAt(1) == 'C') {

						// set case = 'B'
						mode = 'B';

						// else if #Student
					} else if (headerOrNot.charAt(1) == 'S') {

						// set case = C;
						mode = 'C';

					}

				} else { // this means the line is not a header

					if (mode == 'A') {
						//The number of coins students are given to spend
						numOfCoins = Integer.parseInt(headerOrNot);

					}
					if (mode == 'B') {

						String[] parts = headerOrNot.split(" ");
						
						//Split and add the course to the list
						Course course = new Course(parts[0], parts[1], 
								                   Integer.parseInt(parts[2]));

						// Add this course to the global list of courses
						courseList.add(course);

					}

					if (mode == 'C') {

						studentName = headerOrNot;

						studentID = scanner.nextLine().trim();

						student = new Student(studentName, studentID, numOfCoins);

						studentList.add(student);
						
						//When we have got the student's name and ID
						mode = 'D';
						

					}
					
					//Mode D activates wehn we need to take the classes and 
					//the coins spent on them
					else if (mode == 'D') {
						String[] parts = headerOrNot.split(" ");

						int coinsAllocated = 0;

						courseCode = parts[0];

						coinsAllocated = Integer.parseInt(parts[1]);
						
						
						//get the course the student is interested in by this
						//method
						Course c = getCourseFromCourseList(courseCode);
						
						//Add the student to the course's priority queue
						c.addStudent(student, coinsAllocated);
						
						//Add the course to the list of the courses the student
						//is interested in
						student.addToCart(c);

					}

				}

			}
			
			//Close the scanner to prevent leaks
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("File Parse Error");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param fileName1
	 *            - output file with a line for each course
	 * @param fileName2
	 *            - output file with a line for each student
	 */
	public static void generateAndWriteResults(String fileName1, String fileName2) {
		try {
			// List Students enrolled in each course
			PrintWriter writer = new PrintWriter(new File(fileName1));
			for (Course c : courseList) {
				writer.println("-----" + c.getCourseCode() + " " + c.getCourseName() + "-----");

				// Core functionality
				c.processRegistrationList();

				// List students enrolled in each course
				int count = 1;
				for (Student s : c.getCourseRegister()) {
					writer.println(count + ". " + s.getid() + "\t" + s.getName());
					s.enrollCourse(c);
					count++;
				}
				writer.println();
			}
			writer.close();

			// List courses each student gets
			writer = new PrintWriter(new File(fileName2));
			for (Student s : studentList) {
				writer.println("-----" + s.getid() + " " + s.getName() + "-----");
				int count = 1;
				for (Course c : s.getEnrolledCourses()) {
					writer.println(count + ". " + c.getCourseCode() + "\t" + c.getCourseName());
					count++;
				}
				writer.println();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Look up Course from classCode
	 * 
	 * @param courseCode
	 * @return Course object
	 */
	private static Course getCourseFromCourseList(String courseCode) {
		for (Course c : courseList) {
			if (c.getCourseCode().equals(courseCode)) {
				return c;
			}
		}

		return null;
	}
}
