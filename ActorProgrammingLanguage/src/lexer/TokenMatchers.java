/*
 * @author Kyran Adams
 */
package lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lexer.Token.TokenType;

/**
 * The Enumeration TokenMatchers.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public enum TokenMatchers {

    /**
     * Matches whitespace characters.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    SPACE {
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final PositionInfo lexInfo) {
            final StringBuilder spaces = new StringBuilder();
            for (String sub = code; matches(sub, lexInfo); sub = sub
                    .substring(1)) {
                final char firstCharacter = sub.charAt(0);
                spaces.append(firstCharacter);
            }
            return new Token(TokenType.SPACE, spaces.toString(), lexInfo);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            return Character.isWhitespace(code.charAt(0));
        }
        
    },
    
    /** Matches a line comment */
    LINE_COMMENT {
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final PositionInfo lexInfo) {
            final StringBuilder letters = new StringBuilder();
            for (String sub = code; !sub.startsWith("\n") && sub.length() > 0; sub = sub
                    .substring(1)) {
                final char c = sub.charAt(0);
                letters.append(c);
            }
            return new Token(TokenType.COMMENT, letters.toString(), lexInfo);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            return code.startsWith("//");
        }
        
    },

    /** Matches a multi-line comment. */
    MULTILINE_COMMENT {
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final PositionInfo lexInfo) {
            final StringBuilder letters = new StringBuilder();
            for (String sub = code; sub.length() > 0; sub = sub.substring(1)) {
                if (sub.startsWith("*/")) {
                    letters.append("*/");
                    break;
                }
                final char c = sub.charAt(0);
                letters.append(c);

            }
            return new Token(TokenType.COMMENT, letters.toString(), lexInfo);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            return code.startsWith("/*");
        }
        
    },
    
    /**
     * Matches a number literal.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    NUMBER {
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(String code, final PositionInfo lexInfo) {
            final StringBuilder number = new StringBuilder();
            boolean foundDec = false;
            do {
                // Only one period in the number
                if (code.charAt(0) == '.') {
                    foundDec = true;
                }
                number.append(code.charAt(0));
                code = code.substring(1);
            } while (code.length() > 0
                    && (Character.isDigit(code.charAt(0)) || !foundDec
                            && code.charAt(0) == '.'));
            return new Token(TokenType.NUMBER, number.toString(), lexInfo);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            return Character.isDigit(code.charAt(0)) || code.charAt(0) == '.'
                    && Character.isDigit(code.charAt(1));
        }
        
    },
    
    /**
     * Matches a string literal.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    STRING {
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(String code, final PositionInfo lexInfo) {
            
            final StringBuilder string = new StringBuilder();
            string.append('"');
            code = code.substring(1);
            while (code.length() > 0 && code.charAt(0) != '"') {
                string.append(code.charAt(0));
                code = code.substring(1);
            }
            string.append('"');
            return new Token(TokenType.STRING, string.toString(), lexInfo);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            return code.charAt(0) == '"';
        }
        
    },

    /**
     * Matches a character literal.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    CHAR {
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(String code, final PositionInfo lexInfo) {
            final StringBuilder string = new StringBuilder();
            string.append('\'');
            code = code.substring(1);
            do {
                string.append(code.charAt(0));
                code = code.substring(1);
            } while (code.length() > 0 && code.charAt(0) != '\'');
            string.append('\'');
            return new Token(TokenType.CHAR, string.toString(), lexInfo);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            return code.charAt(0) == '\'';
        }
        
    },

    /**
     * Matches a boolean literal.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    BOOLEAN {
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final PositionInfo lexInfo) {
            if (code.startsWith("true")) {
                return new Token(TokenType.BOOLEAN, "true", lexInfo);
            }
            if (code.startsWith("false")) {
                return new Token(TokenType.BOOLEAN, "false", lexInfo);
            }
            throw new LexerException("Tried to get token " + getClass()
                    + ", but did not match. Was {" + code.charAt(0)
                    + "} instead. Should never get here! Compiler bug.");
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            return code.startsWith("true") || code.startsWith("false");
        }
        
    },
    
    /**
     * Matches an operator.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    OPERATOR {
        
        /** The Constant OPERATORS. */
        @SuppressWarnings("serial")
        private final List<StringToToken> OPERATORS = Collections
        .unmodifiableList(new ArrayList<StringToToken>() {
            {
                add(new StringToToken(";", TokenType.SEMI));
                add(new StringToToken(",", TokenType.COMMA));
                add(new StringToToken("->", TokenType.ARROW_RIGHT));
                add(new StringToToken("=", TokenType.EQUAL));
                add(new StringToToken("*", TokenType.MULTIPLY));
                add(new StringToToken("/", TokenType.DIVIDE));
                add(new StringToToken("%", TokenType.MOD));
                add(new StringToToken("+", TokenType.PLUS));
                add(new StringToToken("-", TokenType.MINUS));
                add(new StringToToken("^", TokenType.RAISED));
                add(new StringToToken("<=", TokenType.LESS_THAN_EQUAL));
                add(new StringToToken(">=",
                                TokenType.GREATER_THAN_EQUAL));
                add(new StringToToken("<", TokenType.LESS_THAN));
                add(new StringToToken(">", TokenType.GREATER_THAN));
                add(new StringToToken("&&", TokenType.AND));
                add(new StringToToken("||", TokenType.OR));
                add(new StringToToken(".", TokenType.DOT));
            }
        });
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final PositionInfo lexInfo) {
            for (final StringToToken entry : OPERATORS) {
                if (code.startsWith(entry.text)) {
                    return new Token(entry.type, entry.text, lexInfo);
                }
            }
            
            throw new LexerException("Unidentified token (" + code.charAt(0)
                    + ")");
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            for (final StringToToken s : OPERATORS) {
                if (code.startsWith(s.text)) {
                    return true;
                }
            }
            return false;
        }
    },

    /**
     * Matches keywords in the language.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    KEYWORDS {
        
        /** The Constant OPERATORS. */
        @SuppressWarnings("serial")
        private final List<StringToToken> KEYWORDS = Collections
        .unmodifiableList(new ArrayList<StringToToken>() {
            {
                add(new StringToToken("to", TokenType.TO));
                add(new StringToToken("lambda", TokenType.LAMBDA));
                add(new StringToToken("if", TokenType.IF));
                add(new StringToToken("mut", TokenType.MUTABLE));
                add(new StringToToken("then", TokenType.THEN));
                add(new StringToToken("else", TokenType.ELSE));
                add(new StringToToken("new", TokenType.NEW));
                add(new StringToToken("datatype", TokenType.DATA_TYPE));
            }
        });
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final PositionInfo lexInfo) {
            for (final StringToToken entry : KEYWORDS) {
                if (code.startsWith(entry.text)) {
                    return new Token(entry.type, entry.text, lexInfo);
                }
            }
            
            throw new LexerException("Unidentified token (" + code.charAt(0)
                    + ")");
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            for (final StringToToken s : KEYWORDS) {
                if (code.startsWith(s.text)) {
                    return true;
                }
            }
            return false;
        }
    },
    
    /**
     * Matches open and close parens, square brackets, and curly brackets.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    BRACKETS {
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final PositionInfo lexInfo) {
            final char c = code.charAt(0);
            final String token = String.valueOf(c);
            switch (code.charAt(0)) {
                case '(':
                    return new Token(TokenType.OPEN_PARENS, token, lexInfo);
                case ')':
                    return new Token(TokenType.CLOSE_PARENS, token, lexInfo);
                case '[':
                    return new Token(TokenType.OPEN_SQUARE_BRACKET, token,
                            lexInfo);
                case ']':
                    return new Token(TokenType.CLOSE_SQUARE_BRACKET, token,
                            lexInfo);
                case '{':
                    return new Token(TokenType.OPEN_CURLY_BRACKET, token,
                            lexInfo);
                case '}':
                    return new Token(TokenType.CLOSE_CURLY_BRACKET, token,
                            lexInfo);
            }
            throw new LexerException("Unidentified token: " + token);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            return code.charAt(0) == '(' || code.charAt(0) == ')'
                    || code.charAt(0) == '[' || code.charAt(0) == ']'
                    || code.charAt(0) == '{' || code.charAt(0) == '}';
        }
    },
    
    /**
     * Matches an identifier, like a variable name.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    IDENTIFIER {
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(String code, final PositionInfo lexInfo) {
            final StringBuilder identifier = new StringBuilder();
            do {
                identifier.append(code.charAt(0));
                code = code.substring(1);
            } while (matches(code, lexInfo));
            return new Token(TokenType.IDENTIFIER, identifier.toString(),
                    lexInfo);
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final PositionInfo lexInfo) {
            return Character.isAlphabetic(code.charAt(0))
                    || code.charAt(0) == '_';
        }
        
    };
    /**
     * Gets the token without checking for errors.
     *
     * @param code
     *            the code
     * @param lexInfo
     *            the lex info
     *
     * @return the token no check
     */
    protected abstract Token getTokenNoCheck(final String code,
            final PositionInfo lexInfo);
    
    /**
     * Matches without checking for errors.
     *
     * @param code
     *            the code
     * @param lexInfo
     *            the lex info
     *
     * @return true, if it matches
     */
    protected abstract boolean matchesNoCheck(final String code,
            final PositionInfo lexInfo);
    
    /**
     * Gets the token.
     *
     * @param code
     *            the code
     * @param lexInfo
     *            the lex info
     *
     * @return the token
     */
    public final Token getToken(final String code, final PositionInfo lexInfo) {
        if (code.length() <= 0) {
            throw new LexerException("Code length was 0.");
        }
        
        if (!matches(code, lexInfo)) {
            throw new LexerException("Tried to get token " + getClass()
                    + ", but did not match. Was {" + code.charAt(0)
                    + "} instead.");
        }
        return getTokenNoCheck(code, lexInfo);
    }
    
    /**
     * Matches.
     *
     * @param code
     *            the code
     * @param lexInfo
     *            the lex info
     *
     * @return true, if successful
     */
    public final boolean matches(final String code, final PositionInfo lexInfo) {
        try {
            return matchesNoCheck(code, lexInfo);
        } catch (final StringIndexOutOfBoundsException e) {
            // We are at the EOF
            return false;
        }
    }
    
    /**
     * Helper class that represents string to token conversion
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    class StringToToken {

        /** The text. */
        public String text;
        
        /** The type. */
        public TokenType type;

        /**
         * Instantiates a new string to operator.
         *
         * @param string
         *            the string
         * @param type
         *            the type
         */
        public StringToToken(final String string, final TokenType type) {
            text = string;
            this.type = type;
        }
        
    }
}
