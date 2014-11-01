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

// TODO: Auto-generated Javadoc
/**
 * The Class Interpreter.
 */
public class Interpreter {
    
    /** The print stream. */
    private final PrintStream printStream;
    
    /**
     * Instantiates a new interpreter.
     *
     * @param printStream the print stream
     */
    public Interpreter(final PrintStream printStream) {
        this.printStream = printStream;
    }

    /**
     * Interpret.
     *
     * @param s the s
     * @return the context
     */
    public Context interpret(final String s) {
        final Context context = new Context(printStream);
        final Lexer lexer = new Lexer(s);
        final List<ExpressionNode> nodes = new Parser(lexer.lex())
                .parse(context);
        for (final ExpressionNode node : nodes) {
            node.getValue(context);
        }
        return context;
    }
}
