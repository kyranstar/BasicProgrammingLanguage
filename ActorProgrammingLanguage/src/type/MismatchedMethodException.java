package type;

public class MismatchedMethodException extends RuntimeException {

	public MismatchedMethodException(final String string) {
		super(string);
	}

	public MismatchedMethodException() {
		super();
	}

	public MismatchedMethodException(final String arg0, final Throwable arg1,
			final boolean arg2, final boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public MismatchedMethodException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MismatchedMethodException(final Throwable arg0) {
		super(arg0);
	}

}
