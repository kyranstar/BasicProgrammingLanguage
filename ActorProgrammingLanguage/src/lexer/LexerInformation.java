/*
 * @author Kyran Adams
 */
package lexer;

// TODO: Auto-generated Javadoc
/**
 * The Class LexerInformation.
 */
public class LexerInformation {
	// Current line number we are on
	/** The current line. */
	public int currentLine = 0;
	// The index from beginning of file of the last end line
	/** The last end line. */
	public int lastEndLine = 0;
	// Total index from beginning of file
	/** The position. */
	public int position = 0;

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return " (Line: " + currentLine + " Position: "
				+ (position - lastEndLine) + ")";
	}
}
