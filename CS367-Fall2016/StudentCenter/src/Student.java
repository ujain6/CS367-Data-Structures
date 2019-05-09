///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  StudentCenter.java
// File:             Student.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain (ujain6@wisc.edu)
// CS Login:         utkarsh
// Lecturer's Name:  James Skrentny
// 
//
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Class to represent every Student in our Course Registration environment.
 * 
 * @author CS367
 *
 */
public class Student
	{

	// Do not modify this file!

	private String name;
	private String id;
	private int myCourseCoins;
	private List<Course> myCourseCart;
	private List<Course> myEnrolledCourses;

	public Student(String name, String id, int courseCoins)
		{
		this.name = name;
		this.id = id;
		this.myCourseCoins = courseCoins;
		this.myEnrolledCourses = new ArrayList<Course>();
		this.myCourseCart = new ArrayList<Course>();
		}

	public String getName()
		{
		return this.name;
		}

	public String getid()
		{
		return this.id;
		}

	public int getPoints()
		{
		return this.myCourseCoins;
		}

	public List<Course> getEnrolledCourses()
		{
		return this.myEnrolledCourses;
		}

	public boolean deductCoins(int numCoins)
		{
		if(numCoins > this.myCourseCoins)
			{
			return false;
			}
		this.myCourseCoins -= numCoins;
		return true;
		}

	/**
	 * Adds the course to the list of courses the student is interested in. Is
	 * consistent with the registration queue.
	 * 
	 * @param course
	 */
	public void addToCart(Course course)
		{
		myCourseCart.add(course);
		}

	/**
	 * After the registration list is processed, adds successfully enrolled
	 * courses to Student's list
	 * 
	 * @param course
	 */
	public void enrollCourse(Course course)
		{
		myEnrolledCourses.add(course);
		}

	}
