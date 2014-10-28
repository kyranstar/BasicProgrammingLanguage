package parser;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import type.APValue;
import type.APValue.APValueInt;

public abstract class ExpressionNode<T> {

	private final ExpressionNodeType type;

	private final List<ExpressionNode<T>> terms;

	public ExpressionNode(final ExpressionNodeType type,
			final List<ExpressionNode<T>> terms) {
		this.type = type;
		this.terms = terms;
	}

	public ExpressionNodeType getType() {
		return type;
	}

	protected ExpressionNode<T> getTerm(final int i) {
		return terms.get(i);
	}

	@Override
	public String toString() {
		return "" + getClass().getSimpleName() + "<" + terms + ">";
	}

	public abstract APValue<T> getValue();

	public static class ConstantNode extends ExpressionNode<BigDecimal> {

		BigDecimal v;

		public ConstantNode(final BigDecimal bigDecimal) {
			super(ExpressionNodeType.CONSTANT_NODE, null);
			v = bigDecimal;
		}

		@Override
		public String toString() {
			return "" + getClass().getSimpleName() + "<" + v + ">";
		}

		@Override
		public APValue<BigDecimal> getValue() {
			return new APValue.APValueInt(v);
		}

	}

	public static class AdditionNode extends ExpressionNode<BigDecimal> {
		public AdditionNode(final ExpressionNode<BigDecimal> n1,
				final ExpressionNode<BigDecimal> n2) {
			super(ExpressionNodeType.ADDITION_NODE, Arrays.asList(n1, n2));
		}

		@Override
		public APValueInt getValue() {
			final BigDecimal t1 = getTerm(0).getValue().getValue();
			final BigDecimal t2 = getTerm(1).getValue().getValue();

			return new APValue.APValueInt(t1.add(t2));
		}
	}

	public static class SubtractionNode extends ExpressionNode<BigDecimal> {
		public SubtractionNode(final ExpressionNode<BigDecimal> n1,
				final ExpressionNode<BigDecimal> n2) {
			super(ExpressionNodeType.SUBTRACTION_NODE, Arrays.asList(n1, n2));
		}

		@Override
		public APValueInt getValue() {
			final BigDecimal t1 = getTerm(0).getValue().getValue();
			final BigDecimal t2 = getTerm(1).getValue().getValue();

			return new APValue.APValueInt(t1.subtract(t2));
		}

	}

	public static class MultiplicationNode extends ExpressionNode<BigDecimal> {
		public MultiplicationNode(final ExpressionNode<BigDecimal> n1,
				final ExpressionNode<BigDecimal> n2) {
			super(ExpressionNodeType.MULTIPLICATION_NODE, Arrays.asList(n1, n2));
		}

		@Override
		public APValue<BigDecimal> getValue() {
			final BigDecimal t1 = getTerm(0).getValue().getValue();
			final BigDecimal t2 = getTerm(1).getValue().getValue();

			return new APValue.APValueInt(t1.multiply(t2));
		}
	}

	public static class DivisionNode extends ExpressionNode<BigDecimal> {
		public DivisionNode(final ExpressionNode<BigDecimal> n1,
				final ExpressionNode<BigDecimal> n2) {
			super(ExpressionNodeType.DIVISION_NODE, Arrays.asList(n1, n2));
		}

		@Override
		public APValue<BigDecimal> getValue() {
			final BigDecimal t1 = getTerm(0).getValue().getValue();
			final BigDecimal t2 = getTerm(1).getValue().getValue();

			return new APValue.APValueInt(t1.divide(t2));
		}
	}

	public static class ExponentiationNode extends ExpressionNode<BigDecimal> {
		public ExponentiationNode(final ExpressionNode<BigDecimal> n1,
				final ExpressionNode<BigDecimal> n2) {
			super(ExpressionNodeType.EXPONENTIATION_NODE, Arrays.asList(n1, n2));
		}

		@Override
		public APValue<BigDecimal> getValue() {
			final BigDecimal t1 = getTerm(0).getValue().getValue();
			final BigDecimal t2 = getTerm(1).getValue().getValue();

			return new APValue.APValueInt(t1.pow(t2.intValue()));
		}
	}

	public static class VariableNode extends ExpressionNode<BigDecimal> {
		private final String name;

		public VariableNode(final String s) {
			super(ExpressionNodeType.EXPONENTIATION_NODE, null);
			this.name = s;
		}

		@Override
		public APValue<BigDecimal> getValue() {
			// TODO: Use map for variables
			return new APValue.APValueInt(new BigDecimal("-1"));
		}
	}

}
