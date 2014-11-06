/*
 * @author Kyran Adams
 */
package parser.checking;

/**
 * The Class ParserException.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class CompilerException extends Exception {

    /**
     * Instantiates a new parser exception.
     *
     * @param string
     *            the string
     */
    public CompilerException(final String string) {
        super(string);
    }

    /**
     * Instantiates a new parser exception.
     */
    public CompilerException() {
        super();
    }

    /**
     * Instantiates a new parser exception.
     *
     * @param arg0
     *            the arg0
     * @param arg1
     *            the arg1
     * @param arg2
     *            the arg2
     * @param arg3
     *            the arg3
     */
    public CompilerException(final String arg0, final Throwable arg1,
            final boolean arg2, final boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    /**
     * Instantiates a new parser exception.
     *
     * @param arg0
     *            the arg0
     * @param arg1
     *            the arg1
     */
    public CompilerException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new parser exception.
     *
     * @param arg0
     *            the arg0
     */
    public CompilerException(final Throwable arg0) {
        super(arg0);
    }

}
