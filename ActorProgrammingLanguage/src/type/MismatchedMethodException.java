/*
 * @author Kyran Adams
 */
package type;

// TODO: Auto-generated Javadoc
/**
 * The Class MismatchedMethodException.
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class MismatchedMethodException extends RuntimeException {

	/**
	 * Instantiates a new mismatched method exception.
	 *
	 * @param string the string
	 */
	public MismatchedMethodException(final String string) {
		super(string);
	}

	/**
	 * Instantiates a new mismatched method exception.
	 */
	public MismatchedMethodException() {
		super();
	}

	/**
	 * Instantiates a new mismatched method exception.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * @param arg3 the arg3
	 */
	public MismatchedMethodException(final String arg0, final Throwable arg1,
			final boolean arg2, final boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * Instantiates a new mismatched method exception.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 */
	public MismatchedMethodException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Instantiates a new mismatched method exception.
	 *
	 * @param arg0 the arg0
	 */
	public MismatchedMethodException(final Throwable arg0) {
		super(arg0);
	}

}
