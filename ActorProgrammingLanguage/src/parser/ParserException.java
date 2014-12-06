/*
 * @author Kyran Adams
 */
package parser;

// TODO: Auto-generated Javadoc
/**
 * The Class ParserException.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class ParserException extends RuntimeException {
    
    /**
     * Instantiates a new parser exception.
     */
    public ParserException() {
        super();
    }
    
    /**
     * Instantiates a new parser exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     * @param enableSuppression
     *            the enable suppression
     * @param writableStackTrace
     *            the writable stack trace
     */
    public ParserException(final String message, final Throwable cause,
            final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    /**
     * Instantiates a new parser exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public ParserException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Instantiates a new parser exception.
     *
     * @param message
     *            the message
     */
    public ParserException(final String message) {
        super(message);
    }
    
    /**
     * Instantiates a new parser exception.
     *
     * @param cause
     *            the cause
     */
    public ParserException(final Throwable cause) {
        super(cause);
    }
    
}
