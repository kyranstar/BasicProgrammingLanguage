/*
 * @author Kyran Adams
 */
package lexer;

// TODO: Auto-generated Javadoc
/**
 * The Class LexerException.
 */
public class LexerException extends RuntimeException {

	/**
	 * Instantiates a new lexer exception.
	 *
	 * @param string the string
	 */
	public LexerException(final String string) {
		super(string);
	}

	/**
	 * Instantiates a new lexer exception.
	 */
	public LexerException() {
		super();
	}

	/**
	 * Instantiates a new lexer exception.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * @param arg3 the arg3
	 */
	public LexerException(final String arg0, final Throwable arg1,
			final boolean arg2, final boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * Instantiates a new lexer exception.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 */
	public LexerException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Instantiates a new lexer exception.
	 *
	 * @param arg0 the arg0
	 */
	public LexerException(final Throwable arg0) {
		super(arg0);
	}

}
