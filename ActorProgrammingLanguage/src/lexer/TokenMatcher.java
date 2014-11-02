/*
 * @author Kyran Adams
 */
package lexer;

// TODO: Auto-generated Javadoc
/**
 * The Class TokenMatcher.
 */
public abstract class TokenMatcher {
    
    /**
     * Gets the token without checking for errors.
     *
     * @param code
     *            the code
     * @param lexInfo
     *            the lex info
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
     * @return the token
     */
    public final Token getToken(final String code,
            final LexerInformation lexInfo) {
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
