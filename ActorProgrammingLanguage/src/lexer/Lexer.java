package lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lexer.Token.TokenType;

public class Lexer {
	LexerInformation lexInfo = new LexerInformation();
	private final String code;
	// Token matchers, ordered by length to avoid hitting shorter ones first.
	// ("len" before "length")
	private final TokenMatcher[] matchers = { new TokenMatchers.SPACE(),
			new TokenMatchers.NUMBER(), new TokenMatchers.OPERATOR(),
			new TokenMatchers.IDENTIFIER() };
	private final List<TokenType> typesToIgnore = Arrays.asList(
			TokenType.SPACE, TokenType.EOF);

	public Lexer(final String code) {
		this.code = code;
	}

	private Token matchToken() {
		final String codeFromPosition = code.substring(lexInfo.position);

		for (final TokenMatcher m : matchers) {
			if (m.matches(codeFromPosition, lexInfo)) {
				final Token t = m.getToken(codeFromPosition, lexInfo);
				// if there is a newline in the text
				final String tokenText = t.getText();
				// Update lexer information
				// if the text contains a new line
				if (tokenText.indexOf('\n') != -1) {
					// add number of \n in text to the currentLine
					lexInfo.currentLine += tokenText.length()
							- tokenText.replace("\n", "").length();
					lexInfo.lastEndLine = lexInfo.position
							+ tokenText.lastIndexOf('\n');
				}
				lexInfo.position += tokenText.length();
				return t;
			}
		}
		throw new LexerException("Could not match character '"
				+ code.charAt(lexInfo.position) + "' with token"
				+ lexInfo.getMessage());
	}

	public List<Token> lex() {
		final List<Token> tokens = new ArrayList<>();

		while (lexInfo.position < code.length()) {
			final Token t = matchToken();
			if (!typesToIgnore.contains(t.getType())) {
				tokens.add(t);
			}
		}

		return tokens;
	}
}
