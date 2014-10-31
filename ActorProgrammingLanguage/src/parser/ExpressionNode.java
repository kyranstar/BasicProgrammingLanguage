/*
 * @author Kyran Adams
 */
package parser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import machine.Context;
import machine.Function;
import type.APValue;
import type.APValue.APValueNum;

/**
 * An ExpressionNode is a expression in the language that evaluates to T.
 *
 * @param <T>
 *            the generic type that this expression returns
 */
public abstract class ExpressionNode<T> {
    
    /** The Constant VOID. */
    public static final ExpressionNode VOID = new ExpressionNode<Void>(null) {
        
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
        final ExpressionNode other = (ExpressionNode) obj;
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
     */
    public static class ConstantNode extends ExpressionNode {
        
        /** The v. */
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
            return ConstantNode.class.getSimpleName() + "<" + v + ">";
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
     * The Class FunctionCallNode. Represents a function call
     */
    public static class FunctionCallNode extends ExpressionNode<BigDecimal> {
        
        /** The function name. */
        private final String name;

        /** The parameters. */
        private final List<ExpressionNode> parameters;
        
        /**
         * Instantiates a new function call node.
         *
         * @param expr
         *            the expr
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
            c.setFunctions(context.getFunctions());

            final Function func = context.getFunction(name);
            
            if (parameters.size() != func.parameters.size()) {
                throw new ParserException("You gave " + parameters.size()
                        + " parameter(s), function " + func.name + " requires "
                        + func.parameters.size() + " parameter(s).");
            }
            
            // Put all parameters in function scope
            for (int i = 0; i < parameters.size(); i++) {
                final ExpressionNode given = parameters.get(i);
                final String name = func.parameters.get(i).name;
                c.putVariable(name, given.getValue(context));
            }
            return func.body.getValue(c);
        }
    }
    
    /**
     * The Class PrintlnNode.
     */
    public static class PrintlnNode extends ExpressionNode {
        
        /** The node. */
        private final ExpressionNode node;
        
        /**
         * Instantiates a new println node.
         *
         * @param node
         *            the node
         */
        public PrintlnNode(final ExpressionNode node) {
            super(null);
            this.node = node;
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context context) {
            context.getOutputStream()
                    .println(node.getValue(context).getValue());
            return APValue.VOID;
        }
        
    }
    
    /**
     * The Class AssignmentNode.
     */
    public static class AssignmentNode extends ExpressionNode {
        
        /** The variable to assign to. */
        private final parser.ExpressionNode.VariableNode variable;

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
            return variable + " = " + expression;
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context context) {
            final APValue expr = this.expression.getValue(context);
            context.putVariable(variable.name, expression.getValue(context));
            return expr;
        }
        
    }
    
    /**
     * The Class IfNode. Represents if else expression
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
                                + getTerm(0).getValue(context).getType());
            }
            if (result) {
                return getTerm(1).getValue(context);
            } else {
                return getTerm(2).getValue(context);
            }
        }
        
    }
    
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
            Comparable termOne, termTwo;
            
            try {
                termOne = (Comparable) getTerm(0).getValue(c).getValue();
                termTwo = (Comparable) getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName() + " on types "
                        + getTerm(0).getValue(c).getType() + " and "
                        + getTerm(1).getValue(c).getType());
            }
            
            return new APValue.APValueBool(termOne.compareTo(termTwo) == 0);
        }

    }

    /**
     * The Class GreaterThanEqualNode.
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
            Comparable termOne, termTwo;
            
            try {
                termOne = (Comparable) getTerm(0).getValue(c).getValue();
                termTwo = (Comparable) getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName() + " on types "
                        + getTerm(0).getValue(c).getType() + " and "
                        + getTerm(1).getValue(c).getType());
            }
            
            return new APValue.APValueBool(termOne.compareTo(termTwo) >= 0);
        }
        
    }
    
    /**
     * The Class LessThanEqualNode.
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
            Comparable termOne, termTwo;
            
            try {
                termOne = (Comparable) getTerm(0).getValue(c).getValue();
                termTwo = (Comparable) getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName() + " on types "
                        + getTerm(0).getValue(c).getType() + " and "
                        + getTerm(1).getValue(c).getType());
            }
            
            return new APValue.APValueBool(termOne.compareTo(termTwo) <= 0);
        }
        
    }
    
    /**
     * The Class GreaterThanNode.
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
            Comparable termOne, termTwo;
            
            try {
                termOne = (Comparable) getTerm(0).getValue(c).getValue();
                termTwo = (Comparable) getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName() + " on types "
                        + getTerm(0).getValue(c).getType() + " and "
                        + getTerm(1).getValue(c).getType());
            }
            
            return new APValue.APValueBool(termOne.compareTo(termTwo) > 0);
        }
        
    }
    
    /**
     * The Class LessThanNode.
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
            Comparable termOne, termTwo;
            
            try {
                termOne = (Comparable) getTerm(0).getValue(c).getValue();
                termTwo = (Comparable) getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName() + " on types "
                        + getTerm(0).getValue(c).getType() + " and "
                        + getTerm(1).getValue(c).getType());
            }
            
            return new APValue.APValueBool(termOne.compareTo(termTwo) < 0);
        }
        
    }
    
    /**
     * The Class AndNode.
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
            boolean termOne, termTwo;
            
            try {
                termOne = getTerm(0).getValue(c).getValue();
                termTwo = getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName() + " on types "
                        + getTerm(0).getValue(c).getType() + " and "
                        + getTerm(1).getValue(c).getType());
            }
            
            return new APValue.APValueBool(termOne && termTwo);
        }
    }
    
    /**
     * The Class OrNode.
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
            boolean termOne, termTwo;
            
            try {
                termOne = getTerm(0).getValue(c).getValue();
                termTwo = getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName()
                        + " on types "
                        + getTerm(0).getValue(c).getValue().getClass()
                                .getSimpleName()
                        + " and "
                        + getTerm(1).getValue(c).getValue().getClass()
                                .getSimpleName());
            }
            
            return new APValue.APValueBool(termOne || termTwo);
        }
        
    }
    
    /**
     * The Class AdditionNode.
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
        public APValueNum getValue(final Context c) {
            final BigDecimal termOne;
            final BigDecimal termTwo;
            try {
                termOne = getTerm(0).getValue(c).getValue();
                termTwo = getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName()
                        + " on types "
                        + getTerm(0).getValue(c).getValue().getClass()
                                .getSimpleName()
                        + " and "
                        + getTerm(1).getValue(c).getValue().getClass()
                                .getSimpleName());
            }
            return new APValue.APValueNum(termOne.add(termTwo));
        }
    }
    
    /**
     * The Class SubtractionNode.
     */
    public static class SubtractionNode extends ExpressionNode<BigDecimal> {

        /**
         * Instantiates a new subtraction node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public SubtractionNode(final ExpressionNode<BigDecimal> firstTerm,
                final ExpressionNode<BigDecimal> secondTerm) {
            super(Arrays.asList(firstTerm, secondTerm));
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValueNum getValue(final Context c) {
            final BigDecimal termOne;
            final BigDecimal termTwo;
            try {
                termOne = getTerm(0).getValue(c).getValue();
                termTwo = getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName()
                        + " on types "
                        + getTerm(0).getValue(c).getValue().getClass()
                                .getSimpleName()
                        + " and "
                        + getTerm(1).getValue(c).getValue().getClass()
                                .getSimpleName());
            }
            return new APValue.APValueNum(termOne.subtract(termTwo));
        }
        
    }
    
    /**
     * The Class MultiplicationNode.
     */
    public static class MultiplicationNode extends ExpressionNode<BigDecimal> {

        /**
         * Instantiates a new multiplication node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public MultiplicationNode(final ExpressionNode<BigDecimal> firstTerm,
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
            final BigDecimal termOne;
            final BigDecimal termTwo;
            try {
                termOne = getTerm(0).getValue(c).getValue();
                termTwo = getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName()
                        + " on types "
                        + getTerm(0).getValue(c).getValue().getClass()
                                .getSimpleName()
                        + " and "
                        + getTerm(1).getValue(c).getValue().getClass()
                                .getSimpleName());
            }
            return new APValue.APValueNum(termOne.multiply(termTwo));
        }
    }
    
    /**
     * The Class DivisionNode.
     */
    public static class DivisionNode extends ExpressionNode<BigDecimal> {

        /** The Constant DECIMALS. */
        public static final int DECIMALS = 50;
        
        /**
         * Instantiates a new division node.
         *
         * @param firstTerm
         *            the firstTerm
         * @param secondTerm
         *            the secondTerm
         */
        public DivisionNode(final ExpressionNode<BigDecimal> firstTerm,
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
            final BigDecimal termOne;
            final BigDecimal termTwo;
            try {
                termOne = getTerm(0).getValue(c).getValue();
                termTwo = getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName()
                        + " on types "
                        + getTerm(0).getValue(c).getValue().getClass()
                                .getSimpleName()
                        + " and "
                        + getTerm(1).getValue(c).getValue().getClass()
                                .getSimpleName());
            }
            return new APValue.APValueNum(termOne.divide(termTwo, DECIMALS,
                    RoundingMode.HALF_UP));
        }
    }
    
    /**
     * The Class ExponentiationNode.
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
            final BigDecimal termOne;
            final BigDecimal termTwo;
            try {
                termOne = getTerm(0).getValue(c).getValue();
                termTwo = getTerm(1).getValue(c).getValue();
            } catch (final ClassCastException e) {
                throw new ParserException("Cannot do operation "
                        + getClass().getSimpleName()
                        + " on types "
                        + getTerm(0).getValue(c).getValue().getClass()
                                .getSimpleName()
                        + " and "
                        + getTerm(1).getValue(c).getValue().getClass()
                                .getSimpleName());
            }
            return new APValue.APValueNum(termOne.pow(termTwo.intValue()));
        }
    }
    
    /**
     * The Class VariableNode.
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
            return VariableNode.class.getSimpleName() + "<" + name + ">";
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see parser.ExpressionNode#getValue(machine.Context)
         */
        @Override
        public APValue getValue(final Context c) {
            return c.getVariable(name);
        }
        
        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }
    }
    
}
