/*
 * @author Kyran Adams
 */
package machine;

// TODO: Auto-generated Javadoc
/**
 * The Class ContextException.
 */
public class ContextException extends RuntimeException {

	/**
	 * Instantiates a new context exception.
	 *
	 * @param string the string
	 */
	public ContextException(final String string) {
		super(string);
	}

	/**
	 * Instantiates a new context exception.
	 */
	public ContextException() {
		super();
	}

	/**
	 * Instantiates a new context exception.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * @param arg3 the arg3
	 */
	public ContextException(final String arg0, final Throwable arg1,
			final boolean arg2, final boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * Instantiates a new context exception.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 */
	public ContextException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Instantiates a new context exception.
	 *
	 * @param arg0 the arg0
	 */
	public ContextException(final Throwable arg0) {
		super(arg0);
	}

}
