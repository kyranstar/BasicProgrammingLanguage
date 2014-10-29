package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lexer.Lexer;
import lexer.Token;
import machine.Context;
import parser.ExpressionNode;
import parser.Parser;

public class Main {
	
	public static void main(final String[] args) {
		final List<Token> tokens = new Lexer("a = true && false; a = 3;").lex();
		System.out.println(tokens);
		
		final Context context = new Context();
		
		final List<ExpressionNode.VariableNode> parameters = new ArrayList<>();
		
		final List<ExpressionNode> nodes = new Parser(tokens).parse(context);
		for (final ExpressionNode node : nodes) {
			System.out.println(node.getValue(context));
		}
		for (final Map.Entry<String, ExpressionNode> a : context.getContext().entrySet()) {
			System.out.println(a.getKey() + " -> " + a.getValue().getValue(context));
		}
	}
}
