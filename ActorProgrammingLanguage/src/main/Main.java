/*
 * @author Kyran Adams
 */
package main;

import interpreter.Interpreter;

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
     */
    public static void main(final String[] args) {
        new Interpreter(System.out).interpret(args[0]);
    }
}
