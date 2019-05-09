/**
 * This exception should be thrown whenever the received image buffer has any
 * broken part.
 * 
 * @author zarcen (Wei-Chen Chen)
 */
public class BrokenImageException extends Exception {

	/**
	 * Constructs a BrokenImageException with a message
	 * @param s the error message
	 */
	public BrokenImageException(String s) {
		super(s);
	}

	/**
	 * Constructs a BrokenImageException
	 */
	public BrokenImageException() {
	}
}
