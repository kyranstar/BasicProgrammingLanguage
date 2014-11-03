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
import parser.ExpressionNode.VariableNode;
import type.APValue;
import type.APValueList;

/**
 * The Class LibraryFunction.
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public final class LibraryFunction {
    
    /**
     * Private constructor, this is a utility class.
     */
    private LibraryFunction() {
    }
    
    /**
     * Apply library functions.
     *
     * @param context
     *            the context
    
     * @return the context */
    public static Context applyLibraryFunctions(final Context context) {
        sublistFunction(context);
        printlnFunction(context);
        
        return context;
    }
    
    /**
     * Sublist function. First number is inclusive, second is exclusive.
     *
     * Example: subList ([1,2,3,4], 1,3) = [2,3]
     *
     * @param context
     *            the context
     */
    private static void sublistFunction(final Context context) {
        final String listArg = "a";
        final String firstIndexArg = "b";
        final String secondIndexArg = "c";
        
        context.putFunction(new Function("sublist", Arrays.asList(
                new VariableNode(listArg), new VariableNode(firstIndexArg),
                new VariableNode(secondIndexArg)), new ExpressionNode<Void>(
                null) {
            @Override
            public APValue getValue(final Context context) {
                final int firstIndex = ((BigDecimal) new VariableNode(
                        firstIndexArg).getValue(context).getValue())
                        .intValueExact();
                final int secondIndex = ((BigDecimal) new VariableNode(
                        secondIndexArg).getValue(context).getValue())
                        .intValueExact();
                
                final List<ExpressionNode> list = (List<ExpressionNode>) new VariableNode(
                        listArg).getValue(context).getValue();
                return new APValueList(list.subList(firstIndex, secondIndex));
            }
        }));
        context.putFunction(new Function("sublist", Arrays.asList(
                new VariableNode(listArg), new VariableNode(firstIndexArg)),
                new ExpressionNode<Void>(null) {
            @Override
            public APValue getValue(final Context context) {
                final int firstIndex = ((BigDecimal) new VariableNode(
                        firstIndexArg).getValue(context).getValue())
                        .intValueExact();

                final List<ExpressionNode> list = (List<ExpressionNode>) new VariableNode(
                        listArg).getValue(context).getValue();
                try {
                            return new APValueList(list.subList(firstIndex,
                                    list.size()));
                } catch (final IllegalArgumentException e) {
                    throw new IndexOutOfBoundsException(e.getMessage());
                }
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
        
        context.putFunction(new Function("println", Arrays
                .asList(new VariableNode(argName)), new ExpressionNode<Void>(
                null) {
            @Override
            public APValue getValue(final Context context) {
                context.getOutputStream().println(
                        new VariableNode(argName).getValue(context).getValue());
                return APValue.VOID;
            }
        }));
    }
}
