package lexer;

import java.util.ArrayList;
import java.util.List;

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

	public static class BOOLEAN extends TokenMatcher {

		@Override
		public Token getTokenNoCheck(final String code,
				final LexerInformation lexInfo) {
			if (code.startsWith("true")) {
				return new Token(TokenType.BOOLEAN, "true");
			}
			if (code.startsWith("false")) {
				return new Token(TokenType.BOOLEAN, "false");
			}
			throw new LexerException("Tried to get token " + getClass()
					+ ", but did not match. Was {" + code.charAt(0)
					+ "} instead. Should never get here! Compiler bug.");
		}

		@Override
		public boolean matchesNoCheck(final String code,
				final LexerInformation lexInfo) {
			return code.startsWith("true") || code.startsWith("false");
		}

	};

	public static class OPERATOR extends TokenMatcher {
		private static final List<StringToOperator> operators = new ArrayList<>();
		static {
			operators.add(new StringToOperator(";", TokenType.SEMI));
			operators.add(new StringToOperator(",", TokenType.COMMA));
			operators.add(new StringToOperator("=", TokenType.EQUAL));

			operators.add(new StringToOperator("*", TokenType.MULDIV));
			operators.add(new StringToOperator("/", TokenType.MULDIV));
			operators.add(new StringToOperator("+", TokenType.PLUSMINUS));
			operators.add(new StringToOperator("-", TokenType.PLUSMINUS));
			operators.add(new StringToOperator("^", TokenType.RAISED));

			operators
			.add(new StringToOperator("<=", TokenType.LESS_THAN_EQUAL));
			operators.add(new StringToOperator(">=",
					TokenType.GREATER_THAN_EQUAL));

			operators.add(new StringToOperator("<", TokenType.LESS_THAN));
			operators.add(new StringToOperator(">", TokenType.GREATER_THAN));

			operators.add(new StringToOperator("&&", TokenType.AND));
			operators.add(new StringToOperator("||", TokenType.OR));
		}

		private static class StringToOperator {
			public StringToOperator(final String string, final TokenType type) {
				text = string;
				this.type = type;
			}

			public String text;
			public TokenType type;
		}

		@Override
		public Token getTokenNoCheck(final String code,
				final LexerInformation lexInfo) {
			for (final StringToOperator entry : operators) {
				if (code.startsWith(entry.text)) {
					return new Token(entry.type, entry.text);
				}
			}

			throw new LexerException("Unidentified token");
		}

		@Override
		public boolean matchesNoCheck(final String code,
				final LexerInformation lexInfo) {
			for (final StringToOperator s : operators) {
				if (code.startsWith(s.text)) {
					return true;
				}
			}
			return false;
		}
	}

	public static class IF extends TokenMatcher {
		final String ifString = "if";
		final String elseString = "else";

		@Override
		protected Token getTokenNoCheck(final String code,
				final LexerInformation lexInfo) {

			if (code.startsWith(ifString)) {
				return new Token(TokenType.IF, ifString);
			} else {
				return new Token(TokenType.ELSE, elseString);
			}
		}

		@Override
		protected boolean matchesNoCheck(final String code,
				final LexerInformation lexInfo) {
			return code.startsWith(ifString) || code.startsWith(elseString);
		}

	}

	public static class BRACKETS extends TokenMatcher {
		@Override
		public Token getTokenNoCheck(final String code,
				final LexerInformation lexInfo) {
			final char c = code.charAt(0);
			final String token = String.valueOf(c);
			switch (code.charAt(0)) {
			case '(':
				return new Token(TokenType.OPEN_PARENS, token);
			case ')':
				return new Token(TokenType.CLOSE_PARENS, token);
			}
			throw new LexerException("Unidentified token: " + token);
		}

		@Override
		public boolean matchesNoCheck(final String code,
				final LexerInformation lexInfo) {
			return code.startsWith("(") || code.startsWith(")");
		}
	}

	public static class IDENTIFIER extends TokenMatcher {

		@Override
		public Token getTokenNoCheck(String code, final LexerInformation lexInfo) {
			final StringBuilder identifier = new StringBuilder();
			do {
				identifier.append(code.charAt(0));
				code = code.substring(1);
			} while (matches(code, lexInfo));
			return new Token(TokenType.IDENTIFIER, identifier.toString());
		}

		@Override
		public boolean matchesNoCheck(final String code,
				final LexerInformation lexInfo) {
			return Character.isAlphabetic(code.charAt(0))
					|| code.charAt(0) == '_';
		}

	};
}
