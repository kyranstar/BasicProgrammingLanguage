/*
 * @author Kyran Adams
 */
package lexer;

import java.util.ArrayList;
import java.util.List;

import lexer.Token.TokenType;

/**
 * The Class TokenMatchers.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class TokenMatchers {
    
    /**
     * Instantiates a new token matchers.
     */
    private TokenMatchers() {
    }
    
    /**
     * The Class SPACE.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class SPACE extends TokenMatcher {
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final LexerInformation lexInfo) {
            final StringBuilder spaces = new StringBuilder();
            for (String sub = code; matches(sub, lexInfo); sub = sub
                    .substring(1)) {
                final char c = sub.charAt(0);
                spaces.append(c);
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
                final LexerInformation lexInfo) {
            return Character.isWhitespace(code.charAt(0));
        }
        
    }
    
    /**
     */
    public static class LINE_COMMENT extends TokenMatcher {
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final LexerInformation lexInfo) {
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
                final LexerInformation lexInfo) {
            return code.startsWith("//");
        }
        
    }

    /**
     */
    public static class MULTILINE_COMMENT extends TokenMatcher {
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final LexerInformation lexInfo) {
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
                final LexerInformation lexInfo) {
            return code.startsWith("/*");
        }
        
    }
    
    /**
     * The Class NUMBER.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class NUMBER extends TokenMatcher {
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(String code, final LexerInformation lexInfo) {
            final StringBuilder number = new StringBuilder();
            do {
                number.append(code.charAt(0));
                code = code.substring(1);
            } while (matches(code, lexInfo));
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
                final LexerInformation lexInfo) {
            return Character.isDigit(code.charAt(0)) || code.charAt(0) == '.';
        }
        
    };
    
    /**
     * The Class STRING.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class STRING extends TokenMatcher {
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(String code, final LexerInformation lexInfo) {
            final StringBuilder string = new StringBuilder();
            string.append('"');
            code = code.substring(1);
            do {
                string.append(code.charAt(0));
                code = code.substring(1);
            } while (code.length() > 0 && code.charAt(0) != '"');
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
                final LexerInformation lexInfo) {
            return code.charAt(0) == '"';
        }
        
    };

    /**
     * The Class STRING.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class CHAR extends TokenMatcher {
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(String code, final LexerInformation lexInfo) {
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
                final LexerInformation lexInfo) {
            return code.charAt(0) == '\'';
        }
        
    };

    /**
     * The Class BOOLEAN.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class BOOLEAN extends TokenMatcher {
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final LexerInformation lexInfo) {
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
                final LexerInformation lexInfo) {
            return code.startsWith("true") || code.startsWith("false");
        }
        
    };
    
    /**
     * The Class OPERATOR.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class OPERATOR extends TokenMatcher {
        
        /** The Constant OPERATORS. */
        private static final List<StringToOperator> OPERATORS = new ArrayList<>();
        static {
            OPERATORS.add(new StringToOperator(";", TokenType.SEMI));
            OPERATORS.add(new StringToOperator(",", TokenType.COMMA));
            OPERATORS.add(new StringToOperator("=", TokenType.EQUAL));
            
            OPERATORS.add(new StringToOperator("*", TokenType.MULDIV));
            OPERATORS.add(new StringToOperator("/", TokenType.MULDIV));
            OPERATORS.add(new StringToOperator("+", TokenType.PLUSMINUS));
            OPERATORS.add(new StringToOperator("-", TokenType.PLUSMINUS));
            OPERATORS.add(new StringToOperator("^", TokenType.RAISED));
            
            OPERATORS
                    .add(new StringToOperator("<=", TokenType.LESS_THAN_EQUAL));
            OPERATORS.add(new StringToOperator(">=",
                    TokenType.GREATER_THAN_EQUAL));
            
            OPERATORS.add(new StringToOperator("<", TokenType.LESS_THAN));
            OPERATORS.add(new StringToOperator(">", TokenType.GREATER_THAN));
            
            OPERATORS.add(new StringToOperator("&&", TokenType.AND));
            OPERATORS.add(new StringToOperator("||", TokenType.OR));
        }
        
        /**
         * The Class StringToOperator.
         *
         * @author Kyran Adams
         * @version $Revision: 1.0 $
         */
        private static class StringToOperator {
            
            /**
             * Instantiates a new string to operator.
             *
             * @param string
             *            the string
             * @param type
             *            the type
             */
            public StringToOperator(final String string, final TokenType type) {
                text = string;
                this.type = type;
            }
            
            /** The text. */
            public String text;
            
            /** The type. */
            public TokenType type;
        }
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final LexerInformation lexInfo) {
            for (final StringToOperator entry : OPERATORS) {
                if (code.startsWith(entry.text)) {
                    return new Token(entry.type, entry.text, lexInfo);
                }
            }
            
            throw new LexerException("Unidentified token");
        }
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public boolean matchesNoCheck(final String code,
                final LexerInformation lexInfo) {
            for (final StringToOperator s : OPERATORS) {
                if (code.startsWith(s.text)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    /**
     * The Class IF.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class IF extends TokenMatcher {
        
        /** The if string. */
        final String ifString = "if";
        
        /** The else string. */
        final String elseString = "else";
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        protected Token getTokenNoCheck(final String code,
                final LexerInformation lexInfo) {
            
            if (code.startsWith(ifString)) {
                return new Token(TokenType.IF, ifString, lexInfo);
            } else {
                return new Token(TokenType.ELSE, elseString, lexInfo);
            }
        }
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#matchesNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        protected boolean matchesNoCheck(final String code,
                final LexerInformation lexInfo) {
            return code.startsWith(ifString) || code.startsWith(elseString);
        }
        
    }
    
    /**
     * The Class BRACKETS.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class BRACKETS extends TokenMatcher {
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(final String code,
                final LexerInformation lexInfo) {
            final char c = code.charAt(0);
            final String token = String.valueOf(c);
            switch (code.charAt(0)) {
                case '(':
                    return new Token(TokenType.OPEN_PARENS, token, lexInfo);
                case ')':
                    return new Token(TokenType.CLOSE_PARENS, token, lexInfo);
                case '[':
                    return new Token(TokenType.OPEN_BRACKET, token, lexInfo);
                case ']':
                    return new Token(TokenType.CLOSE_BRACKET, token, lexInfo);
                case '{':
                    return new Token(TokenType.OPEN_CURLY, token, lexInfo);
                case '}':
                    return new Token(TokenType.CLOSE_CURLY, token, lexInfo);
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
                final LexerInformation lexInfo) {
            return code.startsWith("(") || code.startsWith(")")
                    || code.startsWith("[") || code.startsWith("]")
                    || code.startsWith("{") || code.startsWith("}");
        }
    }
    
    /**
     * The Class IDENTIFIER.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class IDENTIFIER extends TokenMatcher {
        
        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        public Token getTokenNoCheck(String code, final LexerInformation lexInfo) {
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
                final LexerInformation lexInfo) {
            return Character.isAlphabetic(code.charAt(0))
                    || code.charAt(0) == '_';
        }
        
    };
}
