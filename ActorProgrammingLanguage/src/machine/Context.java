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

// TODO: Auto-generated Javadoc
/**
 * The Class Context stores variable and function mappings.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Context {
    
    /** The variable mapping. */
    private Map<String, APValue> variables;
    
    /** The function mapping. */
    private Map<FunctionSignature, Function> functions;

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
        functions = new HashMap<>();
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
        functions = new HashMap<>();
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
    public void putVariable(final String name, final APValue value) {
        // If this context has a parent
        if (parent.isPresent()) {
            // If that parent has the variable we are assigning
            if (parent.get().getVariable(name) != null) {
                // Put it to the parent instead
                parent.get().putVariable(name, value);
                return;
            }
        }
        getVariables().put(name, value);
    }
    
    /**
     * Put function.
     *
     *
     * @param func
     *            the func
     */
    public void putFunction(final Function func) {
        // If this context has a parent
        if (parent.isPresent()) {
            // If that parent has the variable we are assigning
            if (parent.get().getFunction(func.signature) != null) {
                // Put it to the parent instead
                parent.get().putFunction(func);
                return;
            }
        }
        getFunctions().put(func.signature, func);
    }
    
    /**
     * Gets the variable with a given name.
     *
     * @param name
     *            the s
     *
     * @return the variable
     */
    public APValue getVariable(final String name) {
        APValue node = getVariables().get(name);
        if (node == null) {
            if (parent.isPresent()) {
                node = parent.get().getVariable(name);
            } else {
                throw new ContextException("Could not find value for <" + name
                        + ">");
            }
        }
        return node;
        
    }
    
    /**
     * Gets the function with a given signature.
     *
     * @param signature
     *            the function signature
     *
     * @return the function
     */
    public Function getFunction(final FunctionSignature signature) {
        Function node = getFunctions().get(signature);
        if (node == null) {
            if (parent.isPresent()) {
                node = parent.get().getFunction(signature);
            } else {
                throw new ContextException(
                        "Could not find function with name <" + signature + ">");
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
    public Map<String, APValue> getVariables() {
        return variables;
    }
    
    /**
     * Sets the variable map.
     *
     * @param variables
     *            the variable map
     */
    public void setVariables(final Map<String, APValue> variables) {
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

    /**
     * Gets the functions map.
     *
     *
     * @return the functions
     */
    public Map<FunctionSignature, Function> getFunctions() {
        return functions;
    }
    
    /**
     * Sets the functions map.
     *
     * @param functions
     *            the functions
     */
    public void setFunctions(final Map<FunctionSignature, Function> functions) {
        this.functions = functions;
    }

    @Override
    public String toString() {
        return "Context [variables=" + variables + ", functions=" + functions
                + ", outputStream=" + outputStream + "]";
    }
    
}
