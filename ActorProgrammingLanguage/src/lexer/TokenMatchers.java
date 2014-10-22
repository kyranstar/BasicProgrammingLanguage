package lexer;

import lexer.Token.TokenType;

public class TokenMatchers {
	public static class SPACE extends TokenMatcher {

		@Override
		public Token getTokenNoCheck(String code, final LexerInformation lexInfo) {
			final StringBuilder spaces = new StringBuilder();
			do {
				final char c = code.charAt(0);
				spaces.append(c);
				code = code.substring(1);
			} while (matches(code, lexInfo));
			return new Token(TokenType.SPACE, spaces.toString());
		}

		@Override
		public boolean matchesNoCheck(final String code,
				final LexerInformation lexInfo) {
			return Character.isWhitespace(code.charAt(0));
		}

	}

	public static class NUMBER extends TokenMatcher {

		@Override
		public Token getTokenNoCheck(String code, final LexerInformation lexInfo) {
			final StringBuilder number = new StringBuilder();
			do {
				number.append(code.charAt(0));
				code = code.substring(1);
			} while (matches(code, lexInfo));
			return new Token(TokenType.NUMBER, number.toString());
		}

		@Override
		public boolean matchesNoCheck(final String code,
				final LexerInformation lexInfo) {
			return Character.isDigit(code.charAt(0)) || code.charAt(0) == '.';
		}

	};

	public static class OPERATOR extends TokenMatcher {
		@Override
		public Token getTokenNoCheck(final String code,
				final LexerInformation lexInfo) {
			return new Token(TokenType.OPERATOR, code.charAt(0) + "");
		}

		@Override
		public boolean matchesNoCheck(final String code,
				final LexerInformation lexInfo) {
			return code.startsWith("+") || code.startsWith("-")
					|| code.startsWith("*") || code.startsWith("/");

		}

	}
}
