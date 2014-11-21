/*
 * @author Kyran Adams
 */
package main;

import interpreter.Interpreter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The Class Main.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public final class Main {

    /**
     * Unused private constructor.
     */
    private Main() {
    }

    /**
     * The main method. Callable from command line.
     *
     * @param args
     *            The arguments. arg[0] = the code to run.
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void main(final String[] args) throws IOException {
        final String code = readFile("./test/examples/code.txt",
                Charset.defaultCharset());

        final Interpreter interpreter = new Interpreter(System.out);
        interpreter.interpret(code);
    }
    
    /**
     * Read file.
     *
     * @param path
     *            the path
     * @param encoding
     *            the encoding
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    static String readFile(final String path, final Charset encoding)
            throws IOException {
        final byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
