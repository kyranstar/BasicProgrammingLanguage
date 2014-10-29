package parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import lexer.Token;
import lexer.Token.TokenType;
import machine.Context;
import machine.Function;
import parser.ExpressionNode.VariableNode;
import type.APValue;
import type.APValue.APValueBool;
import type.APValue.APValueNum;
import type.APValue.Methods;

public class Parser {
	private static final APValueNum NEGATIVE_ONE = new APValueNum(new BigDecimal("-1"));
	LinkedList<Token> tokens = new LinkedList<>();
	Token lookahead;
	
	public Parser(final List<Token> tokens) {
		this.tokens.addAll(tokens);
		if (tokens.isEmpty())
			throw new ParserException("Cannot parse an empty file!");
	}
	
	private void nextToken() {
		try {
			tokens.pop();
		} catch (final NoSuchElementException e) {
			throw new ParserException("Ran out of characters!", e);
		}
		// at the end of input we return an epsilon token
		if (tokens.isEmpty()) {
			lookahead = new Token(TokenType.EOF, "<EOF>");
		} else {
			lookahead = tokens.getFirst();
		}
	}
	
	public List<ExpressionNode> parse(final Context context) {
		
		final List<ExpressionNode> expressions = new ArrayList<>();
		
		lookahead = tokens.getFirst();
		
		while (lookahead.getType() != TokenType.EOF) {
			expressions.add(statement(context));
		}
		return expressions;
	}
	
	private ExpressionNode statement(final Context context) {
		final ExpressionNode.VariableNode expr = identifier();
		nextToken();
		if (lookahead.getType() == TokenType.EQUAL) {
			final ExpressionNode assignment = assignment(context, expr);
			return assignment;
		} else if (lookahead.getType() == TokenType.IDENTIFIER) {
			// we have a parameter
			final List<VariableNode> variables = new ArrayList<>();
			while (lookahead.getType() == TokenType.IDENTIFIER) {
				variables.add(identifier());
				nextToken();
			}
			if (lookahead.getType() == TokenType.EQUAL) {
				nextToken();
				final ExpressionNode expression = expression(context);
				final Function function = new Function(expr.getName(), variables, expression);
				context.putFunction(expr.getName(), function);
				nextToken();
				return ExpressionNode.VOID;
			} else
				throw new ParserException("Expected EQUAL, got " + lookahead);
		} else {
			System.out.println(lookahead.getType());
			throw new ParserException("Only assignments supported now.");
		}
		
	}
	
	private ExpressionNode assignment(final Context context, final ExpressionNode.VariableNode expr) {
		nextToken();
		final ExpressionNode assigned = expression(context);
		final ExpressionNode assignment = new ExpressionNode.AssignmentNode(expr, assigned);
		if (lookahead.getType() != TokenType.SEMI)
			throw new ParserException("Expected semicolon, got " + lookahead.getText());
		nextToken();
		return assignment;
	}
	
	private ExpressionNode expression(final Context context) {
		final ExpressionNode expr = signedTerm(context);
		return sumOp(expr, context);
	}
	
	private ExpressionNode sumOp(final ExpressionNode expr, final Context context) {
		if (lookahead.getType() == TokenType.PLUSMINUS) {
			// sum_op -> PLUSMINUS term sum_op
			ExpressionNode sum;
			final boolean positive = lookahead.getText().equals("+");
			nextToken();
			final ExpressionNode t = term(context);
			if (positive) {
				sum = new ExpressionNode.AdditionNode(expr, t);
			} else {
				sum = new ExpressionNode.SubtractionNode(expr, t);
			}
			
			return sumOp(sum, context);
		} else if (lookahead.getType() == TokenType.OR) {
			nextToken();
			return sumOp(new ExpressionNode.OrNode(expr, term(context)), context);
		} else
			// sum_op -> EPSILON
			return expr;
	}
	
	private ExpressionNode term(final Context context) {
		// term -> factor term_op
		return termOp(factor(context), context);
	}
	
	private ExpressionNode termOp(final ExpressionNode expr, final Context context) {
		if (lookahead.getType() == TokenType.MULDIV) {
			// term_op -> MULTDIV factor term_op
			ExpressionNode prod;
			
			final boolean positive = lookahead.getText().equals("*");
			nextToken();
			final ExpressionNode f = signedFactor(context);
			
			if (positive) {
				prod = new ExpressionNode.MultiplicationNode(expr, f);
			} else {
				prod = new ExpressionNode.DivisionNode(expr, f);
			}
			
			return termOp(prod, context);
		} else if (lookahead.getType() == TokenType.AND) {
			nextToken();
			return termOp(new ExpressionNode.AndNode(expr, value(context)), context);
		} else
			// term_op -> EPSILON
			return expr;
		
	}
	
	private ExpressionNode signedFactor(final Context context) {
		if (lookahead.getType() == TokenType.PLUSMINUS) {
			final boolean positive = lookahead.getText().equals("+");
			nextToken();
			final ExpressionNode t = factor(context);
			if (positive)
				return t;
			else
				return new ExpressionNode.ConstantNode(((APValueNum) t.getValue(context).getValue()).callMethod(Methods.MULTIPLY, NEGATIVE_ONE));
		} else
			// signed_factor -> factor
			return factor(context);
	}
	
	private ExpressionNode factor(final Context context) {
		// factor -> argument factor_op
		return factorOp(argument(context), context);
	}
	
	private ExpressionNode factorOp(final ExpressionNode expression, final Context context) {
		if (lookahead.getType() == TokenType.RAISED) {
			// factor_op -> RAISED expression
			nextToken();
			final ExpressionNode exponent = signedFactor(context);
			return new ExpressionNode.ExponentiationNode(expression, exponent);
			
		} else
			// factor_op -> EPSILON
			return expression;
	}
	
	private ExpressionNode argument(final Context context) {
		if (lookahead.getType() == TokenType.OPEN_PARENS) {
			// argument -> OPEN_BRACKET sum CLOSE_BRACKET
			nextToken();
			final ExpressionNode node = expression(context);
			
			if (lookahead.getType() != TokenType.CLOSE_PARENS)
				throw new ParserException("Closing brackets expected and " + lookahead.getText() + " found instead");
			
			nextToken();
			return node;
		} else
			// argument -> value
			return value(context);
	}
	
	private ExpressionNode signedTerm(final Context context) {
		if (lookahead.getType() == TokenType.PLUSMINUS) {
			// signed_term -> PLUSMINUS term
			final boolean positive = lookahead.getText().equals("+");
			nextToken();
			final ExpressionNode t = term(context);
			if (positive)
				return t;
			else
				return new ExpressionNode.ConstantNode(((APValue) t.getValue(context).getValue()).callMethod(Methods.MULTIPLY, NEGATIVE_ONE));
		} else
			// signed_term -> term
			return term(context);
	}
	
	private ExpressionNode value(final Context context) {
		if (lookahead.getType() == TokenType.NUMBER) {
			final ExpressionNode.ConstantNode expr = new ExpressionNode.ConstantNode(new APValueNum(new BigDecimal(lookahead.getText())));
			nextToken();
			return expr;
		} else if (lookahead.getType() == TokenType.BOOLEAN) {
			final ExpressionNode.ConstantNode expr = new ExpressionNode.ConstantNode(new APValueBool(Boolean.parseBoolean(lookahead.getText())));
			nextToken();
			return expr;
		}

		else if (lookahead.getType() == TokenType.IDENTIFIER) {
			
			final ExpressionNode.VariableNode expr = identifier();
			nextToken();
			if (lookahead.getType() == TokenType.OPEN_PARENS)
				return functionParameters(context, expr);
			return expr;
		} else
			throw new ParserException("Unexpected token " + lookahead + " found");
	}

	/**
	 * @param context
	 * @param expr
	 * @return
	 */
	private ExpressionNode functionParameters(final Context context, final ExpressionNode.VariableNode expr) {
		final List<ExpressionNode> parameters = new ArrayList<>();
		nextToken();
		if (lookahead.getType() == TokenType.CLOSE_PARENS) {
			// No params
			final ExpressionNode.FunctionCallNode node = new ExpressionNode.FunctionCallNode(expr, parameters);
			nextToken();
			return node;
		}

		parameters.add(expression(context));
		System.out.println(lookahead.getType());
		if (lookahead.getType() == TokenType.CLOSE_PARENS) {
			// One parameter
			final ExpressionNode.FunctionCallNode node = new ExpressionNode.FunctionCallNode(expr, parameters);
			nextToken();
			return node;
		}
		
		while (lookahead.getType() != TokenType.CLOSE_PARENS) {
			if (lookahead.getType() != TokenType.COMMA)
				throw new ParserException("Expected comma, got " + lookahead);
			nextToken();
			parameters.add(expression(context));
		}
		nextToken();
		
		return new ExpressionNode.FunctionCallNode(expr, parameters);
	}
	
	private ExpressionNode.VariableNode identifier() {
		return new ExpressionNode.VariableNode(lookahead.getText());
	}
}
