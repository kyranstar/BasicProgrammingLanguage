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

	public TokenType getType() {
		return type;
	}

	@Override
	public String toString() {
		return ("<" + type + "\"" + getText() + "\"" + ">").replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t");
	}

	public static enum TokenType {
		NUMBER,
		SPACE,
		EOF,
		PLUSMINUS,
		MULDIV,
		RAISED,
		OPEN_PARENS,
		CLOSE_PARENS,
		IDENTIFIER,
		SEMI,
		EQUAL,
		COMMA;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (text == null ? 0 : text.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Token other = (Token) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
