/*
 * @author Kyran Adams
 */
package parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import machine.Context;
import machine.Function;
import type.APValue;
import type.APValue.Operators;
import type.APValueData;
import type.APValueFunction;
import type.APValueList;
import type.APValueNum;

/**
 * An ExpressionNode is a expression in the language that evaluates to T.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 * @param <T>
 *            the generic type
 */
public abstract class ExpressionNode<T> {

    /** The Constant VOID. */
    public static final ExpressionNode<Void> VOID = new ExpressionNode<Void>(
            null) {

        @Override
        public APValue<Void> getValue(final Context context) {
            return APValue.VOID;
        }
    };

    /** The terms of an expression. In "3+4" the terms are 3 and 4. */
    private final List<ExpressionNode<T>> terms;

    /**
     * Instantiates a new expression node.
     *
     * @param terms
     *            the terms
     */
    public ExpressionNode(final List<ExpressionNode<T>> terms) {
        this.terms = terms;
    }

    /**
     * Gets the term.
     *
     * @param i
     *            the index
     *
     * @return the term
     */
    protected ExpressionNode<T> getTerm(final int i) {
        return terms.get(i);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "<" + terms + ">";
    }

    /**
     * Gets the value of the expression in the context.
     *
     * @param context
     *            the context
     *
     * @return the expressions value in context
     */
    public abstract APValue<T> getValue(Context context);

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (terms == null ? 0 : terms.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExpressionNode<?> other = (ExpressionNode<?>) obj;
        if (terms == null) {
            if (other.terms != null) {
                return false;
            }
        } else if (!terms.equals(other.terms)) {
            return false;
        }
        return true;
    }

    /**
     * The Class ConstantNode. Represents a constant, for example "true" or "3"
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class ConstantNode extends ExpressionNode {

        /** The value. */
        APValue v;

        /**
         * Instantiates a new constant node.
         *
         * @param apValue
         *            the ap value
         */
        public ConstantNode(final APValue apValue) {
            super(null);
            v = apValue;
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#toString()
         */
        @Override
        public String toString() {
            return v.toString();
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            return v;
        }

    }

    /**
     * The Class FunctionCallNode. Represents a function call.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class FunctionCallNode extends ExpressionNode {

        /** The function name. */
        private final String name;

        /** The parameters. */
        private final List<ExpressionNode> parameters;

        /**
         * Instantiates a new function call node.
         *
         * @param name
         *            the name
         * @param parameters
         *            the parameters
         */
        public FunctionCallNode(final String name,
                final List<ExpressionNode> parameters) {
            super(null);
            this.name = name;
            this.parameters = parameters;
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#toString()
         */
        @Override
        public String toString() {
            return name + "(" + parameters + ")";
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context context) {
            // We don't want a child scope because then it can affect variables
            // outside of scope.
            final Context c = new Context(context.getOutputStream());
            // Add all functions of outer scope, but we have to add this
            // function individually to avoid stackoverflow.
            final Map<String, APValue> everythingButCurrentFunction = new HashMap<>(
                    context.getVariables());
            everythingButCurrentFunction.remove(name);
            
            c.setVariables(everythingButCurrentFunction);
            
            final APValue valueFunction = context.getFunction(name);
            final Function func = (Function) valueFunction.getValue();
            // give it access to itself
            c.putFunction(func);

            if (parameters.size() != func.parameters.size()) {
                throw new ParserException("You gave " + parameters.size()
                        + " parameter(s), function " + func.name + " requires "
                        + func.parameters.size() + " parameter(s).");
            }

            // Put all parameters in function scope
            for (int i = 0; i < parameters.size(); i++) {
                final ExpressionNode given = parameters.get(i);
                final String name = func.parameters.get(i).name;
                c.putFunction(name, given.getValue(context));
            }
            
            final APValue returnVal = func.body.getValue(c);
            // The reason we have to simplify a list before we return it is if
            // the list uses the parameters. This means that if you return
            // [a,a,a], the caller cannot simplify it because it has no access
            // to parameters anymore.
            if (returnVal instanceof APValueList) {
                final List<ExpressionNode> nodes = (List<ExpressionNode>) returnVal
                        .getValue();
                final List<ExpressionNode> simplifiedNodes = new ArrayList<>();
                for (final ExpressionNode node : nodes) {
                    simplifiedNodes.add(new ConstantNode(node.getValue(c)));
                }
                return new APValueList(simplifiedNodes);
            }

            return returnVal;
        }
    }

    /**
     * The Class AssignmentNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class AssignmentNode extends ExpressionNode {

        /** The variable to assign to. */
        private final VariableNode variable;

        /** The expression to assign to the variable. */
        private final ExpressionNode expression;

        /**
         * Instantiates a new assignment node.
         *
         * @param expr
         *            the variable to assign to
         * @param assigned
         *            the assigned expression
         */
        public AssignmentNode(final VariableNode expr,
                final ExpressionNode<BigDecimal> assigned) {
            super(null);
            variable = expr;
            this.expression = assigned;
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#toString()
         */
        @Override
        public String toString() {
            return getVariable() + " = " + getExpression();
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context context) {
            final APValue expr = this.getExpression().getValue(context);
            context.putFunction(getVariable().name,
                    getExpression().getValue(context));
            return expr;
        }

        public VariableNode getVariable() {
            return variable;
        }

        public ExpressionNode getExpression() {
            return expression;
        }

    }

    /**
     * The Class FieldAssignmentNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class FieldAssignmentNode extends ExpressionNode {

        /** The variable to assign to. */
        private final VariableNode variable;

        /** The field in the variable to assign to. */
        private final VariableNode field;

        /** The expression to assign to the variable. */
        private final ExpressionNode expression;

        /**
         * Instantiates a new assignment node.
         *
         * @param expr
         *            the variable to assign to
         * @param assigned
         *            the assigned expression
         * @param assigned2
         */
        public FieldAssignmentNode(final VariableNode expr,
                final VariableNode field, final ExpressionNode assigned) {
            super(null);
            this.field = field;
            variable = expr;
            this.expression = assigned;
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#toString()
         */
        @Override
        public String toString() {
            return getVariable() + "." + field + " = " + getExpression();
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context context) {
            final APValue lh = context.getVariables().get(variable.getName());
            if (!(lh instanceof APValueData)) {
                throw new ParserException("Can't access field of non data type");
            }
            ((APValueData) lh).getValue().fields.put(field.getName(),
                    new ConstantNode(expression.getValue(context)));
            return APValue.VOID;
        }

        public VariableNode getVariable() {
            return variable;
        }

        public ExpressionNode getExpression() {
            return expression;
        }

    }

    /**
     * The Class IfNode. Represents if then else expression
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class IfNode extends ExpressionNode {

        /**
         * Instantiates a new if node.
         *
         * @param ifExpr
         *            the if expr
         * @param thenExpr
         *            the then expr
         * @param elseExpr
         *            the else expr
         */
        public IfNode(final ExpressionNode<Boolean> ifExpr,
                final ExpressionNode thenExpr, final ExpressionNode elseExpr) {
            super(Arrays.asList(ifExpr, thenExpr, elseExpr));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context context) {
            boolean result;
            try {
                result = (boolean) getTerm(0).getValue(context).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException(
                        "If expression requires a boolean expression, was "
                                + getTerm(0).getValue(context).getClass());
            }
            if (result) {
                return getTerm(1).getValue(context);
            } else {
                return getTerm(2).getValue(context);
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#toString()
         */
        @Override
        public String toString() {
            return "if (" + getTerm(0) + ")" + getTerm(1) + " else "
                    + getTerm(2);
        }
    }
    
    /**
     * The Class AssignmentNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class LambdaNode extends ExpressionNode {

        /** The variable to assign to. */
        private final Function func;

        /**
         * Instantiates a new assignment node.
         *
         * @param func
         *            the func
         */
        public LambdaNode(final Function func) {
            super(null);
            this.func = func;
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#toString()
         */
        @Override
        public String toString() {
            return func.toString();
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context context) {
            return new APValueFunction(func);
        }

    }
    
    /**
     * The Class EqualNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class EqualNode extends ExpressionNode {

        /**
         * Instantiates a new equal (comparison) node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public EqualNode(final ExpressionNode<? extends Comparable> firstTerm,
                final ExpressionNode<? extends Comparable> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.EQUAL, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " = " + getTerm(1) + ")";
        }
    }

    /**
     * The Class GreaterThanEqualNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class GreaterThanEqualNode extends ExpressionNode {

        /**
         * Instantiates a new greater than equal node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public GreaterThanEqualNode(
                final ExpressionNode<? extends Comparable> firstTerm,
                final ExpressionNode<? extends Comparable> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.GREATER_EQUAL, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " >= " + getTerm(1) + ")";
        }
    }

    /**
     * The Class LessThanEqualNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class LessThanEqualNode extends ExpressionNode {

        /**
         * Instantiates a new less than equal node.
         *
         * @param firstTerm
         *            the first term
         * @param secondTerm
         *            the second term
         */
        public LessThanEqualNode(
                final ExpressionNode<? extends Comparable> firstTerm,
                final ExpressionNode<? extends Comparable> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.LESS_EQUAL, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " <= " + getTerm(1) + ")";
        }
    }

    /**
     * The Class GreaterThanNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class GreaterThanNode extends ExpressionNode {

        /**
         * Instantiates a new greater than node.
         *
         * @param firstTerm
         *            the first term
         * @param secondTerm
         *            the second term
         */
        public GreaterThanNode(
                final ExpressionNode<? extends Comparable> firstTerm,
                final ExpressionNode<? extends Comparable> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.GREATER, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " > " + getTerm(1) + ")";
        }
    }

    /**
     * The Class LessThanNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class LessThanNode extends ExpressionNode {

        /**
         * Instantiates a new less than node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public LessThanNode(
                final ExpressionNode<? extends Comparable> firstTerm,
                final ExpressionNode<? extends Comparable> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.LESS, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " < " + getTerm(1) + ")";
        }
    }

    /**
     * The Class ListIndexNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class ListIndexNode extends ExpressionNode {

        /** The list. */
        private final ExpressionNode list;
        
        /** The index. */
        private final ExpressionNode index;

        /**
         * Instantiates a new list index node.
         *
         * @param expr
         *            the expr
         * @param insideParens
         *            the inside parens
         */
        public ListIndexNode(final ExpressionNode expr,
                final ExpressionNode insideParens) {
            super(null);
            this.list = expr;
            this.index = insideParens;
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context context) {
            final APValueList apValueList = (APValueList) this.list
                    .getValue(context);
            final int indexValue = ((BigDecimal) index.getValue(context)
                    .getValue()).intValueExact();
            final ExpressionNode expressionNode = (ExpressionNode) apValueList
                    .getValue().get(indexValue);
            return expressionNode.getValue(context);
        }
        
        @Override
        public String toString() {
            return "(" + list + "{" + index + "})";
        }
    }

    /**
     * The Class FieldAccessNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class FieldAccessNode extends ExpressionNode {

        /** The data structure. */
        private final ExpressionNode dataStructure;
        
        /** The field in the data structure. */
        private final VariableNode field;

        /**
         * Instantiates a new list index node.
         *
         * @param expr
         *            the data structure
         * @param field
         *            the field being accessed
         */
        public FieldAccessNode(final ExpressionNode expr,
                final VariableNode field) {
            super(null);
            this.dataStructure = expr;
            this.field = field;
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context context) {
            final APValueData apValueData = (APValueData) this.dataStructure
                    .getValue(context);
            return apValueData.getValue().fields.get(field.name).getValue(
                    context);
        }
        
        @Override
        public String toString() {
            return "(" + dataStructure + "." + field + ")";
        }
    }
    
    /**
     * The Class AndNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class AndNode extends ExpressionNode<Boolean> {

        /**
         * Instantiates a new and node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public AndNode(final ExpressionNode<Boolean> firstTerm,
                final ExpressionNode<Boolean> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.AND, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " && " + getTerm(1) + ")";
        }
    }

    /**
     * The Class OrNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class OrNode extends ExpressionNode<Boolean> {

        /**
         * Instantiates a new or node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public OrNode(final ExpressionNode<Boolean> firstTerm,
                final ExpressionNode<Boolean> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.OR, termTwo);

        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " || " + getTerm(1) + ")";
        }
    }

    /**
     * The Class AdditionNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class AdditionNode extends ExpressionNode<BigDecimal> {

        /**
         * Instantiates a new addition node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public AdditionNode(final ExpressionNode<BigDecimal> firstTerm,
                final ExpressionNode<BigDecimal> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.ADD, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " + " + getTerm(1) + ")";
        }
    }

    /**
     * The Class SubtractionNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class SubtractionNode extends ExpressionNode {

        /**
         * Instantiates a new subtraction node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public SubtractionNode(final ExpressionNode firstTerm,
                final ExpressionNode secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.SUBTRACT, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " - " + getTerm(1) + ")";
        }
    }

    /**
     * The Class MultiplicationNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class MultiplicationNode extends ExpressionNode {

        /**
         * Instantiates a new multiplication node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public MultiplicationNode(final ExpressionNode firstTerm,
                final ExpressionNode secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.MULTIPLY, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " * " + getTerm(1) + ")";
        }
    }
    
    /**
     * The Class ModNode.
     */
    public static class ModNode extends ExpressionNode {

        /**
         * Instantiates a new modulo node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public ModNode(final ExpressionNode firstTerm,
                final ExpressionNode secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.MOD, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " % " + getTerm(1) + ")";
        }
    }

    /**
     * The Class DivisionNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class DivisionNode extends ExpressionNode {

        /**
         * Instantiates a new division node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public DivisionNode(final ExpressionNode firstTerm,
                final ExpressionNode secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.DIVIDE, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " / " + getTerm(1) + ")";
        }
    }

    /**
     * The Class ExponentiationNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class ExponentiationNode extends ExpressionNode<BigDecimal> {

        /**
         * Instantiates a new exponentiation node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public ExponentiationNode(final ExpressionNode<BigDecimal> firstTerm,
                final ExpressionNode<BigDecimal> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue<BigDecimal> getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            return termOne.callMethod(Operators.POWER, termTwo);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " ^ " + getTerm(1) + ")";
        }
    }
    
    /**
     * The Class RangeNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class RangeNode extends ExpressionNode {

        /**
         * Instantiates a new exponentiation node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public RangeNode(final ExpressionNode firstTerm,
                final ExpressionNode secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            final APValue termOne = getTerm(0).getValue(c);
            final APValue termTwo = getTerm(1).getValue(c);

            final List<ExpressionNode> nodes = new ArrayList<>();
            if (termOne instanceof APValueNum && termTwo instanceof APValueNum) {
                BigDecimal first = (BigDecimal) termOne.getValue();
                final BigDecimal second = (BigDecimal) termTwo.getValue();
                
                for (; first.compareTo(second) <= 0; first = first
                        .add(BigDecimal.ONE)) {
                    nodes.add(new ConstantNode(new APValueNum(first)));
                }
            } else {
                throw new ParserException("Cannot create range of types "
                        + termOne.getClass() + " and " + termTwo.getClass());
            }

            return new APValueList(nodes);
        }
        
        @Override
        public String toString() {
            return "(" + getTerm(0) + " to " + getTerm(1) + ")";
        }
    }

    /**
     * The Class VariableNode.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */
    public static class VariableNode extends ExpressionNode {

        /** The name. */
        private final String name;

        /**
         * Instantiates a new variable node.
         *
         * @param s
         *            the s
         */
        
        public VariableNode(final String s) {
            super(null);
            this.name = s;
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#toString()
         */
        @Override
        public String toString() {
            return name;
        }

        /*
         * (non-Javadoc)
         *
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            return c.getFunction(name);
        }

        /**
         * Gets the name.
         *
         *
         * @return the name
         */
        public String getName() {
            return name;
        }
    }

}
