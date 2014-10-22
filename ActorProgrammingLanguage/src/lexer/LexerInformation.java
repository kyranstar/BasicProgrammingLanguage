package lexer;

public class LexerInformation {
	// Current line number we are on
	public int currentLine = 0;
	// The index from beginning of file of the last end line
	public int lastEndLine = 0;
	// Total index from beginning of file
	public int position = 0;

	public String getMessage() {
		return " (Line: " + currentLine + " Position: "
				+ (position - lastEndLine) + ")";
	}
}
