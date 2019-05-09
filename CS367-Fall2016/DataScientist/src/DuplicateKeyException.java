/**
 * Exception for Duplicate Key.
 * 
 * @author CS367
 */

public class DuplicateKeyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateKeyException() {
		super("Duplicate keys not allowed.");
	}

}
