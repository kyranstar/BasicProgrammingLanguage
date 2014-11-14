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
 * The Class Lexer. Takes a string and returns a list of tokens.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Lexer {
    
    /** The lex info. */
    private final PositionInfo lexInfo = new PositionInfo();
    
    /** The code. */
    private final String code;
    
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
     * Match a single token.
     *
     *
     * @return the token
     */
    private Token matchToken() {
        final String codeFromPosition = code.substring(lexInfo.position);
        
        final List<Token> potentialMatches = new ArrayList<>();
        
        for (final TokenMatchers m : TokenMatchers.values()) {
            if (m.matches(codeFromPosition, lexInfo)) {
                final Token t = m.getToken(codeFromPosition, lexInfo.copy());
                potentialMatches.add(t);
            }
        }
        // If there are no potential matches
        if (potentialMatches.isEmpty()) {
            throw new LexerException("Could not match character '"
                    + code.charAt(lexInfo.position) + "' with token");
        }
        // If there are matches, match the longest
        Token longest = null;
        for (final Token t : potentialMatches) {
            if (longest == null
                    || t.getText().length() > longest.getText().length()) {
                longest = t;
            }
        }

        // if there is a newline in the text
        final String tokenText = longest.getText();
        updateLexInfoPosition(tokenText);
        return longest;
    }
    
    /**
     * This method updates the currentLine, lastEndLine, and position of the
     * lexInfo. This should be called whenever a string is lexed with the string
     * as the param.
     *
     * @param tokenText
     *            the token text
     */
    private void updateLexInfoPosition(final String tokenText) {
        // if the text contains a new line
        if (tokenText.indexOf('\n') != -1) {
            // add number of \n in text to the currentLine
            lexInfo.currentLine += tokenText.length()
                    - tokenText.replace("\n", "").length();
            lexInfo.lastEndLine = lexInfo.position
                    + tokenText.lastIndexOf('\n');
        }
        lexInfo.position += tokenText.length();
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
                final Token nextToken = matchToken();
                if (!typesToIgnore.contains(nextToken.getType())) {
                    tokens.add(nextToken);
                }
            }
            
            return tokens;
        } catch (final LexerException e) {
            throw new LexerException(lexInfo.getMessage(), e);
        }
    }
}
