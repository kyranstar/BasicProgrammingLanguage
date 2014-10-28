package parser;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import lexer.Token;
import lexer.Token.TokenType;

public class Parser {
	LinkedList<Token> tokens = new LinkedList<>();
	Token lookahead;

	public Parser(final List<Token> tokens) {
		this.tokens.addAll(tokens);
		if (tokens.isEmpty())
			throw new ParserException("Cannot parse an empty file!");
	}

	private void nextToken() {
		tokens.pop();
		// at the end of input we return an epsilon token
		if (tokens.isEmpty()) {
			lookahead = new Token(TokenType.EOF, "<EOF>");
		} else {
			lookahead = tokens.getFirst();
		}
	}

	public ExpressionNode<BigDecimal> parse() {
		lookahead = tokens.getFirst();

		final ExpressionNode<BigDecimal> expr = expression();

		if (lookahead.getType() != TokenType.EOF)
			throw new ParserException("Unexpected symbol" + lookahead
					+ " found");
		return expr;
	}

	private ExpressionNode<BigDecimal> expression() {
		final ExpressionNode<BigDecimal> expr = signedTerm();
		return sumOp(expr);
	}

	private ExpressionNode<BigDecimal> sumOp(
			final ExpressionNode<BigDecimal> expr) {
		if (lookahead.getType() == TokenType.PLUSMINUS) {
			// sum_op -> PLUSMINUS term sum_op
			ExpressionNode<BigDecimal> sum;
			final boolean positive = lookahead.getText().equals("+");
			nextToken();
			final ExpressionNode<BigDecimal> t = term();
			if (positive) {
				sum = new ExpressionNode.AdditionNode(expr, t);
			} else {
				sum = new ExpressionNode.SubtractionNode(expr, t);
			}

			return sumOp(sum);
		} else
			// sum_op -> EPSILON
			return expr;
	}

	private ExpressionNode<BigDecimal> term() {
		// term -> factor term_op
		return termOp(factor());
	}

	private ExpressionNode<BigDecimal> termOp(
			final ExpressionNode<BigDecimal> expr) {
		if (lookahead.getType() == TokenType.MULDIV) {
			// term_op -> MULTDIV factor term_op
			ExpressionNode<BigDecimal> prod;

			final boolean positive = lookahead.getText().equals("*");
			nextToken();
			final ExpressionNode<BigDecimal> f = signedFactor();

			if (positive) {
				prod = new ExpressionNode.MultiplicationNode(expr, f);
			} else {
				prod = new ExpressionNode.DivisionNode(expr, f);
			}

			return termOp(prod);
		} else
			// term_op -> EPSILON
			return expr;

	}

	private ExpressionNode<BigDecimal> signedFactor() {
		if (lookahead.getType() == TokenType.PLUSMINUS) {
			final boolean positive = lookahead.getText().equals("+");
			nextToken();
			final ExpressionNode<BigDecimal> t = factor();
			if (positive)
				return t;
			else
				return new ExpressionNode.ConstantNode(t.getValue().getValue()
						.multiply(new BigDecimal("-1")));
		} else
			// signed_factor -> factor
			return factor();
	}

	private ExpressionNode<BigDecimal> factor() {
		// factor -> argument factor_op
		return factorOp(argument());
	}

	private ExpressionNode<BigDecimal> factorOp(
			final ExpressionNode<BigDecimal> expression) {
		if (lookahead.getType() == TokenType.RAISED) {
			// factor_op -> RAISED expression
			nextToken();
			final ExpressionNode<BigDecimal> exponent = signedFactor();
			return new ExpressionNode.ExponentiationNode(expression, exponent);

		} else
			// factor_op -> EPSILON
			return expression;
	}

	private ExpressionNode<BigDecimal> argument() {
		if (lookahead.getType() == TokenType.OPEN_PARENS) {
			// argument -> OPEN_BRACKET sum CLOSE_BRACKET
			nextToken();
			final ExpressionNode<BigDecimal> node = expression();

			if (lookahead.getType() != TokenType.CLOSE_PARENS)
				throw new ParserException("Closing brackets expected and "
						+ lookahead.getText() + " found instead");

			nextToken();
			return node;
		} else
			// argument -> value
			return value();
	}

	private ExpressionNode<BigDecimal> signedTerm() {
		if (lookahead.getType() == TokenType.PLUSMINUS) {
			// signed_term -> PLUSMINUS term
			final boolean positive = lookahead.getText().equals("+");
			nextToken();
			final ExpressionNode<BigDecimal> t = term();
			if (positive)
				return t;
			else
				return new ExpressionNode.ConstantNode(t.getValue().getValue()
						.multiply(new BigDecimal("-1")));
		} else
			// signed_term -> term
			return term();
	}

	private ExpressionNode<BigDecimal> value() {
		if (lookahead.getType() == TokenType.NUMBER) {
			final ExpressionNode.ConstantNode expr = new ExpressionNode.ConstantNode(
					new BigDecimal(lookahead.getText()));
			nextToken();
			return expr;
		} else if (lookahead.getType() == TokenType.IDENTIFIER) {
			final ExpressionNode.VariableNode expr = new ExpressionNode.VariableNode(
					lookahead.getText());
			nextToken();
			return expr;
		} else
			throw new ParserException("Unexpected token " + lookahead
					+ " found");
	}
}
