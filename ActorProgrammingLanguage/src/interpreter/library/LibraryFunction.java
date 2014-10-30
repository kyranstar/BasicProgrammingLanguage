package interpreter.library;

import java.util.Arrays;

import machine.Context;
import machine.Function;
import parser.ExpressionNode;

public class LibraryFunction {
	public static Context applyLibraryFunctions(final Context c) {
		c.putFunction(
				"print",
				new Function("print", Arrays
						.asList(new ExpressionNode.VariableNode("a")),
						new ExpressionNode.PrintNode(
								new ExpressionNode.VariableNode("a"))));

		return c;
	}
}
