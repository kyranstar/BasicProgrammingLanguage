package main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lexer.Lexer;
import lexer.Token;
import machine.Context;
import machine.Function;
import parser.ExpressionNode;
import parser.ExpressionNode.AdditionNode;
import parser.ExpressionNode.ConstantNode;
import parser.Parser;

public class Main {

	public static void main(final String[] args) {
		final List<Token> tokens = new Lexer("a = 5; b = f(56);").lex();
		System.out.println(tokens);

		final Context context = new Context();

		final List<ExpressionNode.VariableNode> parameters = new ArrayList<>();

		parameters.add(new ExpressionNode.VariableNode("a"));

		context.putFunction("f", new Function("f", parameters,
				new AdditionNode(new ExpressionNode.VariableNode("a"),
						new ConstantNode(new BigDecimal("1")))));

		final List<ExpressionNode> nodes = new Parser(tokens).parse(context);
		for (final ExpressionNode node : nodes) {
			System.out.println(node.getValue(context));
		}
		for (final Map.Entry<String, ExpressionNode> a : context.getContext()
				.entrySet()) {
			System.out.println(a.getKey() + " -> "
					+ a.getValue().getValue(context));
		}
	}
}
