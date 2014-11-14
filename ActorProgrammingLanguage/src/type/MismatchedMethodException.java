/*
 * @author Kyran Adams
 */
package type;

import parser.ParserException;

// TODO: Auto-generated Javadoc
/**
 * The Class MismatchedMethodException.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class MismatchedMethodException extends ParserException {
    
    /**
     * Instantiates a new mismatched method exception.
     */
    public MismatchedMethodException() {
        super();
    }
    
    /**
     * Instantiates a new mismatched method exception.
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
    public MismatchedMethodException(final String message,
            final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    /**
     * Instantiates a new mismatched method exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public MismatchedMethodException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Instantiates a new mismatched method exception.
     *
     * @param message
     *            the message
     */
    public MismatchedMethodException(final String message) {
        super(message);
    }
    
    /**
     * Instantiates a new mismatched method exception.
     *
     * @param cause
     *            the cause
     */
    public MismatchedMethodException(final Throwable cause) {
        super(cause);
    }
    
}
