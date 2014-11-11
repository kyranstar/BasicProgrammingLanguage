/*
 * @author Kyran Adams
 */
package parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import parser.ExpressionNode.FunctionCallNode;
import parser.ExpressionNode.ListIndexNode;
import parser.ExpressionNode.MultiplicationNode;
import parser.ExpressionNode.RangeNode;
import parser.ExpressionNode.SubtractionNode;
import parser.ExpressionNode.VariableNode;
import parser.checking.CompilerException;
import parser.checking.TreeChecker;
import type.APValue.Operators;
import type.APValueBool;
import type.APValueChar;
import type.APValueFunction;
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
            try {
                TreeChecker.checkTree(expressions);
            } catch (final CompilerException e) {
                throw new ParserException("Could not compile", e);
            }

            return expressions;
        } catch (final ParserException e) {
            throw new ParserException(e.getMessage() + lookahead.getMessage(),
                    e);
        }
    }
    
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
        if (lookahead.getType() == TokenType.TO) {
            return highOp(matchRange(expr, context), context);
        } else if (lookahead.getType() == TokenType.MULDIV) {
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
            return highOp(new ListIndexNode(expr, insideParens), context);

        } else if (lookahead.getType() == TokenType.OPEN_PARENS) {
            if (expr.getClass() != VariableNode.class) {
                throw new ParserException(
                        "Can't call function on a non function value");
            }

            return highOp(functionParameters(context, (VariableNode) expr),
                    context);
        } else if (lookahead.getType() == TokenType.IDENTIFIER) {
            final VariableNode functionName = identifier();
            nextToken();
            final ExpressionNode secondArg = expression(context);
            // binary function
            return highOp(
                    new FunctionCallNode(functionName.getName(), Arrays.asList(
                            expr, secondArg)), context);
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
            return matchNumber();
        } else if (lookahead.getType() == TokenType.BOOLEAN) {
            return matchBoolean();
        } else if (lookahead.getType() == TokenType.STRING) {
            return matchString();
        } else if (lookahead.getType() == TokenType.CHAR) {
            return matchChar();
        } else if (lookahead.getType() == TokenType.OPEN_BRACKET) {
            return matchList(context);
        } else if (lookahead.getType() == TokenType.IDENTIFIER) {
            final VariableNode expr = identifier();
            nextToken();
            return expr;
        } else if (lookahead.getType() == TokenType.LAMBDA) {
            return lambda(context);
        } else {
            throw new ParserException("Unexpected token " + lookahead
                    + " found");
        }
    }

    private ExpressionNode lambda(final Context context) {
        assert lookahead.getType() == TokenType.LAMBDA;
        nextToken();
        final VariableNode expr = identifier();
        nextToken();
        final List<VariableNode> expressions = new ArrayList<>();
        expressions.add(expr);
        while (lookahead.getType() != TokenType.ARROW) {
            nextToken();
            if (lookahead.getType() != TokenType.ARROW) {
                expressions.add(identifier());
            }
        }
        nextToken();
        final Function func = new Function(null, expressions,
                expression(context));
        return new ConstantNode(new APValueFunction(func));
    }
    
    private ExpressionNode matchBoolean() {
        final ExpressionNode.ConstantNode expr = new ExpressionNode.ConstantNode(
                new APValueBool(Boolean.parseBoolean(lookahead.getText())));
        nextToken();
        return expr;
    }

    private ExpressionNode matchNumber() {
        final ExpressionNode.ConstantNode expr = new ExpressionNode.ConstantNode(
                new APValueNum(new BigDecimal(lookahead.getText())));
        nextToken();
        return expr;
    }

    private ExpressionNode matchList(final Context context) {
        final List<ExpressionNode> nodes = new ArrayList<>();
        while (lookahead.getType() != TokenType.CLOSE_BRACKET) {
            nextToken();
            nodes.add(expression(context));
        }
        nextToken();
        return new ConstantNode(new APValueList(nodes));
    }

    private ExpressionNode matchRange(final ExpressionNode first,
            final Context context) {
        assert lookahead.getType() == TokenType.TO;
        nextToken();
        final ExpressionNode second = expression(context);
        return new RangeNode(first, second);
    }
    
    private ExpressionNode matchString() {
        final String stringMinusQuotes = lookahead.getText().substring(1,
                lookahead.getText().length() - 1);
        final ConstantNode expr = new ConstantNode(new APValueList(
                stringToList(unescapeJavaString(stringMinusQuotes))));
        nextToken();
        return expr;
    }

    private ExpressionNode matchChar() {
        final String charMinusQuotes = unescapeJavaString(lookahead.getText()
                .substring(1, lookahead.getText().length() - 1));

        if (charMinusQuotes.length() == 1) {
            final ExpressionNode.ConstantNode expr = new ExpressionNode.ConstantNode(
                    new APValueChar(charMinusQuotes.charAt(0)));
            nextToken();
            return expr;
        } else {
            throw new ParserException(
                    "Character literal must be one character long");
        }
    }

    /**
     * Unescapes a string that contains standard Java escape sequences.
     * <ul>
     * <li><strong>&#92;b &#92;f &#92;n &#92;r &#92;t &#92;" &#92;'</strong> :
     * BS, FF, NL, CR, TAB, double and single quote.</li>
     * <li><strong>&#92;X &#92;XX &#92;XXX</strong> : Octal character
     * specification (0 - 377, 0x00 - 0xFF).</li>
     * <li><strong>&#92;uXXXX</strong> : Hexadecimal based Unicode character.</li>
     * </ul>
     *
     * @param st
     *            A string optionally containing standard java escape sequences.
     * @return The translated string.
     */
    public static String unescapeJavaString(final String st) {
        
        final StringBuilder sb = new StringBuilder(st.length());
        
        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch == '\\') {
                final char nextChar = i == st.length() - 1 ? '\\' : st
                        .charAt(i + 1);
                // Octal escape?
                if (nextChar >= '0' && nextChar <= '7') {
                    String code = "" + nextChar;
                    i++;
                    if (i < st.length() - 1 && st.charAt(i + 1) >= '0'
                            && st.charAt(i + 1) <= '7') {
                        code += st.charAt(i + 1);
                        i++;
                        if (i < st.length() - 1 && st.charAt(i + 1) >= '0'
                                && st.charAt(i + 1) <= '7') {
                            code += st.charAt(i + 1);
                            i++;
                        }
                    }
                    sb.append((char) Integer.parseInt(code, 8));
                    continue;
                }
                switch (nextChar) {
                    case '\\':
                        ch = '\\';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
                    // Hex Unicode: u????
                    case 'u':
                        if (i >= st.length() - 5) {
                            ch = 'u';
                            break;
                        }
                        final int code = Integer.parseInt(
                                "" + st.charAt(i + 2) + st.charAt(i + 3)
                                        + st.charAt(i + 4) + st.charAt(i + 5),
                                16);
                        sb.append(Character.toChars(code));
                        i += 5;
                        continue;
                }
                i++;
            }
            sb.append(ch);
        }
        return sb.toString();
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
