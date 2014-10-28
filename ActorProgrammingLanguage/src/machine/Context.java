package machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import parser.ExpressionNode;

public class Context {
	private Map<String, ExpressionNode> context;
	Optional<Context> parent;

	public Context() {
		setContext(new HashMap<>());
		parent = Optional.empty();
	}

	public Context(final Context parent) {
		setContext(new HashMap<>());
		this.parent = Optional.of(parent);
	}

	public void put(final String s, final ExpressionNode en) {
		// If this context has a parent
		if (parent.isPresent()) {
			// If that parent has the variable we are assigning
			if (parent.get().get(s) != null) {
				// Put it to the parent instead
				parent.get().put(s, en);
				return;
			}
		}
		getContext().put(s, en);
	}

	public ExpressionNode get(final String s) {
		final ExpressionNode node = getContext().get(s);
		if (node == null) {
			if (parent.isPresent())
				return parent.get().get(s);
			else
				throw new ContextException("Could not find value for <" + s
						+ ">");
		}
		return node;

	}

	public Context getChild() {
		return new Context(this);
	}

	public Map<String, ExpressionNode> getContext() {
		return context;
	}

	public void setContext(Map<String, ExpressionNode> context) {
		this.context = context;
	}
}
