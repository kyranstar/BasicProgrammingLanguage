package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
	LexerInformation lexInfo = new LexerInformation();
	private final String code;
	private final char[] ignoredCharacters = { ' ' };
	// Token matchers, ordered by length to avoid hitting shorter ones first.
	// ("len" before "length")
	private final TokenMatcher[] matchers = { new TokenMatchers.SPACE(),
			new TokenMatchers.NUMBER() };

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
		throw new LexerException("Could not match character '" + code.charAt(0)
				+ "' with token" + lexInfo.getMessage());
	}

	public List<Token> lex() {
		final List<Token> tokens = new ArrayList<>();

		while (lexInfo.position < code.length()) {
			tokens.add(matchToken());
		}

		return tokens;
	}
}
