package main;

import lexer.Lexer;

public class Main {

	public static void main(final String[] args) {
		System.out.println(new Lexer("812002 + 3834.512").lex());
	}

}
