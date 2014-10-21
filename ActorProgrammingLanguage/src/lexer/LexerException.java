package lexer;

public class LexerException extends RuntimeException {

	public LexerException(final String string) {
		super(string);
	}

	public LexerException() {
		super();
	}

	public LexerException(final String arg0, final Throwable arg1,
			final boolean arg2, final boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public LexerException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public LexerException(final Throwable arg0) {
		super(arg0);
	}

}
