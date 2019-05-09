///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  Receiver.java
// File:             BadImageHeaderException.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain, ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  Jim Skrentny
//
/**
 * 
 * This exception should be thrown if the maintained list buffer has an
 * invalid image header
 *
 */
public class BadImageHeaderException extends RuntimeException {

	/**
	 * Constructs a BadImageHeaderException with a message
	 * @param message the error message
	 */
	public BadImageHeaderException(String message) {
		super(message);
	}

	/**
	 * Constructs a BrokenImageException
	 */
	public BadImageHeaderException() {
		super();
	}
}
