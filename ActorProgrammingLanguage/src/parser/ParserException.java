package parser;

public class ParserException extends RuntimeException {

	public ParserException(final String string) {
		super(string);
	}

	public ParserException() {
		super();
	}

	public ParserException(final String arg0, final Throwable arg1,
			final boolean arg2, final boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ParserException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public ParserException(final Throwable arg0) {
		super(arg0);
	}

}
