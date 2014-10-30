package parser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import machine.Context;
import machine.Function;
import type.APValue;
import type.APValue.APValueNum;

public abstract class ExpressionNode<T> {

	public static final ExpressionNode VOID = new ExpressionNode<Void>(null) {

		@Override
		public APValue<Void> getValue(final Context context) {
			return APValue.VOID;
		}
	};

	private final List<ExpressionNode<T>> terms;

	public ExpressionNode(final List<ExpressionNode<T>> terms) {
		this.terms = terms;
	}

	protected ExpressionNode<T> getTerm(final int i) {
		return terms.get(i);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "<" + terms + ">";
	}

	public abstract APValue<T> getValue(Context context);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (terms == null ? 0 : terms.hashCode());
		return result;
	}
	
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
	
	public static class ConstantNode extends ExpressionNode {

		APValue v;

		public ConstantNode(final APValue apValue) {
			super(null);
			v = apValue;
		}

		@Override
		public String toString() {
			return ConstantNode.class.getSimpleName() + "<" + v + ">";
		}

		@Override
		public APValue getValue(final Context c) {
			return v;
		}

	}

	public static class FunctionDefNode extends ExpressionNode<Void> {

		private final Function func;

		public FunctionDefNode(final String name, final List<ExpressionNode.VariableNode> parameters, final ExpressionNode body) {
			super(null);
			this.func = new Function(name, parameters, body);
		}

		@Override
		public APValue<Void> getValue(final Context context) {
			context.putFunction(func.name, func);
			return APValue.VOID;
		}

	}

	public static class FunctionCallNode extends ExpressionNode<BigDecimal> {

		private final ExpressionNode.VariableNode variable;
		private final List<ExpressionNode> parameters;

		public FunctionCallNode(final VariableNode expr, final List<ExpressionNode> parameters) {
			super(null);
			variable = expr;
			this.parameters = parameters;
		}

		@Override
		public String toString() {
			return variable + "(" + parameters + ")";
		}

		@Override
		public APValue<BigDecimal> getValue(final Context context) {
			// We don't want a child scope because then it can affect variables
			// outside of scope.
			final Context c = new Context(context.getOutputStream());
			final Function func = context.getFunction(variable.name);

			if (parameters.size() != func.parameters.size()) {
				throw new ParserException("You gave " + parameters.size() + " parameter(s), function " + func.name + " requires " + func.parameters.size() + " parameter(s).");
			}

			// Put all parameters in function scope
			for (int i = 0; i < parameters.size(); i++) {
				final ExpressionNode given = parameters.get(i);
				final String name = func.parameters.get(i).name;
				c.putVariable(name, given);
			}
			return func.body.getValue(c);
		}
	}

	public static class PrintlnNode extends ExpressionNode {

		private final ExpressionNode node;

		public PrintlnNode(final ExpressionNode node) {
			super(null);
			this.node = node;
		}

		@Override
		public APValue getValue(final Context context) {
			context.getOutputStream().println(node.getValue(context).getValue());
			return APValue.VOID;
		}

	}

	public static class AssignmentNode extends ExpressionNode {

		private final parser.ExpressionNode.VariableNode variable;
		private final ExpressionNode expression;

		public AssignmentNode(final VariableNode expr, final ExpressionNode<BigDecimal> assigned) {
			super(null);
			variable = expr;
			this.expression = assigned;
		}

		@Override
		public String toString() {
			return variable + " = " + expression;
		}

		@Override
		public APValue<BigDecimal> getValue(final Context context) {
			final APValue<BigDecimal> expr = this.expression.getValue(context);
			context.putVariable(variable.name, expression);
			return expr;
		}

	}

	public static class IfNode extends ExpressionNode {

		public IfNode(final ExpressionNode<Boolean> ifExpr, final ExpressionNode thenExpr, final ExpressionNode elseExpr) {
			super(Arrays.asList(ifExpr, thenExpr, elseExpr));
		}

		@Override
		public APValue getValue(final Context context) {
			boolean result;
			try {
				result = (boolean) getTerm(0).getValue(context).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("If expression requires a boolean expression, was " + getTerm(0).getValue(context).getType());
			}
			if (result) {
				return getTerm(1).getValue(context);
			} else {
				return getTerm(2).getValue(context);
			}
		}

	}

	public static class GreaterThanEqualNode extends ExpressionNode<BigDecimal> {
		public GreaterThanEqualNode(final ExpressionNode<BigDecimal> n1, final ExpressionNode<BigDecimal> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValue getValue(final Context c) {
			BigDecimal t1, t2;

			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getType() + " and "
						+ getTerm(1).getValue(c).getType());
			}

			return new APValue.APValueBool(t1.compareTo(t2) >= 0);
		}

	}

	public static class LessThanEqualNode extends ExpressionNode<BigDecimal> {
		public LessThanEqualNode(final ExpressionNode<BigDecimal> n1, final ExpressionNode<BigDecimal> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValue getValue(final Context c) {
			BigDecimal t1, t2;

			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getType() + " and "
						+ getTerm(1).getValue(c).getType());
			}

			return new APValue.APValueBool(t1.compareTo(t2) <= 0);
		}

	}

	public static class GreaterThanNode extends ExpressionNode<BigDecimal> {
		public GreaterThanNode(final ExpressionNode<BigDecimal> n1, final ExpressionNode<BigDecimal> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValue getValue(final Context c) {
			BigDecimal t1, t2;

			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getType() + " and "
						+ getTerm(1).getValue(c).getType());
			}

			return new APValue.APValueBool(t1.compareTo(t2) > 0);
		}

	}

	public static class LessThanNode extends ExpressionNode<BigDecimal> {
		public LessThanNode(final ExpressionNode<BigDecimal> n1, final ExpressionNode<BigDecimal> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValue getValue(final Context c) {
			BigDecimal t1, t2;

			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getType() + " and "
						+ getTerm(1).getValue(c).getType());
			}

			return new APValue.APValueBool(t1.compareTo(t2) < 0);
		}

	}

	public static class AndNode extends ExpressionNode<Boolean> {

		public AndNode(final ExpressionNode<Boolean> n1, final ExpressionNode<Boolean> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValue getValue(final Context c) {
			boolean t1, t2;

			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getType() + " and "
						+ getTerm(1).getValue(c).getType());
			}

			return new APValue.APValueBool(t1 && t2);
		}
	}

	public static class OrNode extends ExpressionNode<Boolean> {

		public OrNode(final ExpressionNode<Boolean> n1, final ExpressionNode<Boolean> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValue getValue(final Context c) {
			boolean t1, t2;

			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getValue().getClass().getSimpleName()
						+ " and " + getTerm(1).getValue(c).getValue().getClass().getSimpleName());
			}

			return new APValue.APValueBool(t1 || t2);
		}

	}

	public static class AdditionNode extends ExpressionNode<BigDecimal> {
		public AdditionNode(final ExpressionNode<BigDecimal> n1, final ExpressionNode<BigDecimal> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValueNum getValue(final Context c) {
			final BigDecimal t1;
			final BigDecimal t2;
			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getValue().getClass().getSimpleName()
						+ " and " + getTerm(1).getValue(c).getValue().getClass().getSimpleName());
			}
			return new APValue.APValueNum(t1.add(t2));
		}
	}

	public static class SubtractionNode extends ExpressionNode<BigDecimal> {
		public SubtractionNode(final ExpressionNode<BigDecimal> n1, final ExpressionNode<BigDecimal> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValueNum getValue(final Context c) {
			final BigDecimal t1;
			final BigDecimal t2;
			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getValue().getClass().getSimpleName()
						+ " and " + getTerm(1).getValue(c).getValue().getClass().getSimpleName());
			}
			return new APValue.APValueNum(t1.subtract(t2));
		}

	}

	public static class MultiplicationNode extends ExpressionNode<BigDecimal> {
		public MultiplicationNode(final ExpressionNode<BigDecimal> n1, final ExpressionNode<BigDecimal> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValue<BigDecimal> getValue(final Context c) {
			final BigDecimal t1;
			final BigDecimal t2;
			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getValue().getClass().getSimpleName()
						+ " and " + getTerm(1).getValue(c).getValue().getClass().getSimpleName());
			}
			return new APValue.APValueNum(t1.multiply(t2));
		}
	}

	public static class DivisionNode extends ExpressionNode<BigDecimal> {
		public static final int DECIMALS = 50;

		public DivisionNode(final ExpressionNode<BigDecimal> n1, final ExpressionNode<BigDecimal> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValue<BigDecimal> getValue(final Context c) {
			final BigDecimal t1;
			final BigDecimal t2;
			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getValue().getClass().getSimpleName()
						+ " and " + getTerm(1).getValue(c).getValue().getClass().getSimpleName());
			}
			return new APValue.APValueNum(t1.divide(t2, DECIMALS, RoundingMode.HALF_UP));
		}
	}

	public static class ExponentiationNode extends ExpressionNode<BigDecimal> {
		public ExponentiationNode(final ExpressionNode<BigDecimal> n1, final ExpressionNode<BigDecimal> n2) {
			super(Arrays.asList(n1, n2));
		}

		@Override
		public APValue<BigDecimal> getValue(final Context c) {
			final BigDecimal t1;
			final BigDecimal t2;
			try {
				t1 = getTerm(0).getValue(c).getValue();
				t2 = getTerm(1).getValue(c).getValue();
			} catch (final ClassCastException e) {
				throw new ParserException("Cannot do operation " + getClass().getSimpleName() + " on types " + getTerm(0).getValue(c).getValue().getClass().getSimpleName()
						+ " and " + getTerm(1).getValue(c).getValue().getClass().getSimpleName());
			}
			return new APValue.APValueNum(t1.pow(t2.intValue()));
		}
	}

	public static class VariableNode extends ExpressionNode {
		private final String name;

		public VariableNode(final String s) {
			super(null);
			this.name = s;
		}

		@Override
		public String toString() {
			return VariableNode.class.getSimpleName() + "<" + name + ">";
		}

		@Override
		public APValue getValue(final Context c) {
			// TODO: Use map for variables
			return c.getVariable(name).getValue(c);
		}

		public String getName() {
			return name;
		}
	}

}
