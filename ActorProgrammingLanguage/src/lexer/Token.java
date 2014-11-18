/*
 * @author Kyran Adams
 */
package lexer;

/**
 * The Class Token. Represents a single token.
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
    /** The info. */
    private final PositionInfo info;
    
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
            final PositionInfo currentInfo) {
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
     * Gets the lex info.
     *
     * @return the lex info
     */
    public PositionInfo getLexInfo() {
        return info;
    }
    
    /**
     * The Enum TokenType.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static enum TokenType {
        
        /** The number token. */
        NUMBER,
        /** The whitespace token. */
        SPACE,
        /** The end of file marker token. */
        EOF,
        /** The addition operator and unary positive operator +. */
        PLUS,
        /** The subtraction operator and unary negative operator -. */
        MINUS,
        /** The multiplication operator *. */
        MULTIPLY,
        /** The division operator /. */
        DIVIDE,
        /** The modulo operator %. */
        MOD,
        /** The raiser operator ^. */
        RAISED,
        /** The open parenthesis. */
        OPEN_PARENS,
        /** The close parenthesis. */
        CLOSE_PARENS,
        /** The identifier value. */
        IDENTIFIER,
        /** The semi colon. */
        SEMI,
        /** The equal sign. */
        EQUAL,
        /** The comma symbol. */
        COMMA,
        /** The and operator &&. */
        AND,
        /** The or operator ||. */
        OR,
        /** The boolean value, true or false. */
        BOOLEAN,
        /** The if token. */
        IF,
        /** The else token. */
        ELSE,
        /** The then token. */
        THEN,
        /** The less than operator <. */
        LESS_THAN,
        /** The greater than operator >. */
        GREATER_THAN,
        /** The less than equal operator <=. */
        LESS_THAN_EQUAL,
        /** The greater than equal operator >=. */
        GREATER_THAN_EQUAL,
        /** The open square bracket [. */
        OPEN_SQUARE_BRACKET,
        /** The close square bracket ]. */
        CLOSE_SQUARE_BRACKET,
        /** An open curly bracket {. */
        OPEN_CURLY_BRACKET,
        /** A close curly bracket }. */
        CLOSE_CURLY_BRACKET,
        /** A comment token. */
        COMMENT,
        /** A string literal. */
        STRING,
        /** A character literal. */
        CHAR,
        /** The arrow token ->. */
        ARROW_RIGHT,
        /** The lambda token. */
        LAMBDA,
        /** The to token. Used in ranges. */
        TO,
        NEW,
        DATA_TYPE,
        DOT,
        MUTABLE;
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
