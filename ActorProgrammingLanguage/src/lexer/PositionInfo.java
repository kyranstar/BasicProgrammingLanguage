/*
 * @author Kyran Adams
 */
package lexer;

/**
 * The Class PositionInfo. Holds information about the current position in the
 * code.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class PositionInfo {
    
    /** Current line number we are on. */
    public int currentLine = 0;
    
    /** The index from beginning of file of the last end line. */
    public int lastEndLine = 0;
    /** Total index from beginning of file. */
    public int position = 0;
    
    /**
     * Gets the message.
     *
     *
     * @return the message
     */
    public String getMessage() {
        return " (Line: " + currentLine + " Position: "
                + (position - lastEndLine) + ")";
    }
    
    /**
     * Method copy.
     *
     * @return LexerInformation
     */
    public PositionInfo copy() {
        final PositionInfo copy = new PositionInfo();
        copy.currentLine = currentLine;
        copy.lastEndLine = lastEndLine;
        copy.position = position;
        return copy;
    }
}
