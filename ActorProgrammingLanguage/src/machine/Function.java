package machine;

import java.util.List;

import parser.ExpressionNode;

public class Function {
	public final ExpressionNode body;
	public final List<ExpressionNode.VariableNode> parameters;
	public final String name;

	public Function(final String name2,
			final List<ExpressionNode.VariableNode> parameters,
			final ExpressionNode body) {
		name = name2;
		this.parameters = parameters;
		this.body = body;
	}
}
