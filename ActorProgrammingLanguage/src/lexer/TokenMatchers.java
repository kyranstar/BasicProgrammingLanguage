/*
 * @author Kyran Adams
 */
package lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lexer.Token.TokenType;

/**
 * The Class TokenMatchers.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public enum TokenMatchers {
    
    /**
     * The Class SPACE.
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
                final LexerInformation lexInfo) {
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
                final LexerInformation lexInfo) {
            return Character.isWhitespace(code.charAt(0));
        }

    },

    /**
     */
    LINE_COMMENT {

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
            for (String sub = code; !(sub.charAt(0) == '\n')
                    && sub.length() > 0; sub = sub.substring(1)) {
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

    },
    
    /**
     */
    MULTILINE_COMMENT {

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

    },

    /**
     * The Class NUMBER.
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
        public Token getTokenNoCheck(String code, final LexerInformation lexInfo) {
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
                final LexerInformation lexInfo) {
            return Character.isDigit(code.charAt(0)) || code.charAt(0) == '.';
        }

    },

    /**
     * The Class STRING.
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
        public Token getTokenNoCheck(String code, final LexerInformation lexInfo) {

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
                final LexerInformation lexInfo) {
            return code.charAt(0) == '"';
        }

    },
    
    /**
     * The Class STRING.
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

    },
    
    /**
     * The Class BOOLEAN.
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

    },

    /**
     * The Class OPERATOR.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    OPERATOR {

        /** The Constant OPERATORS. */
        private final List<StringToOperator> OPERATORS = Collections
                .unmodifiableList(new ArrayList<StringToOperator>() {
                    {
                        add(new StringToOperator(";", TokenType.SEMI));
                        add(new StringToOperator(",", TokenType.COMMA));
                        add(new StringToOperator("->", TokenType.ARROW_RIGHT));
                        add(new StringToOperator("=", TokenType.EQUAL));

                        add(new StringToOperator("*", TokenType.MULTIPLY));
                        add(new StringToOperator("/", TokenType.DIVIDE));
                        add(new StringToOperator("%", TokenType.MOD));
                        add(new StringToOperator("+", TokenType.PLUS));
                        add(new StringToOperator("-", TokenType.MINUS));
                        add(new StringToOperator("^", TokenType.RAISED));

                        add(new StringToOperator("<=",
                                TokenType.LESS_THAN_EQUAL));
                        add(new StringToOperator(">=",
                                TokenType.GREATER_THAN_EQUAL));

                        add(new StringToOperator("<", TokenType.LESS_THAN));
                        add(new StringToOperator(">", TokenType.GREATER_THAN));

                        add(new StringToOperator("&&", TokenType.AND));
                        add(new StringToOperator("||", TokenType.OR));
                    }
                });

        /**
         * The Class StringToOperator.
         *
         * @author Kyran Adams
         * @version $Revision: 1.0 $
         */
        class StringToOperator {

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
            public StringToOperator(final String string, final TokenType type) {
                text = string;
                this.type = type;
            }
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
                final LexerInformation lexInfo) {
            for (final StringToOperator s : OPERATORS) {
                if (code.startsWith(s.text)) {
                    return true;
                }
            }
            return false;
        }
    },
    
    /**
     * The Class OPERATOR.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    KEYWORDS {

        /** The Constant OPERATORS. */
        private final List<StringToKeywords> KEYWORDS = Collections
                .unmodifiableList(new ArrayList<StringToKeywords>() {
                    {
                        add(new StringToKeywords("to", TokenType.TO));
                        add(new StringToKeywords("lambda", TokenType.LAMBDA));
                    }
                });

        /**
         * The Class StringToOperator.
         *
         * @author Kyran Adams
         * @version $Revision: 1.0 $
         */
        class StringToKeywords {
            
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
            public StringToKeywords(final String string, final TokenType type) {
                text = string;
                this.type = type;
            }

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
            for (final StringToKeywords entry : KEYWORDS) {
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
                final LexerInformation lexInfo) {
            for (final StringToKeywords s : KEYWORDS) {
                if (code.startsWith(s.text)) {
                    return true;
                }
            }
            return false;
        }
    },

    /**
     * The Class IF.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    IF {

        /** The if string. */
        public final static String IF_STRING = "if";

        /** The else string. */
        public final static String ELSE_STRING = "else";
        
        /** The then string. */
        public final static String THEN_STRING = "then";

        /*
         * (non-Javadoc)
         *
         * @see lexer.TokenMatcher#getTokenNoCheck(java.lang.String,
         * lexer.LexerInformation)
         */
        @Override
        protected Token getTokenNoCheck(final String code,
                final LexerInformation lexInfo) {

            if (code.startsWith(IF_STRING)) {
                return new Token(TokenType.IF, IF_STRING, lexInfo);
            } else if (code.startsWith(THEN_STRING)) {
                return new Token(TokenType.THEN, THEN_STRING, lexInfo);
            } else {
                return new Token(TokenType.ELSE, ELSE_STRING, lexInfo);
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
            return code.startsWith(IF_STRING) || code.startsWith(ELSE_STRING)
                    || code.startsWith(THEN_STRING);
        }

    },

    /**
     * The Class BRACKETS.
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
                final LexerInformation lexInfo) {
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
                final LexerInformation lexInfo) {
            return code.charAt(0) == '(' || code.charAt(0) == ')'
                    || code.charAt(0) == '[' || code.charAt(0) == ']'
                    || code.charAt(0) == '{' || code.charAt(0) == '}';
        }
    },

    /**
     * The Class IDENTIFIER.
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
            final LexerInformation lexInfo);

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
            final LexerInformation lexInfo);

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
    public final Token getToken(final String code,
            final LexerInformation lexInfo) {
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
    public final boolean matches(final String code,
            final LexerInformation lexInfo) {
        try {
            return matchesNoCheck(code, lexInfo);
        } catch (final StringIndexOutOfBoundsException e) {
            // We are at the EOF
            return false;
        }
    }
}
