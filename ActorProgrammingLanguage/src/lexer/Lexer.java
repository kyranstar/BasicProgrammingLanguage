/*
 * @author Kyran Adams
 */
package lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lexer.Token.TokenType;

// TODO: Auto-generated Javadoc
/**
 * The Class Lexer.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Lexer {

    /** The lex info. */
    private final LexerInformation lexInfo = new LexerInformation();

    /** The code. */
    private final String code;
    // Token matchers, ordered by length to avoid hitting shorter ones first.
    // ("len" before "length")
    /** The matchers. */
    private final TokenMatcher[] matchers = {
            new TokenMatchers.MULTILINE_COMMENT(),
            new TokenMatchers.LINE_COMMENT(), new TokenMatchers.STRING(),
            new TokenMatchers.SPACE(), new TokenMatchers.NUMBER(),
            new TokenMatchers.BOOLEAN(), new TokenMatchers.OPERATOR(),
            new TokenMatchers.BRACKETS(), new TokenMatchers.IF(),
            new TokenMatchers.IDENTIFIER() };

    /** The types to ignore when passing to parser. */
    private final List<TokenType> typesToIgnore = Arrays.asList(
            TokenType.COMMENT, TokenType.SPACE, TokenType.EOF);

    /**
     * Instantiates a new lexer.
     *
     * @param code
     *            the code
     */
    public Lexer(final String code) {
        this.code = code;
    }

    /**
     * Match token.
     *
     *
     * @return the token
     */
    private Token matchToken() {
        final String codeFromPosition = code.substring(lexInfo.position);

        for (final TokenMatcher m : matchers) {
            if (m.matches(codeFromPosition, lexInfo)) {
                final Token t = m.getToken(codeFromPosition, lexInfo.copy());
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
                + code.charAt(lexInfo.position) + "' with token");
    }

    /**
     * Lexes the code into tokens.
     *
     *
     * @return the list
     */
    public List<Token> lex() {
        try {
            final List<Token> tokens = new ArrayList<>();

            while (lexInfo.position < code.length()) {
                final Token t = matchToken();
                if (!typesToIgnore.contains(t.getType())) {
                    tokens.add(t);
                }
            }

            return tokens;
        } catch (final LexerException e) {
            throw new LexerException(e.getMessage() + lexInfo.getMessage());
        }
    }
}
