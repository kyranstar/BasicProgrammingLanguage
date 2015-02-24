/*
 * @author Kyran Adams
 */
package machine;

import interpreter.library.LibraryFunction;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parser.ParserException;
import type.APValue;
import type.APValueFunction;
import type.APValueType;

// TODO: Auto-generated Javadoc
/**
 * The Class Context stores function mappings.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Context {

    /** The variable mapping. */
    @SuppressWarnings("serial")
    private Map<String, VariableMapping> variables = new HashMap<String, VariableMapping>() {
        {
            final String[] types = new String[] { "Num", "Char", "Bool",
                    "Func", "List" };
            for (final String s : types) {
                put(s, new VariableMapping(new APValueType(s), false));
            }
        }
    };

    /** A map of datatype names to a list of possible constructors. */
    @SuppressWarnings("serial")
    private final Map<String, List<DataConstructor>> dataTypes = new HashMap<>();

    /** The output stream. */
    private PrintStream outputStream;

    /**
     * Instantiates a new context with a given print stream.
     *
     * @param printStream
     *            the printStream
     */
    public Context(final PrintStream printStream) {
        outputStream = printStream;

        LibraryFunction.applyLibraryFunctions(this);
    }

    /**
     * Put variable.
     *
     * @param name
     *            the s
     * @param value
     *            the en
     * @param isMutable
     *            the is mutable
     */
    public void putFunction(final String name, final APValue value,
            final boolean isMutable) {
        final VariableMapping map = getVariables().get(name);
        if (map != null && !map.isMutable) {
            throw new ParserException(
                    "Can't change the value of non mutable function " + name);
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
        return variableMapping.variable;

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
     * @param printStream
     *            the new output stream
     */
    public void setOutputStream(final PrintStream printStream) {
        outputStream = printStream;
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
     * @param isMutable
     *            the is mutable
     */
    public void putFunction(final Function function, final boolean isMutable) {
        putFunction(function.name, new APValueFunction(function), isMutable);
    }
    
    /**
     * Put data type.
     *
     * @param dataType
     *            the data type
     */
    public void putDataType(final DataConstructor dataType) {
        if (dataTypes.get(dataType.name) == null) {
            dataTypes.put(dataType.name, new ArrayList<>());
        }
        dataTypes.get(dataType.name).add(dataType);
        final String name = dataType.name;
        // // split on the parens
        // final String[] parts = name.split("\\(");
        // // Change it from format "name(sub)" to "name$sub"
        // name = parts[0] + "$" + parts[1].substring(0, parts[1].length() - 1);
        variables.put(name, new VariableMapping(new APValueType(dataType.name),
                false));
    }
    
    /**
     * Gets the data type.
     *
     * @param name
     *            the name
     * @return the data type
     */
    public List<DataConstructor> getDataType(final String name) {
        return dataTypes.get(name);
    }

    /**
     * The Class VariableMapping.
     */
    public static class VariableMapping {
        
        /** The variable. */
        public APValue variable;
        
        /** The is mutable. */
        public boolean isMutable;

        /**
         * Instantiates a new variable mapping.
         *
         * @param variable
         *            the variable
         * @param isMutable
         *            the is mutable
         */
        public VariableMapping(final APValue variable, final boolean isMutable) {
            this.variable = variable;
            this.isMutable = isMutable;
        }
        
    }
}
