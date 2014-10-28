package machine;

public class ContextException extends RuntimeException {

	public ContextException(final String string) {
		super(string);
	}

	public ContextException() {
		super();
	}

	public ContextException(final String arg0, final Throwable arg1,
			final boolean arg2, final boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ContextException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public ContextException(final Throwable arg0) {
		super(arg0);
	}

}
