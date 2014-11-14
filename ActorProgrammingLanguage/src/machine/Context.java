/*
 * @author Kyran Adams
 */
package machine;

import interpreter.library.LibraryFunction;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import type.APValue;
import type.APValueFunction;

/**
 * The Class Context stores function mappings.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Context {
    
    /** The variable mapping. */
    private Map<String, APValue> variables;
    
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
        outputStream = p;
        
        LibraryFunction.applyLibraryFunctions(this);
    }
    
    /**
     * Put variable.
     *
     * @param name
     *            the s
     * @param value
     *            the en
     */
    public void putFunction(final String name, final APValue value) {
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
    public APValue getFunction(final String functionSignature) {
        final APValue node = getVariables().get(functionSignature);
        return node;
        
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Context [variables=" + variables + ", outputStream="
                + outputStream + "]";
    }
    
    /**
     * Put function.
     *
     * @param function
     *            the function
     */
    public void putFunction(final Function function) {
        putFunction(function.name, new APValueFunction(function));
    }
    
}
