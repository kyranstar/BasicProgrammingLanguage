package type;

import java.math.BigDecimal;

public abstract class APValue<T> {
	// public static final List<APValue> declaredTypes = new
	// ArrayList<APValue>() {
	// {
	// add(new APValueInt());
	// }
	// };

	public static final APValue<Void> VOID = new APValue<Void>() {
		@Override
		public String toString() {
			return "VOID";
		}
		
		@Override
		public Class<Void> getType() {
			return Void.class;
		}

		@Override
		public APValue callMethod(final Methods s, final APValue arg) {

			throw new MismatchedMethodException("Can't call method " + s + " on type void!");
		}
	};
	String type;
	private T value;

	public static class APValueNum extends APValue<BigDecimal> {
		private static final Class<BigDecimal> TYPE = BigDecimal.class;
		
		public APValueNum(final BigDecimal expressionNode) {
			this.setValue(expressionNode);
		}

		@Override
		public String toString() {
			return "APValueNum<" + getValue() + ">";
		}
		
		@Override
		public Class<BigDecimal> getType() {
			return TYPE;
		}

		@Override
		public APValue callMethod(final Methods s, final APValue arg) {
			if (!arg.getType().equals(TYPE))
				throw new MismatchedMethodException(s + " must take two numerical types. Was " + TYPE + " and " + arg.getType());

			switch (s) {
				case MULTIPLY:
					return new APValueNum(getValue().multiply((BigDecimal) arg.getValue()));
				case DIVIDE:
					return new APValueNum(getValue().divide((BigDecimal) arg.getValue()));
				case ADD:
					return new APValueNum(getValue().add((BigDecimal) arg.getValue()));
				case SUBTRACT:
					return new APValueNum(getValue().subtract((BigDecimal) arg.getValue()));
				case POWER:
					return new APValueNum(getValue().pow(((BigDecimal) arg.getValue()).intValue()));
			}
			throw new MismatchedMethodException("Can't call method " + s + " on type " + TYPE + " and " + arg.getType());
		}
	}

	public static class APValueBool extends APValue<Boolean> {
		private static final Class<Boolean> TYPE = Boolean.class;

		public APValueBool(final Boolean expressionNode) {
			this.setValue(expressionNode);
		}

		@Override
		public String toString() {
			return "APValueBool<" + getValue() + ">";
		}
		
		@Override
		public Class<Boolean> getType() {
			return TYPE;
		}

		@Override
		public APValue callMethod(final Methods method, final APValue arg) {
			if (!arg.getType().equals(TYPE))
				throw new MismatchedMethodException(method + " must take two bool types. Was " + TYPE + " and " + arg.getType());

			switch (method) {
				case AND:
					return new APValueBool(getValue() && ((APValueBool) arg).getValue());
				case OR:
					return new APValueBool(getValue() || ((APValueBool) arg).getValue());
			}
			throw new MismatchedMethodException("Can't call method " + method + " on type bool!");
		}
	}
	
	public static enum Methods {
		// Number operators
		MULTIPLY,
		DIVIDE,
		SUBTRACT,
		ADD,
		POWER,
		// Boolean operators
		AND,
		OR,
	}
	
	public abstract Class<T> getType();

	public T getValue() {
		return value;
	}

	public abstract APValue callMethod(Methods s, APValue arg);

	public void setValue(final T value) {
		this.value = value;
	}
}
