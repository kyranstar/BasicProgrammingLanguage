/*
 * @author Kyran Adams
 */
package machine;

import interpreter.library.LibraryFunction;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import parser.ParserException;
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
    private Map<String, VariableMapping> variables;

    private final Map<String, DataStructure> dataTypes;

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
        dataTypes = new HashMap<>();
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
    public void putFunction(final String name, final APValue value,
            final boolean isMutable) {
        final VariableMapping map = getVariables().get(name);
        if (map != null) {
            if (!map.isMutable) {
                throw new ParserException(
                        "Can't change the value of non mutable function "
                                + name);
            }
        }
        
        getVariables().put(name, new VariableMapping(value, isMutable));
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
        final VariableMapping variableMapping = getVariables().get(
                functionSignature);
        if (variableMapping == null) {
            return null;
        }
        final APValue node = variableMapping.variable;
        return node;

    }

    /**
     * Gets the variables.
     *
     *
     * @return the variables
     */
    public Map<String, VariableMapping> getVariables() {
        return variables;
    }

    /**
     * Sets the variable map.
     *
     * @param variables
     *            the variable map
     */
    public void setVariables(final Map<String, VariableMapping> variables) {
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
    public void putFunction(final Function function, final boolean isMutable) {
        putFunction(function.name, new APValueFunction(function), isMutable);
    }
    
    public void putDataType(final DataStructure dataType) {
        dataTypes.put(dataType.name, dataType);
    }
    
    public DataStructure getDataType(final String name) {
        return dataTypes.get(name);
    }

    public static class VariableMapping {
        public APValue variable;
        public boolean isMutable;

        public VariableMapping(final APValue variable, final boolean isMutable) {
            this.variable = variable;
            this.isMutable = isMutable;
        }
        
    }
}
