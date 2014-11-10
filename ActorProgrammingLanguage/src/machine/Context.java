/*
 * @author Kyran Adams
 */
package machine;

import interpreter.library.LibraryFunction;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import type.APValue;
import type.APValueFunction;

/**
 * The Class Context stores variable and function mappings.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Context {
    
    /** The variable mapping. */
    private Map<FunctionSignature, APValue> variables;

    /** The parent. */
    Optional<Context> parent;
    
    /** The output stream. */
    private PrintStream outputStream;
    
    /**
     * Instantiates a new context with a given print stream.
     *
     * @param p
     *            the p
     */
    public Context(final PrintStream p) {
        setVariables(new HashMap<>());
        parent = Optional.empty();
        outputStream = p;
        
        LibraryFunction.applyLibraryFunctions(this);
    }
    
    /**
     * Instantiates a new context with a given parent.
     *
     * @param parent
     *            the parent
     */
    public Context(final Context parent) {
        setVariables(new HashMap<>());
        this.parent = Optional.of(parent);
        outputStream = parent.getOutputStream();
    }
    
    /**
     * Put variable.
     *
     * @param name
     *            the s
     * @param value
     *            the en
     */
    public void putFunction(final FunctionSignature name, final APValue value) {
        // If this context has a parent
        if (parent.isPresent()) {
            // If that parent has the variable we are assigning
            if (parent.get().getFunction(name) != null) {
                // Put it to the parent instead
                parent.get().putFunction(name, value);
                return;
            }
        }
        getVariables().put(name, value);
    }

    /**
     * Gets the variable with a given name.
     *
     * @param functionSignature
     *            the s
     *
     * @return the variable
     */
    public APValue getFunction(final FunctionSignature functionSignature) {
        APValue node = getVariables().get(functionSignature);
        if (node == null) {
            if (parent.isPresent()) {
                node = parent.get().getFunction(functionSignature);
            } else {
                throw new ContextException("Could not find value for <"
                        + functionSignature + ">");
            }
        }
        return node;
        
    }
    
    /**
     * Gets the child context.
     *
     *
     * @return the child context
     */
    public Context getChild() {
        return new Context(this);
    }
    
    /**
     * Gets the variables.
     *
     *
     * @return the variables
     */
    public Map<FunctionSignature, APValue> getVariables() {
        return variables;
    }
    
    /**
     * Sets the variable map.
     *
     * @param variables
     *            the variable map
     */
    public void setVariables(final Map<FunctionSignature, APValue> variables) {
        this.variables = variables;
    }
    
    /**
     * Gets the output stream.
     *
     *
     * @return the output stream
     */
    public PrintStream getOutputStream() {
        return outputStream;
    }
    
    /**
     * Sets the output stream.
     *
     * @param p
     *            the new output stream
     */
    public void setOutputStream(final PrintStream p) {
        outputStream = p;
    }

    @Override
    public String toString() {
        return "Context [variables=" + variables + ", outputStream="
                + outputStream + "]";
    }
    
    public void putFunction(final Function function) {
        putFunction(function.signature, new APValueFunction(function));
    }
    
}
