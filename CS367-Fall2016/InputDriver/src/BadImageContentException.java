///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  Receiver.java
// File:             BadImageContentException.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain, ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  Jim Skrentny
//
/**
 * 
 * This exception should be thrown if the maintained list buffer has an invalid
 *  image content
 * 
 */
public class BadImageContentException extends RuntimeException {

	/**
	 * Constructs a BadImageContentException with a message
	 * @param message the error message
	 */
	public BadImageContentException(String message) {
		super(message);
	}

	/**
	 * Constructs a BrokenImageException
	 */
	public BadImageContentException() {
		super();
	}
}
