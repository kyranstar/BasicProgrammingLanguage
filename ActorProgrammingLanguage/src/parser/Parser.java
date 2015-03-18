/*
 * @author Kyran Adams
 */
package parser;

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
import machine.Context.VariableMapping;
import machine.DataConstructor;
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
import parser.ExpressionNode.IndexAssignmentNode;
import parser.ExpressionNode.LessThanEqualNode;
import parser.ExpressionNode.LessThanNode;
import parser.ExpressionNode.ListIndexNode;
import parser.ExpressionNode.ModNode;
import parser.ExpressionNode.MultiplicationNode;
import parser.ExpressionNode.OrNode;
import parser.ExpressionNode.RangeNode;
import parser.ExpressionNode.SequenceNode;
import parser.ExpressionNode.SubtractionNode;
import parser.ExpressionNode.VariableNode;
import parser.checking.CompilerException;
import parser.checking.TreeChecker;
import type.APNumber;
import type.APValueBool;
import type.APValueChar;
import type.APValueData;
import type.APValueFunction;
import type.APValueList;
import type.APValueNum;
import type.APValueType;
import type.DataStructureInstance;

/**
 * The Class Parser. Takes a list of tokens and turns it into a parse tree.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Parser {

    /** The Number Constant NEGATIVE_ONE. This is used for the unary operator -. */
    private static final ExpressionNode<APNumber> NEGATIVE_ONE = new ConstantNode<>(
            new APValueNum(new APNumber("-1")));

    /** The Constant BACKWARD_TOKENS_IN_ERROR. */
    private static final int BACKWARD_TOKENS_IN_ERROR = 5;

    /** The Constant lastTokens. */
    private final Stack<Token> lastTokens = new Stack<>();

    /** The tokens list. */
    private final LinkedList<Token> tokens = new LinkedList<>();

    /** The next token. */
    private Token lookahead;
    
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

        lookahead = this.tokens.getFirst();
    }
    
    /**
     * Move the lookahead to the next token.
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
    @SuppressWarnings("rawtypes")
    public List<ExpressionNode> parse(final Context context) {
        try {
            final List<ExpressionNode> expressions = new ArrayList<>();
            
            while (lookahead.getType() != TokenType.EOF) {
                expressions.add(statement(context));
                assertNextToken(TokenType.SEMI);
                nextToken();
            }
            try {
                TreeChecker.checkTree(expressions);
            } catch (final CompilerException e) {
                throw new ParserException("Could not compile", e);
            }

            return expressions;
        } catch (final ParserException e) {
            final StringBuilder lastTokensString = new StringBuilder();
            for (final Token t : lastTokens) {
                lastTokensString.append(t.getText()).append(' ');
            }

            throw new ParserException(lookahead.getMessage() + "\n"
                    + "After : \"" + lastTokensString.toString() + "\"\n"
                    + e.getMessage(), e);
        }
    }
    
    /**
     * Match a statement.
     *
     * @param context
     *            the context
     * @return the expression node
     */
    @SuppressWarnings("rawtypes")
    private ExpressionNode statement(final Context context) {
        boolean mutable = false;
        if (lookahead.getType() == TokenType.MUTABLE) {
            mutable = true;
            nextToken();
        }
        assertNextToken(TokenType.IDENTIFIER, TokenType.DATA_TYPE,
                TokenType.OPEN_CURLY_BRACKET);
        
        if (lookahead.getType() == TokenType.IDENTIFIER) {
            return handleIdStatement(context, mutable);
        } else if (lookahead.getType() == TokenType.DATA_TYPE) {
            return matchDatatypeStatement(context);
        } else {
            assertNextToken(TokenType.OPEN_CURLY_BRACKET);
            return indexAssignment(context, expression(context));
        }
    }
    
    /**
     * Matches a datatype declaration statement
     *
     * @param context
     * @return ExpressionNode.VOID
     */
    private ExpressionNode matchDatatypeStatement(final Context context) {
        // data type declaration
        assertNextToken(TokenType.DATA_TYPE);
        nextToken();
        assertNextToken(TokenType.IDENTIFIER);
        final VariableNode dataTypeName = identifier();
        context.getVariables().put(
                dataTypeName.getName(),
                new VariableMapping(new APValueType(dataTypeName.getName()),
                        false));
        assertNextToken(TokenType.EQUAL);
        nextToken();
        
        while (true) {
            assertNextToken(TokenType.IDENTIFIER);
            final VariableNode subNode = identifier();

            final List<String> fields = new ArrayList<>();
            if (lookahead.getType() == TokenType.OPEN_CURLY_BRACKET) {
                assertNextToken(TokenType.OPEN_CURLY_BRACKET);
                nextToken();
                while (true) {
                    assertNextToken(TokenType.IDENTIFIER);
                    fields.add(identifier().getName());
                    if (lookahead.getType() == TokenType.CLOSE_CURLY_BRACKET) {
                        nextToken();
                        break;
                    }
                    assertNextToken(TokenType.COMMA);
                    nextToken();
                }
            }
            context.putDataType(new DataConstructor(dataTypeName.getName(),
                    subNode.getName(), fields));
            
            if (lookahead.getType() == TokenType.BAR) {
                nextToken();
            } else {
                break;
            }
        }
        return ExpressionNode.VOID;
    }

    /**
     * Matches a statement starting with an identifier
     *
     * @param context
     * @param mutable
     * @return
     */
    private ExpressionNode handleIdStatement(final Context context,
            final boolean mutable) {
        
        assertNextToken(TokenType.IDENTIFIER);
        final VariableNode expr = identifier();
        
        assertNextToken(TokenType.DOT, TokenType.EQUAL, TokenType.OPEN_PARENS,
                TokenType.OPEN_CURLY_BRACKET);

        if (lookahead.getType() == TokenType.DOT) {
            // Assignment with field access as lh operator
            final ExpressionNode assignment = fieldAssignment(context, expr);
            return assignment;
        } else if (lookahead.getType() == TokenType.EQUAL) {
            // variable declaration
            final ExpressionNode assignment = assignment(context, expr, mutable);
            return assignment;
        } else if (lookahead.getType() == TokenType.OPEN_PARENS) {
            // function call
            return functionParameters(context, expr);
        } else {
            assertNextToken(TokenType.OPEN_CURLY_BRACKET);
            // index assignment
            return indexAssignment(context, expr);
        }
        // can't get here
    }

    /**
     * Index assignment.<br>
     * <code>a{b} = c</code> Assumes that a is already matched and passed in as
     * the argument
     *
     * @param context
     *            the context
     * @param leftHand
     *            the left hand expression
     * @return the expression node
     */
    @SuppressWarnings("rawtypes")
    private IndexAssignmentNode indexAssignment(final Context context,
            final ExpressionNode<List> leftHand) {
        assertNextToken(TokenType.OPEN_CURLY_BRACKET);
        nextToken();
        final ExpressionNode insideCurlies = expression(context);
        assertNextToken(TokenType.CLOSE_CURLY_BRACKET);
        nextToken();
        assertNextToken(TokenType.EQUAL);
        nextToken();
        final ExpressionNode rightHandExpression = expression(context);
        return new IndexAssignmentNode(leftHand, insideCurlies,
                rightHandExpression);
    }

    /**
     * Field assignment.<br>
     * <code>a.b = c</code><br>
     * Assumes that a is already matched and are passed in as expr.
     *
     * @param context
     *            the context
     * @param expr
     *            the a expression
     * @return the fieldAssignmentNode representing the assignment
     */
    @SuppressWarnings("rawtypes")
    private ExpressionNode fieldAssignment(final Context context,
            final VariableNode expr) {
        assertNextToken(TokenType.DOT);
        nextToken();
        assertNextToken(TokenType.IDENTIFIER);
        final VariableNode field = identifier();
        assertNextToken(TokenType.EQUAL);
        nextToken();
        final ExpressionNode assigned = expression(context);
        return new FieldAssignmentNode(expr, field, assigned);
    }

    /**
     * Assignment.<br>
     * <code>a = b</code><br>
     * Assumes that a is already matched and is passed in as an argument.
     *
     * @param context
     *            the context
     * @param expr
     *            the left hand expression
     * @param mutable
     *            whether this assignment will be marked mutable or not
     * @return the expression node
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
     * Matches an expression.<br>
     * Can be of forms:<br>
     * <li>If expression: <code>if...</code></li> <li>New expression:
     * <code>new...</code></li> <li>Sequence expression: <code>{...</code></li>
     * <li></li>
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    @SuppressWarnings("rawtypes")
    private ExpressionNode expression(final Context context) {
        if (lookahead.getType() == TokenType.IF) {
            return ifExpr(context);
        } else if (lookahead.getType() == TokenType.NEW) {
            return newExpr(context);
        } else if (lookahead.getType() == TokenType.OPEN_CURLY_BRACKET) {
            return seqExpr(context);
        }

        final ExpressionNode expr = signedTerm(context);
        return lowOp(expr, context);
    }

    /**
     * Matches a sequence expression.<br>
     * <code>{a;b; return c;}</code>
     *
     * @param context
     *            the context
     * @return the expression node
     */
    @SuppressWarnings("rawtypes")
    private ExpressionNode seqExpr(final Context context) {
        assertNextToken(TokenType.OPEN_CURLY_BRACKET);
        nextToken();
        final List<ExpressionNode> statements = new ArrayList<>();

        while (lookahead.getType() != TokenType.RETURN
                && lookahead.getType() != TokenType.CLOSE_CURLY_BRACKET) {
            statements.add(statement(context));
            assertNextToken(TokenType.SEMI);
            nextToken();
        }
        assertNextToken(TokenType.RETURN, TokenType.CLOSE_CURLY_BRACKET);
        if (lookahead.getType() == TokenType.RETURN) {
            nextToken();
            final ExpressionNode expression = expression(context);
            assertNextToken(TokenType.SEMI);
            nextToken();
            assertNextToken(TokenType.CLOSE_CURLY_BRACKET);
            nextToken();
            return new SequenceNode(statements, expression);
        }
        assertNextToken(TokenType.CLOSE_CURLY_BRACKET);
        nextToken();
        return new SequenceNode(statements, ExpressionNode.VOID);
    }

    /**
     * Matches a new datatype expression.<br>
     * <code>new a.b(c,d)</code>
     *
     * @param context
     *            the context
     * @return the expression node
     */
    @SuppressWarnings("rawtypes")
    private ExpressionNode newExpr(final Context context) {
        assertNextToken(TokenType.NEW);
        nextToken();
        assertNextToken(TokenType.IDENTIFIER);
        String type = identifier().getName();
        assertNextToken(TokenType.DOT);
        nextToken();
        assertNextToken(TokenType.IDENTIFIER);
        // add subname
        type += "$" + identifier().getName();
        
        assertNextToken(TokenType.OPEN_PARENS);
        final Map<String, ExpressionNode> values = new HashMap<>();
        while (true) {
            nextToken();
            if (lookahead.getType() == TokenType.CLOSE_PARENS) {
                nextToken();
                break;
            }
            assertNextToken(TokenType.IDENTIFIER);
            final VariableNode field = identifier();
            final AssignmentNode node = assignment(context, field, true);
            values.put(node.getVariable().getName(), node.getExpression());
            if (lookahead.getType() == TokenType.CLOSE_PARENS) {
                nextToken();
                break;
            }
            assertNextToken(TokenType.COMMA);
        }
        return new ConstantNode<>(new APValueData(new DataStructureInstance(
                type, values)));
    }

    /**
     * If expression.
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
     * Matches a term.<br>
     * <code>returns {@link #highOp(ExpressionNode, Context) highOp(}{@link #factor(Context) factor(context)}{@link #highOp(ExpressionNode, Context) ,context)}</code>
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    @SuppressWarnings("rawtypes")
    private ExpressionNode term(final Context context) {
        // term -> factor term_op
        return highOp(factor(context), context);
    }

    /**
     * High precedence operations: *, /, %, &&, || or binary functions
     *
     * @param expr
     *            the left hand expression
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
            // binary function
            
            final VariableNode functionName = identifier();
            final ExpressionNode secondArg = expression(context);
            return highOp(
                    new FunctionCallNode(functionName, Arrays.asList(expr,
                            secondArg)), context);
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
    @SuppressWarnings("rawtypes")
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
    @SuppressWarnings("rawtypes")
    private ExpressionNode factor(final Context context) {
        return highestOp(argument(context), context);
    }

    /**
     * matches a factor operation.
     *
     * @param expr
     *            the expr
     * @param context
     *            the context
     * @return the expression node
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
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
            // if (expr.getClass() != VariableNode.class) {
            // throw new ParserException(
            // "Can't call function on a non function value. Was a "
            // + expr.getClass());
            // }

            return highestOp(functionParameters(context, expr), context);
        } else if (lookahead.getType() == TokenType.DOT) {
            // field access
            nextToken();
            assertNextToken(TokenType.IDENTIFIER);
            final VariableNode node = identifier();
            return highestOp(new FieldAccessNode(expr, node), context);

        } else if (lookahead.getType() == TokenType.TO) {
            return highestOp(matchRange(expr, context), context);
        } else {
            return expr;
        }
    }

    /**
     * matches an single argument<br>
     * <code>(a)</code> or <code>a</code>
     *
     * @param context
     *            the context
     *
     * @return the expression node
     */
    @SuppressWarnings("rawtypes")
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
    @SuppressWarnings("rawtypes")
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
    @SuppressWarnings("rawtypes")
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
            return expr;
        } else if (lookahead.getType() == TokenType.FUNCTION) {
            return function(context);
        } else if (lookahead.getType() == TokenType.TYPE_NAME) {
            final ConstantNode<String> type = new ConstantNode<>(
                    new APValueType(lookahead.getText()));
            nextToken();
            return type;
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
    @SuppressWarnings("rawtypes")
    private ExpressionNode function(final Context context) {
        assertNextToken(TokenType.FUNCTION);
        nextToken();
        final List<VariableNode> params = new ArrayList<>();
        while (lookahead.getType() != TokenType.ARROW_RIGHT) {
            params.add(identifier());
        }
        nextToken();
        final Function func = new Function(null, params, expression(context));
        return new ConstantNode<>(new APValueFunction(func));
    }

    /**
     * Match boolean.
     *
     * @return the expression node
     */
    private ExpressionNode<Boolean> matchBoolean() {
        final ConstantNode<Boolean> expr = new ConstantNode<>(new APValueBool(
                Boolean.parseBoolean(lookahead.getText())));
        nextToken();
        return expr;
    }

    /**
     * Match number.
     *
     * @return the expression node
     */
    private ExpressionNode<APNumber> matchNumber() {
        final ConstantNode<APNumber> expr = new ConstantNode<>(new APValueNum(
                new APNumber(lookahead.getText())));
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
    @SuppressWarnings("rawtypes")
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
        return new ConstantNode<>(new APValueList(nodes));
    }

    /**
     * Matches a range expression.
     *
     * @param first
     *            the first
     * @param context
     *            the context
     * @return the expression node
     */
    @SuppressWarnings("rawtypes")
    private ExpressionNode matchRange(final ExpressionNode first,
            final Context context) {
        assertNextToken(TokenType.TO);
        nextToken();
        final ExpressionNode second = expression(context);
        return new RangeNode(first, second);
    }

    /**
     * Matches a string literal.
     *
     * @return the expression node
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private ExpressionNode matchString() {
        final String stringMinusQuotes = lookahead.getText().substring(1,
                lookahead.getText().length() - 1);
        final ConstantNode expr = new ConstantNode(new APValueList(
                stringToList(unescapeJavaString(stringMinusQuotes))));
        nextToken();
        return expr;
    }

    /**
     * Matches a char literal.
     *
     * @return the expression node
     */
    @SuppressWarnings("rawtypes")
    private ExpressionNode matchChar() {
        final int CHAR_LENGTH = 1;
        final String charMinusQuotes = unescapeJavaString(lookahead.getText()
                .substring(1, lookahead.getText().length() - 1));

        if (charMinusQuotes.length() == CHAR_LENGTH) {
            final ConstantNode<Character> expr = new ConstantNode<>(
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
                    final StringBuilder code = new StringBuilder(nextChar);
                    i++;
                    if (i < st.length() - 1 && st.charAt(i + 1) >= '0'
                            && st.charAt(i + 1) <= '7') {
                        code.append(st.charAt(i + 1));
                        i++;
                        if (i < st.length() - 1 && st.charAt(i + 1) >= '0'
                                && st.charAt(i + 1) <= '7') {
                            code.append(st.charAt(i + 1));
                            i++;
                        }
                    }
                    sb.append((char) Integer.parseInt(code.toString(), 8));
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
                                new StringBuilder(st.charAt(i + 2))
                                .append(st.charAt(i + 3))
                                .append(st.charAt(i + 4))
                                .append(st.charAt(i + 5)).toString(),
                                16);
                        sb.append(Character.toChars(code));
                        i += 5;
                        continue;
                    default:
                        throw new ParserException("Unsupported escape : \\"
                                + nextChar);
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
    @SuppressWarnings("rawtypes")
    private List<ExpressionNode> stringToList(final String text) {
        final List<ExpressionNode> list = new ArrayList<>();
        for (final Character c : text.toCharArray()) {
            list.add(new ConstantNode<>(new APValueChar(c)));
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
    @SuppressWarnings("rawtypes")
    private ExpressionNode functionParameters(final Context context,
            final ExpressionNode expr) {
        final List<ExpressionNode> parameters = new ArrayList<>();
        nextToken();
        if (lookahead.getType() == TokenType.CLOSE_PARENS) {
            // No params
            final FunctionCallNode node = new FunctionCallNode(expr, parameters);
            nextToken();
            return node;
        }
        parameters.add(expression(context));
        if (lookahead.getType() == TokenType.CLOSE_PARENS) {
            // One parameter
            final FunctionCallNode node = new FunctionCallNode(expr, parameters);
            nextToken();
            return node;
        }

        while (lookahead.getType() != TokenType.CLOSE_PARENS) {
            assertNextToken(TokenType.COMMA);
            nextToken();
            parameters.add(expression(context));
        }
        nextToken();

        return new FunctionCallNode(expr, parameters);
    }

    /**
     * Matches an identifier.
     *
     *
     * @return the variable node
     */
    private VariableNode identifier() {
        final VariableNode variableNode = new VariableNode(lookahead.getText());
        nextToken();
        return variableNode;
    }

    /**
     * Assert next token equals at least one of the parameters, or throw a
     * parser exception.
     *
     * @param types
     *            the types to check against
     * @throws ParserException
     */
    private void assertNextToken(final TokenType... types) {
        final List<TokenType> typesList = Arrays.asList(types);
        if (typesList.contains(lookahead.getType())) {
            return;
        } else {
            final StringBuilder sb = new StringBuilder();
            if (types.length == 1) {
                // 0
                sb.append(types[0]);
            } else if (types.length == 2) {
                // 0 or 1
                sb.append(types[0]).append(" or ").append(types[1]);
            } else {
                // 0, 1, 2... n-1, or n
                for (int i = 0; i < types.length - 1; i++) {
                    sb.append(types[i]).append(", ");
                }
                sb.append(" or ").append(types[types.length - 1]);
            }
            
            throw new ParserException("Expected " + sb.toString() + " but was "
                    + lookahead + " while matching " + Debug.callerMethod(1));
        }
    }
}
