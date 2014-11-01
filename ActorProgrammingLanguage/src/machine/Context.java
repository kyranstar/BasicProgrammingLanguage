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
 * The Class Context.
 */
public class Context {
    
    /** The context. */
    private Map<String, APValue> context;
    
    /** The functions. */
    private Map<String, Function> functions;

    /** The parent. */
    Optional<Context> parent;
    
    /** The output stream. */
    private PrintStream outputStream;
    
    /**
     * Instantiates a new context.
     *
     * @param p
     *            the p
     */
    public Context(final PrintStream p) {
        setContext(new HashMap<>());
        functions = new HashMap<>();
        parent = Optional.empty();
        outputStream = p;
        
        LibraryFunction.applyLibraryFunctions(this);
    }
    
    /**
     * Instantiates a new context.
     *
     * @param parent
     *            the parent
     */
    public Context(final Context parent) {
        setContext(new HashMap<>());
        functions = new HashMap<>();
        this.parent = Optional.of(parent);
        outputStream = parent.getOutputStream();
    }
    
    /**
     * Put variable.
     *
     * @param s
     *            the s
     * @param en
     *            the en
     */
    public void putVariable(final String s, final APValue en) {
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
    
    /**
     * Put function.
     *
     * @param s
     *            the s
     * @param func
     *            the func
     */
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
        getFunctions().put(s, func);
    }
    
    /**
     * Gets the variable.
     *
     * @param s
     *            the s
     * @return the variable
     */
    public APValue getVariable(final String s) {
        APValue node = getContext().get(s);
        if (node == null) {
            if (parent.isPresent()) {
                node = parent.get().getVariable(s);
            } else {
                throw new ContextException("Could not find value for <" + s
                        + ">");
            }
        }
        return node;
        
    }
    
    /**
     * Gets the function.
     *
     * @param s
     *            the s
     * @return the function
     */
    public Function getFunction(final String s) {
        Function node = getFunctions().get(s);
        if (node == null) {
            if (parent.isPresent()) {
                node = parent.get().getFunction(s);
            } else {
                throw new ContextException(
                        "Could not find function with name <" + s + ">");
            }
        }
        return node;
        
    }
    
    /**
     * Gets the child.
     *
     * @return the child
     */
    public Context getChild() {
        return new Context(this);
    }
    
    /**
     * Gets the context.
     *
     * @return the context
     */
    public Map<String, APValue> getContext() {
        return context;
    }
    
    /**
     * Sets the context.
     *
     * @param context
     *            the context
     */
    public void setContext(final Map<String, APValue> context) {
        this.context = context;
    }
    
    /**
     * Gets the output stream.
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
     * Gets the functions.
     *
     * @return the functions
     */
    public Map<String, Function> getFunctions() {
        return functions;
    }
    
    /**
     * Sets the functions.
     *
     * @param functions the functions
     */
    public void setFunctions(final Map<String, Function> functions) {
        this.functions = functions;
    }
    
}
