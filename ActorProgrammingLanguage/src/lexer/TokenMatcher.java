package lexer;

public abstract class TokenMatcher {

	protected abstract Token getTokenNoCheck(final String code,
			final LexerInformation lexInfo);

	protected abstract boolean matchesNoCheck(final String code,
			final LexerInformation lexInfo);

	public final Token getToken(final String code,
			final LexerInformation lexInfo) {
		if (!matches(code, lexInfo))
			throw new LexerException("Tried to get token " + getClass()
					+ ", but did not match. Was {" + code.charAt(0)
					+ "} instead." + lexInfo.getMessage());
		return getTokenNoCheck(code, lexInfo);
	}

	public final boolean matches(final String code,
			final LexerInformation lexInfo) {
		try {
			return matchesNoCheck(code, lexInfo);
		} catch (final StringIndexOutOfBoundsException e) {
			// We are at the EOF
			return false;
		}
	}
}
