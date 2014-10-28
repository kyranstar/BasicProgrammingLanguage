package machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import parser.ExpressionNode;

public class Context {
	private Map<String, ExpressionNode> context;
	private final Map<String, Function> functions;
	Optional<Context> parent;

	public Context() {
		setContext(new HashMap<>());
		functions = new HashMap<>();
		parent = Optional.empty();
	}

	public Context(final Context parent) {
		setContext(new HashMap<>());
		functions = new HashMap<>();
		this.parent = Optional.of(parent);
	}

	public void putVariable(final String s, final ExpressionNode en) {
		// If this context has a parent
		if (parent.isPresent()) {
			// If that parent has the variable we are assigning
			if (parent.get().getVariable(s) != null) {
				// Put it to the parent instead
				parent.get().putVariable(s, en);
				return;
			}
		}
		getContext().put(s, en);
	}

	public void putFunction(final String s, final Function func) {
		// If this context has a parent
		if (parent.isPresent()) {
			// If that parent has the variable we are assigning
			if (parent.get().getFunction(s) != null) {
				// Put it to the parent instead
				parent.get().putFunction(s, func);
				return;
			}
		}
		functions.put(s, func);
	}

	public ExpressionNode getVariable(final String s) {
		final ExpressionNode node = getContext().get(s);
		if (node == null) {
			if (parent.isPresent())
				return parent.get().getVariable(s);
			else
				throw new ContextException("Could not find value for <" + s
						+ ">");
		}
		return node;

	}

	public Function getFunction(final String s) {
		final Function node = functions.get(s);
		if (node == null) {
			if (parent.isPresent())
				return parent.get().getFunction(s);
			else
				throw new ContextException(
						"Could not find function with name <" + s + ">");
		}
		return node;

	}

	public Context getChild() {
		return new Context(this);
	}

	public Map<String, ExpressionNode> getContext() {
		return context;
	}

	public void setContext(final Map<String, ExpressionNode> context) {
		this.context = context;
	}
}
