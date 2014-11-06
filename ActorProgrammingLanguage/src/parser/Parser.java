/*
 * @author Kyran Adams
 */
package parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import lexer.LexerInformation;
import lexer.Token;
import lexer.Token.TokenType;
import machine.Context;
import machine.Function;
import parser.ExpressionNode.AdditionNode;
import parser.ExpressionNode.AndNode;
import parser.ExpressionNode.ConstantNode;
import parser.ExpressionNode.DivisionNode;
import parser.ExpressionNode.ExponentiationNode;
import parser.ExpressionNode.ListIndexNode;
import parser.ExpressionNode.MultiplicationNode;
import parser.ExpressionNode.SubtractionNode;
import parser.ExpressionNode.VariableNode;
import type.APValue.Operators;
import type.APValueBool;
import type.APValueChar;
import type.APValueList;
import type.APValueNum;

/**
 * The Class Parser.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Parser {

    /** The Constant NEGATIVE_ONE. */
    private static final APValueNum NEGATIVE_ONE = new APValueNum(
            new BigDecimal("-1"));

    /** The tokens. */
    LinkedList<Token> tokens = new LinkedList<>();

    /** The lookahead token. */
    Token lookahead;
    
    /**
     * Instantiates a new parser.
     *
     * @param tokens
     *            the tokens
     */
    public Parser(final List<Token> tokens) {
        if (tokens.isEmpty()) {
            throw new ParserException("Cannot parse an empty file!");
        }
        this.tokens.addAll(tokens);
    }
    
    /**
     * Next token.
     */
    private void nextToken() {
        try {
            tokens.pop();
        } catch (final NoSuchElementException e) {
            throw new ParserException("Ran out of characters!", e);
        }
        // at the end of input we return an epsilon token
        if (tokens.isEmpty()) {
            lookahead = new Token(TokenType.EOF, "<EOF>",
                    new LexerInformation());
        } else {
            lookahead = tokens.getFirst();
        }
    }
    
    /**
     * Parses a string of code.
     *
     * @param context
     *            the context
     *
     * @return the list
     */
    public List<ExpressionNode> parse(final Context context) {
        try {
            final List<ExpressionNode> expressions = new ArrayList<>();
            
            lookahead = tokens.getFirst();
            
            while (lookahead.getType() != TokenType.EOF) {
                expressions.add(statement(context));
            }
            return expressions;
        } catch (final ParserException e) {
            throw new ParserException(e.getMessage() + lookahead.getMessage());
        }
    }
    
    /**
     * Statement.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode statement(final Context context) {
        final VariableNode expr = identifier();
        nextToken();
        if (lookahead.getType() == TokenType.EQUAL) {
            final ExpressionNode assignment = assignment(context, expr);
            return assignment;
        } else if (lookahead.getType() == TokenType.IDENTIFIER) {
            // we have a parameter
            if (lookahead.getType() == TokenType.IDENTIFIER
                    || lookahead.getType() == TokenType.EQUAL) {
                final List<VariableNode> variables = new ArrayList<>();
                while (lookahead.getType() == TokenType.IDENTIFIER) {
                    variables.add(identifier());
                    nextToken();
                }
                if (lookahead.getType() == TokenType.EQUAL) {
                    nextToken();
                    final ExpressionNode expression = expression(context);
                    final Function function = new Function(expr.getName(),
                            variables, expression);
                    context.putFunction(function);
                    nextToken();
                    return ExpressionNode.VOID;
                } else {
                    throw new ParserException(
                            "Expected parameter list or equal (function def)");
                }
            } else {
                throw new ParserException(
                        "Expected EQUAL or OPEN_PARENS or IDENTIFIER");
            }
        } else if (lookahead.getType() == TokenType.OPEN_PARENS) {
            final ExpressionNode node = functionParameters(context, expr);
            nextToken();
            return node;
        } else {
            throw new ParserException(
                    "Non function call, assignment, or function def statement");
        }
        
    }
    
    /**
     * Assignment.
     *
     * @param context
     *            the context
     * @param expr
     *            the expr
     *
     * @return the expression node
     */
    private ExpressionNode assignment(final Context context,
            final VariableNode expr) {
        nextToken();
        final ExpressionNode assigned = expression(context);
        final ExpressionNode assignment = new ExpressionNode.AssignmentNode(
                expr, assigned);
        if (lookahead.getType() != TokenType.SEMI) {
            throw new ParserException("Expected semicolon, got "
                    + lookahead.getText());
        }
        nextToken();
        return assignment;
    }
    
    /**
     * Expression.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode expression(final Context context) {
        if (lookahead.getType() == TokenType.IF) {
            return ifExpr(context);
        }
        
        final ExpressionNode expr = signedTerm(context);
        return lowOp(expr, context);
    }
    
    /**
     * If expr.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode ifExpr(final Context context) {
        assert lookahead.getType() == TokenType.IF;
        nextToken();
        final ExpressionNode ifExpr = expression(context);
        final ExpressionNode thenExpr = expression(context);
        if (lookahead.getType() != TokenType.ELSE) {
            throw new ParserException("Needs else after if, was " + lookahead);
        }
        nextToken();
        final ExpressionNode elseExpr = expression(context);
        return new ExpressionNode.IfNode(ifExpr, thenExpr, elseExpr);
    }
    
    /*
     * Lower level precedence operations: +, -, ||
     * 
     * @param expr the expr
     * 
     * @param context the context
     * 
     * @return the expression node
     */
    /**
     * Method lowOp.
     *
     * @param expr
     *            ExpressionNode
     * @param context
     *            Context
     * @return ExpressionNode
     */
    private ExpressionNode lowOp(final ExpressionNode expr,
            final Context context) {
        if (lookahead.getType() == TokenType.PLUSMINUS) {
            // sum_op -> PLUSMINUS term sum_op
            ExpressionNode sum;
            final boolean positive = lookahead.getText().equals("+");
            nextToken();
            final ExpressionNode t = signedTerm(context);
            if (positive) {
                sum = new AdditionNode(expr, t);
            } else {
                sum = new SubtractionNode(expr, t);
            }
            
            return lowOp(sum, context);
        } else if (lookahead.getType() == TokenType.OR) {
            nextToken();
            return lowOp(new ExpressionNode.OrNode(expr, term(context)),
                    context);
        } else if (lookahead.getType() == TokenType.EQUAL) {
            nextToken();
            return lowOp(new ExpressionNode.EqualNode(expr, term(context)),
                    context);
        } else if (lookahead.getType() == TokenType.LESS_THAN) {
            nextToken();
            return lowOp(new ExpressionNode.LessThanNode(expr, term(context)),
                    context);
        } else if (lookahead.getType() == TokenType.GREATER_THAN) {
            nextToken();
            return lowOp(
                    new ExpressionNode.GreaterThanNode(expr, term(context)),
                    context);
        } else if (lookahead.getType() == TokenType.LESS_THAN_EQUAL) {
            nextToken();
            return lowOp(new ExpressionNode.LessThanEqualNode(expr,
                    term(context)), context);
        } else if (lookahead.getType() == TokenType.GREATER_THAN_EQUAL) {
            nextToken();
            return lowOp(new ExpressionNode.GreaterThanEqualNode(expr,
                    term(context)), context);
        } else {
            // sum_op -> EPSILON
            return expr;
        }
    }
    
    /**
     * Term.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode term(final Context context) {
        // term -> factor term_op
        return highOp(factor(context), context);
    }
    
    /*
     * High precedence operations: *, /, &&
     * 
     * @param expr the expr
     * 
     * @param context the context
     * 
     * @return the expression node
     */
    /**
     * Method highOp.
     *
     * @param expr
     *            ExpressionNode
     * @param context
     *            Context
     * @return ExpressionNode
     */
    private ExpressionNode highOp(final ExpressionNode expr,
            final Context context) {
        if (lookahead.getType() == TokenType.MULDIV) {
            // term_op -> MULTDIV factor term_op
            ExpressionNode prod;
            
            final boolean positive = lookahead.getText().equals("*");
            nextToken();
            final ExpressionNode f = signedFactor(context);
            
            if (positive) {
                prod = new MultiplicationNode(expr, f);
            } else {
                prod = new DivisionNode(expr, f);
            }
            
            return highOp(prod, context);
        } else if (lookahead.getType() == TokenType.AND) {
            nextToken();
            return highOp(new AndNode(expr, value(context)), context);
        } else if (lookahead.getType() == TokenType.OPEN_CURLY) {
            nextToken();
            final ExpressionNode insideParens = expression(context);
            if (lookahead.getType() != TokenType.CLOSE_CURLY) {
                throw new ParserException("Expected CLOSE_CURLY");
            }
            nextToken();
            return lowOp(new ListIndexNode(expr, insideParens), context);

        } else {
            // term_op -> EPSILON
            return expr;
        }
        
    }
    
    /**
     * Signed factor.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode signedFactor(final Context context) {
        if (lookahead.getType() == TokenType.PLUSMINUS) {
            final boolean positive = lookahead.getText().equals("+");
            nextToken();
            final ExpressionNode t = factor(context);
            if (positive) {
                return t;
            } else {
                return new ExpressionNode.ConstantNode(
                        ((APValueNum) t.getValue(context)).callMethod(
                                Operators.MULTIPLY, NEGATIVE_ONE));
            }
        } else {
            // signed_factor -> factor
            return factor(context);
        }
    }
    
    /**
     * matches a factor.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode factor(final Context context) {
        // factor -> argument factor_op
        return factorOp(argument(context), context);
    }
    
    /**
     * matches a factor operation.
     *
     * @param expression
     *            the expression
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode factorOp(final ExpressionNode expression,
            final Context context) {
        if (lookahead.getType() == TokenType.RAISED) {
            // factor_op -> RAISED expression
            nextToken();
            final ExpressionNode exponent = signedFactor(context);
            return new ExponentiationNode(expression, exponent);
            
        } else {
            // factor_op -> EPSILON
            return expression;
        }
    }
    
    /**
     * matches an argument.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode argument(final Context context) {
        if (lookahead.getType() == TokenType.OPEN_PARENS) {
            // argument -> OPEN_BRACKET sum CLOSE_BRACKET
            nextToken();
            final ExpressionNode node = expression(context);
            
            if (lookahead.getType() != TokenType.CLOSE_PARENS) {
                throw new ParserException("Closing brackets expected and "
                        + lookahead.getText() + " found instead");
            }
            
            nextToken();
            return node;
        } else {
            // argument -> value
            return value(context);
        }
    }
    
    /**
     * matches a signed term.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode signedTerm(final Context context) {
        if (lookahead.getType() == TokenType.PLUSMINUS) {
            // signed_term -> PLUSMINUS term
            final boolean positive = lookahead.getText().equals("+");
            nextToken();
            final ExpressionNode t = term(context);
            if (positive) {
                return t;
            } else {
                return new ExpressionNode.ConstantNode(t.getValue(context)
                        .callMethod(Operators.MULTIPLY, NEGATIVE_ONE));
            }
        } else {
            // signed_term -> term
            return term(context);
        }
    }
    
    /**
     * matches a value.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    private ExpressionNode value(final Context context) {
        if (lookahead.getType() == TokenType.NUMBER) {
            final ExpressionNode.ConstantNode expr = new ExpressionNode.ConstantNode(
                    new APValueNum(new BigDecimal(lookahead.getText())));
            nextToken();
            return expr;
        } else if (lookahead.getType() == TokenType.BOOLEAN) {
            final ExpressionNode.ConstantNode expr = new ExpressionNode.ConstantNode(
                    new APValueBool(Boolean.parseBoolean(lookahead.getText())));
            nextToken();
            return expr;
        } else if (lookahead.getType() == TokenType.STRING) {
            final ExpressionNode.ConstantNode expr = new ExpressionNode.ConstantNode(
                    new APValueList(stringToList(lookahead.getText().substring(
                            1, lookahead.getText().length() - 1))));
            nextToken();
            return expr;
        } else if (lookahead.getType() == TokenType.OPEN_BRACKET) {
            final List<ExpressionNode> nodes = new ArrayList<>();
            while (lookahead.getType() != TokenType.CLOSE_BRACKET) {
                nextToken();
                nodes.add(expression(context));
            }
            nextToken();
            return new ExpressionNode.ConstantNode(new APValueList(nodes));
        }
        
        else if (lookahead.getType() == TokenType.IDENTIFIER) {
            
            final VariableNode expr = identifier();
            nextToken();
            if (lookahead.getType() == TokenType.OPEN_PARENS) {
                return functionParameters(context, expr);
            }
            return expr;
        } else {
            throw new ParserException("Unexpected token " + lookahead
                    + " found");
        }
    }
    
    private List<ExpressionNode> stringToList(final String text) {
        final List<ExpressionNode> list = new ArrayList<>();
        for (final Character c : text.toCharArray()) {
            list.add(new ConstantNode(new APValueChar(c)));
        }
        return list;
    }

    /**
     * Matches function parameters.
     *
     * @param context
     *            the context
     * @param expr
     *            the expr
     *
     * @return the expression node
     */
    private ExpressionNode functionParameters(final Context context,
            final VariableNode expr) {
        final List<ExpressionNode> parameters = new ArrayList<>();
        nextToken();
        if (lookahead.getType() == TokenType.CLOSE_PARENS) {
            // No params
            final ExpressionNode.FunctionCallNode node = new ExpressionNode.FunctionCallNode(
                    expr.getName(), parameters);
            nextToken();
            return node;
        }
        parameters.add(expression(context));
        if (lookahead.getType() == TokenType.CLOSE_PARENS) {
            // One parameter
            final ExpressionNode.FunctionCallNode node = new ExpressionNode.FunctionCallNode(
                    expr.getName(), parameters);
            nextToken();
            return node;
        }
        
        while (lookahead.getType() != TokenType.CLOSE_PARENS) {
            if (lookahead.getType() != TokenType.COMMA) {
                throw new ParserException("Expected comma, got " + lookahead);
            }
            nextToken();
            parameters.add(expression(context));
        }
        nextToken();
        
        return new ExpressionNode.FunctionCallNode(expr.getName(), parameters);
    }
    
    /**
     * Matches an identifier.
     *
     *
     * @return the variable node
     */
    private VariableNode identifier() {
        return new VariableNode(lookahead.getText());
    }
}
