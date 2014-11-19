/*
 * @author Kyran Adams
 */
package parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

import lexer.PositionInfo;
import lexer.Token;
import lexer.Token.TokenType;
import machine.Context;
import machine.DataStructure;
import machine.Function;
import parser.ExpressionNode.AdditionNode;
import parser.ExpressionNode.AndNode;
import parser.ExpressionNode.AssignmentNode;
import parser.ExpressionNode.ConstantNode;
import parser.ExpressionNode.DivisionNode;
import parser.ExpressionNode.EqualNode;
import parser.ExpressionNode.ExponentiationNode;
import parser.ExpressionNode.FieldAccessNode;
import parser.ExpressionNode.FieldAssignmentNode;
import parser.ExpressionNode.FunctionCallNode;
import parser.ExpressionNode.GreaterThanEqualNode;
import parser.ExpressionNode.GreaterThanNode;
import parser.ExpressionNode.IfNode;
import parser.ExpressionNode.LessThanEqualNode;
import parser.ExpressionNode.LessThanNode;
import parser.ExpressionNode.ListIndexNode;
import parser.ExpressionNode.ModNode;
import parser.ExpressionNode.MultiplicationNode;
import parser.ExpressionNode.OrNode;
import parser.ExpressionNode.RangeNode;
import parser.ExpressionNode.SubtractionNode;
import parser.ExpressionNode.VariableNode;
import parser.checking.CompilerException;
import parser.checking.TreeChecker;
import type.APValueBool;
import type.APValueChar;
import type.APValueData;
import type.APValueFunction;
import type.APValueList;
import type.APValueNum;
import type.DataStructureInstance;

/**
 * The Class Parser. Takes a list of tokens and turns it into a parse tree.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Parser {
    
    /** The Constant NEGATIVE_ONE. */
    private static final ExpressionNode NEGATIVE_ONE = new ConstantNode(
            new APValueNum(new BigDecimal("-1")));
    
    /** The Constant BACKWARD_TOKENS_IN_ERROR. */
    private static final int BACKWARD_TOKENS_IN_ERROR = 5;
    
    /** The Constant lastTokens. */
    private final Stack<Token> lastTokens = new Stack<>();
    
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
            // We take a token and add it to the history of tokens
            lastTokens.add(tokens.pop());
        } catch (final NoSuchElementException e) {
            throw new ParserException("Ran out of characters!", e);
        }
        // at the end of input we return an end of file token
        if (tokens.isEmpty()) {
            lookahead = new Token(TokenType.EOF, "EOF", new PositionInfo());
        } else {
            lookahead = tokens.getFirst();
        }
        // We only want BACKWARD_TOKENS... tokens in our stack
        if (lastTokens.size() > BACKWARD_TOKENS_IN_ERROR) {
            lastTokens.remove(0);
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
            final StringBuilder sb = new StringBuilder();
            for (final Token t : lastTokens) {
                sb.append(t.getText()).append(' ');
            }
            
            throw new ParserException(lookahead.getMessage() + "\n"
                    + "After : " + sb.toString() + "\n" + e.getMessage(), e);
        }
    }

    /**
     * Statement.
     *
     * @param context
     *            the context
     * @return the expression node
     */
    private ExpressionNode statement(final Context context) {
        boolean mutable = false;
        if (lookahead.getType() == TokenType.MUTABLE) {
            mutable = true;
            nextToken();
        }

        if (lookahead.getType() == TokenType.IDENTIFIER) {
            final VariableNode expr = identifier();
            nextToken();
            if (lookahead.getType() == TokenType.DOT) {
                // Assignment with field access as lh operator
                nextToken();
                assertNextToken(TokenType.IDENTIFIER);
                final VariableNode field = identifier();
                nextToken();
                final ExpressionNode assignment = fieldAssignment(context,
                        expr, field);
                assertNextToken(TokenType.SEMI);
                nextToken();
                return assignment;
            } else if (lookahead.getType() == TokenType.EQUAL) {
                // variable declaration
                final ExpressionNode assignment = assignment(context, expr,
                        mutable);
                assertNextToken(TokenType.SEMI);
                nextToken();
                return assignment;
            } else if (lookahead.getType() == TokenType.IDENTIFIER) {
                // function declaration
                final List<VariableNode> variables = new ArrayList<>();
                while (lookahead.getType() == TokenType.IDENTIFIER) {
                    variables.add(identifier());
                    nextToken();
                }
                assertNextToken(TokenType.EQUAL);
                nextToken();
                final ExpressionNode expression = expression(context);
                final Function function = new Function(expr.getName(),
                        variables, expression);
                context.putFunction(function, mutable);
                assertNextToken(TokenType.SEMI);
                nextToken();
                return ExpressionNode.VOID;
                
            } else if (lookahead.getType() == TokenType.OPEN_PARENS) {
                // function call
                final ExpressionNode node = functionParameters(context, expr);
                nextToken();
                return node;
            }
        } else if (lookahead.getType() == TokenType.DATA_TYPE) {
            // data type declaration
            nextToken();
            assertNextToken(TokenType.IDENTIFIER);
            final VariableNode dataTypeName = identifier();
            nextToken();
            assertNextToken(TokenType.EQUAL);
            nextToken();
            assertNextToken(TokenType.OPEN_CURLY_BRACKET);
            nextToken();
            final List<String> fields = new ArrayList<>();
            while (true) {
                assertNextToken(TokenType.IDENTIFIER);
                fields.add(identifier().getName());
                nextToken();
                if (lookahead.getType() == TokenType.CLOSE_CURLY_BRACKET) {
                    nextToken();
                    break;
                }
                assertNextToken(TokenType.COMMA);
                nextToken();
            }
            context.putDataType(new DataStructure(dataTypeName.getName(),
                    fields));
            assertNextToken(TokenType.SEMI);
            nextToken();
            return ExpressionNode.VOID;
        }
        
        throw new ParserException(
                "Non function call, assignment, datatype definition, or function definition statement");

    }

    private ExpressionNode fieldAssignment(final Context context,
            final VariableNode expr, final VariableNode field) {
        assertNextToken(TokenType.EQUAL);
        nextToken();
        final ExpressionNode assigned = expression(context);
        return new FieldAssignmentNode(expr, field, assigned);
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
    private AssignmentNode assignment(final Context context,
            final VariableNode expr, final boolean mutable) {
        assertNextToken(TokenType.EQUAL);
        nextToken();
        final ExpressionNode assigned = expression(context);
        final AssignmentNode assignment = new AssignmentNode(expr, assigned,
                mutable);
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
        } else if (lookahead.getType() == TokenType.NEW) {
            return newExpr(context);
        }

        final ExpressionNode expr = signedTerm(context);
        return lowOp(expr, context);
    }

    private ExpressionNode newExpr(final Context context) {
        assertNextToken(TokenType.NEW);
        nextToken();
        assertNextToken(TokenType.IDENTIFIER);
        final String type = identifier().getName();
        nextToken();
        assertNextToken(TokenType.OPEN_PARENS);
        final Map<String, ExpressionNode> values = new HashMap<>();
        while (true) {
            nextToken();
            assertNextToken(TokenType.IDENTIFIER);
            final VariableNode field = identifier();
            nextToken();
            final AssignmentNode node = assignment(context, field, true);
            values.put(node.getVariable().getName(), node.getExpression());
            if (lookahead.getType() == TokenType.CLOSE_PARENS) {
                nextToken();
                break;
            }
            assertNextToken(TokenType.COMMA);
        }
        return new ConstantNode(new APValueData(new DataStructureInstance(type,
                values)));
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
        assertNextToken(TokenType.IF);
        nextToken();
        final ExpressionNode ifExpr = expression(context);
        assertNextToken(TokenType.THEN);
        nextToken();
        final ExpressionNode thenExpr = expression(context);
        assertNextToken(TokenType.ELSE);
        nextToken();
        final ExpressionNode elseExpr = expression(context);
        return new IfNode(ifExpr, thenExpr, elseExpr);
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
        if (lookahead.getType() == TokenType.PLUS) {
            nextToken();
            return lowOp(new AdditionNode(expr, signedTerm(context)), context);
        } else if (lookahead.getType() == TokenType.MINUS) {
            nextToken();
            return lowOp(new SubtractionNode(expr, signedTerm(context)),
                    context);
        } else if (lookahead.getType() == TokenType.EQUAL) {
            nextToken();
            return lowOp(new EqualNode(expr, signedTerm(context)), context);
        } else if (lookahead.getType() == TokenType.LESS_THAN) {
            nextToken();
            return lowOp(new LessThanNode(expr, signedTerm(context)), context);
        } else if (lookahead.getType() == TokenType.GREATER_THAN) {
            nextToken();
            return lowOp(new GreaterThanNode(expr, signedTerm(context)),
                    context);
        } else if (lookahead.getType() == TokenType.LESS_THAN_EQUAL) {
            nextToken();
            return lowOp(new LessThanEqualNode(expr, signedTerm(context)),
                    context);
        } else if (lookahead.getType() == TokenType.GREATER_THAN_EQUAL) {
            nextToken();
            return lowOp(new GreaterThanEqualNode(expr, signedTerm(context)),
                    context);
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
        if (lookahead.getType() == TokenType.MULTIPLY) {
            // term_op -> MULTDIV factor term_op
            nextToken();
            final ExpressionNode prod = new MultiplicationNode(expr,
                    signedHighTerm(context));
            return highOp(prod, context);
        } else if (lookahead.getType() == TokenType.DIVIDE) {
            // term_op -> MULTDIV factor term_op
            nextToken();
            final ExpressionNode prod = new DivisionNode(expr,
                    signedHighTerm(context));
            return highOp(prod, context);
        } else if (lookahead.getType() == TokenType.MOD) {
            // term_op -> MULTDIV factor term_op
            nextToken();
            return highOp(new ModNode(expr, signedHighTerm(context)), context);
        } else if (lookahead.getType() == TokenType.AND) {
            nextToken();
            return highOp(new AndNode(expr, signedHighTerm(context)), context);
        } else if (lookahead.getType() == TokenType.OR) {
            nextToken();
            return highOp(new OrNode(expr, signedHighTerm(context)), context);
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
    private ExpressionNode signedHighTerm(final Context context) {
        if (lookahead.getType() == TokenType.PLUS) {
            nextToken();
            return factor(context);
        } else if (lookahead.getType() == TokenType.MINUS) {
            nextToken();
            return new MultiplicationNode(factor(context), NEGATIVE_ONE);
        } else {
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
        return highestOp(argument(context), context);
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
    private ExpressionNode highestOp(final ExpressionNode expr,
            final Context context) {
        if (lookahead.getType() == TokenType.RAISED) {
            // factor_op -> RAISED expression
            nextToken();
            return highestOp(new ExponentiationNode(expr,
                    signedHighTerm(context)), context);
            
        } else if (lookahead.getType() == TokenType.OPEN_CURLY_BRACKET) {
            nextToken();
            final ExpressionNode insideParens = expression(context);
            assertNextToken(TokenType.CLOSE_CURLY_BRACKET);
            nextToken();
            return highestOp(new ListIndexNode(expr, insideParens), context);
            
        } else if (lookahead.getType() == TokenType.OPEN_PARENS) {
            if (expr.getClass() != VariableNode.class) {
                throw new ParserException(
                        "Can't call function on a non function value");
            }
            
            return highestOp(functionParameters(context, (VariableNode) expr),
                    context);
        } else if (lookahead.getType() == TokenType.DOT) {
            // field access
            nextToken();
            assertNextToken(TokenType.IDENTIFIER);
            final VariableNode node = identifier();
            nextToken();
            return highestOp(new FieldAccessNode(expr, node), context);
            
        } else if (lookahead.getType() == TokenType.TO) {
            return highestOp(matchRange(expr, context), context);
        } else {
            return expr;
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
            
            assertNextToken(TokenType.CLOSE_PARENS);
            
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
        if (lookahead.getType() == TokenType.PLUS) {
            // signed_term -> PLUSMINUS term
            nextToken();
            return term(context);
        } else if (lookahead.getType() == TokenType.MINUS) {
            // signed_term -> PLUSMINUS term
            nextToken();
            return new MultiplicationNode(term(context), NEGATIVE_ONE);
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
        } else if (lookahead.getType() == TokenType.OPEN_SQUARE_BRACKET) {
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
    
    /**
     * Lambda.
     *
     * @param context
     *            the context
     * @return the expression node
     */
    private ExpressionNode lambda(final Context context) {
        assertNextToken(TokenType.LAMBDA);
        nextToken();
        final VariableNode expr = identifier();
        nextToken();
        final List<VariableNode> params = new ArrayList<>();
        params.add(expr);
        while (lookahead.getType() != TokenType.ARROW_RIGHT) {
            nextToken();
            if (lookahead.getType() != TokenType.ARROW_RIGHT) {
                params.add(identifier());
            }
        }
        nextToken();
        final Function func = new Function(null, params, expression(context));
        return new ConstantNode(new APValueFunction(func));
    }

    /**
     * Match boolean.
     *
     * @return the expression node
     */
    private ExpressionNode matchBoolean() {
        final ConstantNode expr = new ConstantNode(new APValueBool(
                Boolean.parseBoolean(lookahead.getText())));
        nextToken();
        return expr;
    }
    
    /**
     * Match number.
     *
     * @return the expression node
     */
    private ExpressionNode matchNumber() {
        final ConstantNode expr = new ConstantNode(new APValueNum(
                new BigDecimal(lookahead.getText())));
        nextToken();
        return expr;
    }
    
    /**
     * Match list.
     *
     * @param context
     *            the context
     * @return the expression node
     */
    private ExpressionNode matchList(final Context context) {
        assertNextToken(TokenType.OPEN_SQUARE_BRACKET);
        final List<ExpressionNode> nodes = new ArrayList<>();
        while (lookahead.getType() != TokenType.CLOSE_SQUARE_BRACKET) {
            nextToken();
            if (lookahead.getType() == TokenType.CLOSE_SQUARE_BRACKET) {
                break;
            }
            nodes.add(expression(context));
        }
        assertNextToken(TokenType.CLOSE_SQUARE_BRACKET);
        nextToken();
        return new ConstantNode(new APValueList(nodes));
    }
    
    /**
     * Match range.
     *
     * @param first
     *            the first
     * @param context
     *            the context
     * @return the expression node
     */
    private ExpressionNode matchRange(final ExpressionNode first,
            final Context context) {
        assertNextToken(TokenType.TO);
        nextToken();
        final ExpressionNode second = expression(context);
        return new RangeNode(first, second);
    }

    /**
     * Match string.
     *
     * @return the expression node
     */
    private ExpressionNode matchString() {
        final String stringMinusQuotes = lookahead.getText().substring(1,
                lookahead.getText().length() - 1);
        final ConstantNode expr = new ConstantNode(new APValueList(
                stringToList(unescapeJavaString(stringMinusQuotes))));
        nextToken();
        return expr;
    }
    
    /**
     * Match char.
     *
     * @return the expression node
     */
    private ExpressionNode matchChar() {
        final String charMinusQuotes = unescapeJavaString(lookahead.getText()
                .substring(1, lookahead.getText().length() - 1));
        
        if (charMinusQuotes.length() == 1) {
            final ConstantNode expr = new ConstantNode(new APValueChar(
                    charMinusQuotes.charAt(0)));
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
    
    /**
     * String to list.
     *
     * @param text
     *            the text
     * @return the list
     */
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
            final FunctionCallNode node = new FunctionCallNode(expr.getName(),
                    parameters);
            nextToken();
            return node;
        }
        parameters.add(expression(context));
        if (lookahead.getType() == TokenType.CLOSE_PARENS) {
            // One parameter
            final FunctionCallNode node = new FunctionCallNode(expr.getName(),
                    parameters);
            nextToken();
            return node;
        }
        
        while (lookahead.getType() != TokenType.CLOSE_PARENS) {
            assertNextToken(TokenType.COMMA);
            nextToken();
            parameters.add(expression(context));
        }
        nextToken();
        
        return new FunctionCallNode(expr.getName(), parameters);
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

    /**
     * Assert next token.
     *
     * @param t
     *            the t
     */
    public void assertNextToken(final TokenType t) {
        if (lookahead.getType() != t) {
            throw new ParserException("Expected " + t + " but was " + lookahead);
        }
    }

    /**
     * Assert next token.
     *
     * @param t
     *            the t
     * @param expectedText
     *            the expected text
     */
    public void assertNextToken(final TokenType t, final String expectedText) {
        if (lookahead.getType() != t) {
            throw new ParserException("Expected " + t + "<" + expectedText
                    + ">" + " but was " + lookahead);
        }
    }
    
}
