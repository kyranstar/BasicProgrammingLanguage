/*
 * @author Kyran Adams
 */
package lexer;

// TODO: Auto-generated Javadoc
/**
 * The Class Token.
 * 
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Token {

    /** The text. */
    private final String text;

    /** The type. */
    private final TokenType type;
    
    /* Holds information about this token */
    private final LexerInformation info;

    /**
     * Instantiates a new token.
     *
     * @param type
     *            the type
     * @param text
     *            the text
     * @param currentInfo
     *            LexerInformation
     */
    public Token(final TokenType type, final String text,
            final LexerInformation currentInfo) {
        this.type = type;
        this.text = text;
        info = currentInfo;
    }

    /**
     * Gets the text.
     *
     * 
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the type.
     *
     * 
     * @return the type
     */
    public TokenType getType() {
        return type;
    }
    
    /**
     * Gets the token informations message.
     *
     * 
     * @return the type
     */
    public String getMessage() {
        return info.getMessage();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ("<" + type + "\"" + getText() + "\"" + ">").replaceAll("\n",
                "\\\\n").replaceAll("\t", "\\\\t");
    }

    /**
     * The Enum TokenType.
     * 
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static enum TokenType {

        /** The number. */
        NUMBER,
        /** The space. */
        SPACE,
        /** The eof. */
        EOF,
        /** The plusminus. */
        PLUSMINUS,
        /** The muldiv. */
        MULDIV,
        /** The raised. */
        RAISED,
        /** The open parens. */
        OPEN_PARENS,
        /** The close parens. */
        CLOSE_PARENS,
        /** The identifier. */
        IDENTIFIER,
        /** The semi. */
        SEMI,
        /** The equal. */
        EQUAL,
        /** The comma. */
        COMMA,
        /** The and. */
        AND,
        /** The or. */
        OR,
        /** The boolean. */
        BOOLEAN,
        /** The if. */
        IF,
        /** The else. */
        ELSE,
        /** The less than. */
        LESS_THAN,
        /** The greater than. */
        GREATER_THAN,
        /** The less than equal. */
        LESS_THAN_EQUAL,
        /** The greater than equal. */
        GREATER_THAN_EQUAL,

        /** The open bracket. */
        OPEN_BRACKET,

        /** The close bracket. */
        CLOSE_BRACKET,
        OPEN_CURLY,
        CLOSE_CURLY,
        COMMENT,
        STRING;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (text == null ? 0 : text.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Token other = (Token) obj;
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

}
