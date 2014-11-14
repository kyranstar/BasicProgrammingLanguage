/*
 * @author Kyran Adams
 */
package lexer;

/**
 * The Class LexerException. This represents an error during the lexer phase.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class LexerException extends RuntimeException {
    
    /**
     * Instantiates a new lexer exception.
     */
    public LexerException() {
        super();
    }
    
    /**
     * Instantiates a new lexer exception.
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
    public LexerException(final String message, final Throwable cause,
            final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    /**
     * Instantiates a new lexer exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public LexerException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Instantiates a new lexer exception.
     *
     * @param message
     *            the message
     */
    public LexerException(final String message) {
        super(message);
    }
    
    /**
     * Instantiates a new lexer exception.
     *
     * @param cause
     *            the cause
     */
    public LexerException(final Throwable cause) {
        super(cause);
    }
    
}
