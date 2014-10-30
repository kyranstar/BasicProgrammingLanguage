package interpreter.library;

import java.util.Arrays;

import machine.Context;
import machine.Function;
import parser.ExpressionNode;

public class LibraryFunction {
	public static Context applyLibraryFunctions(final Context c) {

		c.putFunction("println", new Function("println", Arrays.asList(new ExpressionNode.VariableNode("a")), new ExpressionNode.PrintlnNode(new ExpressionNode.VariableNode("a"))));
		
		return c;
	}
}
