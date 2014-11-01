/*
 * @author Kyran Adams
 */
package interpreter.library;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import machine.Context;
import machine.Function;
import parser.ExpressionNode;
import parser.ExpressionNode.OrNode.VariableNode;
import type.APValue;
import type.APValueList;

// TODO: Auto-generated Javadoc
/**
 * The Class LibraryFunction.
 */
public final class LibraryFunction {
    
    /**
     * Instantiates a new library function.
     */
    private LibraryFunction() {
    }
    
    /**
     * Apply library functions.
     *
     * @param context
     *            the context
     * @return the context
     */
    public static Context applyLibraryFunctions(final Context context) {
        sublistFunction(context);
        printlnFunction(context);
        
        return context;
    }
    
    /**
     * Sublist function.
     *
     * @param context
     *            the context
     */
    private static void sublistFunction(final Context context) {
        final String listArg = "a";
        final String firstIndexArg = "b";
        final String secondIndexArg = "c";
        
        context.putFunction(
                "sublist",
                new Function("sublist", Arrays.asList(
                        new VariableNode(listArg), new VariableNode(
                                firstIndexArg),
                        new VariableNode(secondIndexArg)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                final int firstIndex = ((BigDecimal) new VariableNode(
                                        firstIndexArg).getValue(context)
                                        .getValue()).intValueExact();
                                final int secondIndex = ((BigDecimal) new VariableNode(
                                        secondIndexArg).getValue(context)
                                        .getValue()).intValueExact();
                                
                                final List list = (List) new VariableNode(
                                        listArg).getValue(context).getValue();
                                return new APValueList(list.subList(firstIndex,
                                        secondIndex));
                            }
                        }));
    }
    
    /**
     * Println function.
     *
     * @param context
     *            the context
     */
    private static void printlnFunction(final Context context) {
        final String argName = "a";
        
        context.putFunction(
                "println",
                new Function("println", Arrays
                        .asList(new VariableNode(argName)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                context.getOutputStream().println(
                                        new VariableNode(argName).getValue(
                                                context).getValue());
                                return APValue.VOID;
                            }
                        }));
    }
}
