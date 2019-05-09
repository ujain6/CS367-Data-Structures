
/**
 * A simple exception class for functional issues that can arise from various operations
 *
 * @author jmishra
 */
public class WhatsAppException extends Exception
{

    /**
     * Construct a new instance of this exception with a custom message
     *
     * @param message the custom message to be wrapped around by this exception
     */
    public WhatsAppException(String message)
    {
        super(message);
    }
}
