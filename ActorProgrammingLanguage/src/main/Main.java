package main;

import java.util.List;

import lexer.Lexer;
import lexer.Token;
import parser.ExpressionNode;
import parser.Parser;

public class Main {

	public static void main(final String[] args) {
		final List<Token> tokens = new Lexer("abc").lex();
		System.out.println(tokens);
		final ExpressionNode node = new Parser(tokens).parse();
		System.out.println(node);
		System.out.println(node.getValue());
	}
}
