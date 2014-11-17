/*
 * @author Kyran Adams
 */
package interpreter.library;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import machine.Context;
import machine.Function;
import math.BigDecimalMath;
import parser.ExpressionNode;
import parser.ExpressionNode.VariableNode;
import parser.ParserException;
import type.APValue;
import type.APValueChar;
import type.APValueList;
import type.APValueNum;

/**
 * The Class LibraryFunction. This class holds the methods to add library
 * functions to a context.
 *
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
     * Apply all library functions.
     *
     * @param context
     *            the context
     *
     * @return the context
     */
    public static Context applyLibraryFunctions(final Context context) {
        sublistFunction(context);
        foreachFunction(context);
        toStringFunction(context);
        mapFunctions(context);
        foldlFunction(context);
        printFunctions(context);
        mathFunctions(context);
        return context;
    }

    private static void toStringFunction(final Context context) {
        final String argName = "a";
        context.putFunction(new Function("toString", Arrays
                .asList(new VariableNode(argName)), new ExpressionNode<Void>(
                        null) {
            @Override
            public APValue getValue(final Context context) {
                final List<ExpressionNode> characters = new ArrayList<>();
                String n = "";

                final APValue value = new VariableNode(argName)
                .getValue(context);
                if (value instanceof APValueNum) {
                    final BigDecimal numArg = (BigDecimal) value.getValue();
                    n = numArg.stripTrailingZeros().toPlainString();
                } else {
                    n = value.toString();
                }
                for (final char c : n.toCharArray()) {
                    characters.add(new ConstantNode(new APValueChar(c)));
                }
                return new APValueList(characters);
            }
        }));
    }

    private static void foreachFunction(final Context context) {
        final String arg1 = "list";
        final String arg2 = "func";
        context.putFunction(new Function("foreach", Arrays.asList(
                new VariableNode(arg1), new VariableNode(arg2)),
                new ExpressionNode<Void>(null) {
                    @Override
                    public APValue getValue(final Context context) {
                        final List<ExpressionNode> numArg = (List<ExpressionNode>) new VariableNode(
                                arg1).getValue(context).getValue();
                        final Function function = (Function) new VariableNode(
                                arg2).getValue(context).getValue();
                        
                        if (function.parameters.size() != 1) {
                            throw new ParserException(
                                    "Lambda function passed to map must only have one argument");
                        }
                        
                        for (final ExpressionNode v : numArg) {
                            final Context c = new Context(context
                                    .getOutputStream());
                            
                            // give parent functions
                            c.setVariables(new HashMap<>(context.getVariables()));
                            
                            c.putFunction(function.parameters.get(0).getName(),
                                    v.getValue(c));
                            function.body.getValue(c);
                        }
                        
                        return APValue.VOID;
                    }
                }));
    }
    
    /**
     * Fold left function.
     *
     * @param context
     *            the context
     */
    private static void foldlFunction(final Context context) {
        final String arg1 = "arg1";
        final String arg2 = "arg2";
        
        context.putFunction(new Function("foldl", Arrays.asList(
                new VariableNode(arg1), new VariableNode(arg2)),
                new ExpressionNode<Void>(null) {
            @Override
            public APValue getValue(final Context context) {
                final List<ExpressionNode> unmodifiableList = (List<ExpressionNode>) new VariableNode(
                        arg1).getValue(context).getValue();
                final List<ExpressionNode> numArg = new ArrayList<>(
                                unmodifiableList);
                final Function function = (Function) new VariableNode(
                        arg2).getValue(context).getValue();

                if (function.parameters.size() != 2) {
                    throw new ParserException(
                            "Lambda function passed to foldl must take two arguments");
                }

                for (int i = 0; i < numArg.size() - 1; i++) {
                    final ExpressionNode first = numArg.get(i);
                    final ExpressionNode second = numArg.get(i + 1);

                    final Context c = new Context(context
                            .getOutputStream());

                    // give parent functions
                    c.setVariables(new HashMap<>(context.getVariables()));

                    c.putFunction(function.parameters.get(0).getName(),
                            first.getValue(c));
                    c.putFunction(function.parameters.get(1).getName(),
                            second.getValue(c));
                    numArg.set(i + 1,
                            new ConstantNode(function.body.getValue(c)));
                }

                return numArg.get(numArg.size() - 1).getValue(context);
            }
        }));
    }

    /**
     * map and mapWithIndex.
     *
     * @param context
     *            the context
     */
    private static void mapFunctions(final Context context) {
        final String arg1 = "list";
        final String arg2 = "func";
        context.putFunction(new Function("map", Arrays.asList(new VariableNode(
                arg1), new VariableNode(arg2)), new ExpressionNode<Void>(null) {
            @Override
            public APValue getValue(final Context context) {
                final List<ExpressionNode> numArg = (List<ExpressionNode>) new VariableNode(
                        arg1).getValue(context).getValue();
                final Function function = (Function) new VariableNode(arg2)
                        .getValue(context).getValue();
                final List<ExpressionNode> result = new ArrayList<>(numArg
                        .size());

                if (function.parameters.size() != 1) {
                    throw new ParserException(
                            "Lambda function passed to map must only have one argument");
                }

                for (final ExpressionNode v : numArg) {
                    final Context c = new Context(context.getOutputStream());
                    
                    // give parent functions
                    c.setVariables(new HashMap<>(context.getVariables()));

                    c.putFunction(function.parameters.get(0).getName(),
                            v.getValue(c));
                    result.add(new ConstantNode(function.body.getValue(c)));
                }

                return new APValueList(result);
            }
        }));
        context.putFunction(new Function("mapWithIndex", Arrays.asList(
                new VariableNode(arg1), new VariableNode(arg2)),
                new ExpressionNode<Void>(null) {
            @Override
            public APValue getValue(final Context context) {
                final List<ExpressionNode> numArg = (List<ExpressionNode>) new VariableNode(
                        arg1).getValue(context).getValue();
                final Function function = (Function) new VariableNode(
                        arg2).getValue(context).getValue();
                final List<ExpressionNode> result = new ArrayList<>(
                        numArg.size());

                if (function.parameters.size() != 2) {
                    throw new ParserException(
                            "Lambda function passed to mapWithIndex must have two arguments");
                }
                BigDecimal index = BigDecimal.ZERO;
                for (final ExpressionNode v : numArg) {
                    final Context c = new Context(context
                            .getOutputStream());

                    // give parent functions
                    c.setVariables(new HashMap<>(context.getVariables()));

                    c.putFunction(function.parameters.get(0).getName(),
                            v.getValue(c));
                    c.putFunction(function.parameters.get(1).getName(),
                                    new APValueNum(index));
                    result.add(new ConstantNode(function.body
                            .getValue(c)));
                    index = index.add(BigDecimal.ONE);
                }

                return new APValueList(result);
            }
        }));
    }
    
    /*
     * Math functions. sqrt, sin, cosine, tan
     */
    /**
     * Math functions.
     *
     * @param context
     *            the context
     */
    private static void mathFunctions(final Context context) {
        final String argName = "a";
        context.putFunction(new Function("sqrt", Arrays
                .asList(new VariableNode(argName)), new ExpressionNode<Void>(
                null) {
            @Override
            public APValue getValue(final Context context) {
                final BigDecimal numArg = (BigDecimal) new VariableNode(argName)
                .getValue(context).getValue();
                return new APValueNum(BigDecimalMath.sqrt(numArg));
            }
        }));
        context.putFunction(new Function("sin", Arrays.asList(new VariableNode(
                argName)), new ExpressionNode<Void>(null) {
            @Override
            public APValue getValue(final Context context) {
                final BigDecimal numArg = (BigDecimal) new VariableNode(argName)
                .getValue(context).getValue();
                return new APValueNum(BigDecimalMath.sin(numArg));
            }
        }));
        context.putFunction(new Function("cos", Arrays.asList(new VariableNode(
                argName)), new ExpressionNode<Void>(null) {
            @Override
            public APValue getValue(final Context context) {
                final BigDecimal numArg = (BigDecimal) new VariableNode(argName)
                .getValue(context).getValue();
                return new APValueNum(BigDecimalMath.cos(numArg));
            }
        }));
        context.putFunction(new Function("tan", Arrays.asList(new VariableNode(
                argName)), new ExpressionNode<Void>(null) {
            @Override
            public APValue getValue(final Context context) {
                final BigDecimal numArg = (BigDecimal) new VariableNode(argName)
                .getValue(context).getValue();
                return new APValueNum(BigDecimalMath.tan(numArg));
            }
        }));
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
    }

    /**
     * Println and print function.
     *
     * @param context
     *            the context
     */
    private static void printFunctions(final Context context) {
        final String argName = "a";

        context.putFunction(new Function("println", Arrays
                .asList(new VariableNode(argName)), new ExpressionNode<Void>(
                        null) {
            @Override
            public APValue getValue(final Context context) {
                context.getOutputStream().println(
                        new VariableNode(argName).getValue(context));
                return APValue.VOID;
            }
        }));
        context.putFunction(new Function("print", Arrays
                .asList(new VariableNode(argName)), new ExpressionNode<Void>(
                        null) {
            @Override
            public APValue getValue(final Context context) {
                context.getOutputStream().print(
                        new VariableNode(argName).getValue(context).getValue());
                return APValue.VOID;
            }
        }));
    }
}
