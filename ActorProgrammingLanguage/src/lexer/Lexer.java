package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
	private int currentLine = 0;
	private int lastEndLine = 0;
	private int position = 0;
	private final String code;
	private final char[] ignoredCharacters = { ' ' };
	// Token matchers, ordered by length to avoid hitting shorter ones first.
	// ("len" before "length")
	private TokenMatcher[] matchers;

	public Lexer(final String code) {
		this.code = removeIgnored(code);
	}

	private String removeIgnored(String code) {
		for (final char ignored : ignoredCharacters) {
			code = code.replaceAll(String.valueOf(ignored), "");
		}
		return code;
	}

	private Token matchToken() {
		final String codeFromPosition = code.substring(position);
		for (final TokenMatcher m : matchers) {
			if (m.matches(codeFromPosition)) {
				final Token t = m.getToken(codeFromPosition);
				// if there is a newline in the text
				final String tokenText = t.getText();
				if (tokenText.indexOf('\n') != -1) {
					// add number of \n in text to the currentLine
					currentLine += tokenText.length()
							- tokenText.replace("\n", "").length();
					lastEndLine = position + tokenText.lastIndexOf('\n');
				}
				position += tokenText.length();
			}
		}
		throw new LexerException("Could not match token at line " + currentLine
				+ " at position " + (position - lastEndLine));
	}

	public List<Token> lex() {
		final List<Token> tokens = new ArrayList<>();

		return tokens;
	}
}
