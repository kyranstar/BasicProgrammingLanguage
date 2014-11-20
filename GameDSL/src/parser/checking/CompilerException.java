/*
 * @author Kyran Adams
 */
package parser.checking;

// TODO: Auto-generated Javadoc
/**
 * The Class compilerException.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class CompilerException extends Exception {

    /**
     * Instantiates a new compiler exception.
     */
    public CompilerException() {
        super();
    }

    /**
     * Instantiates a new compiler exception.
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
    public CompilerException(final String message, final Throwable cause,
            final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Instantiates a new compiler exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public CompilerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new compiler exception.
     *
     * @param message
     *            the message
     */
    public CompilerException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new compiler exception.
     *
     * @param cause
     *            the cause
     */
    public CompilerException(final Throwable cause) {
        super(cause);
    }

}
