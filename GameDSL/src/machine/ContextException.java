/*
 * @author Kyran Adams
 */
package machine;

/**
 * The Class ContextException. Represents when a variable is asked for but isn't
 * in context.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class ContextException extends RuntimeException {

    /**
     * Instantiates a new context exception.
     */
    public ContextException() {
        super();
    }

    /**
     * Instantiates a new context exception.
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
    public ContextException(final String message, final Throwable cause,
            final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Instantiates a new context exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public ContextException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new context exception.
     *
     * @param message
     *            the message
     */
    public ContextException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new context exception.
     *
     * @param cause
     *            the cause
     */
    public ContextException(final Throwable cause) {
        super(cause);
    }

}
