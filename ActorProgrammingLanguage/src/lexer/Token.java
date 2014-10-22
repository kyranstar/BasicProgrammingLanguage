package lexer;

public class Token {

	private final String text;
	private final TokenType type;

	public Token(final TokenType type, final String text) {
		this.type = type;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return ("<" + type + "\"" + getText() + "\"" + ">").replaceAll("\n",
				"\\\\n").replaceAll("\t", "\\\\t");
	}

	public static enum TokenType {
		NUMBER, SPACE, EOF, OPERATOR;
	}

}
