package type;

import java.math.BigDecimal;

public abstract class APValue<T> {
	// public static final List<APValue> declaredTypes = new
	// ArrayList<APValue>() {
	// {
	// add(new APValueInt());
	// }
	// };

	String type;
	private T value;

	public static class APValueInt extends APValue<BigDecimal> {
		public APValueInt(final BigDecimal expressionNode) {
			this.setValue(expressionNode);
		}

		@Override
		public String toString() {
			return "APValueInt<" + getValue() + ">";
		}

	}

	public T getValue() {
		return value;
	}

	public void setValue(final T value) {
		this.value = value;
	}
}
