/*
 * @author Kyran Adams
 */
package interpreter;

import java.io.PrintStream;
import java.util.List;

import lexer.Lexer;
import machine.Context;
import parser.ExpressionNode;
import parser.Parser;

/**
 * The Class Interpreter.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Interpreter {

    /** The print stream. */
    private final PrintStream printStream;

    /**
     * Instantiates a new interpreter.
     *
     * @param printStream
     *            the print stream
     */
    public Interpreter(final PrintStream printStream) {
        this.printStream = printStream;
    }
    
    /**
     * Interprets the passed in code.
     *
     * @param code
     *            the code
     *
     * @return the context
     */
    public Context interpret(final String code) {
        final Context context = new Context(printStream);
        final Lexer lexer = new Lexer(code);
        final List<ExpressionNode> nodes = new Parser(lexer.lex())
        .parse(context);
        for (final ExpressionNode node : nodes) {
            System.out.println(node + ";");
        }

        for (final ExpressionNode node : nodes) {
            node.getValue(context);
        }
        return context;
    }
}
