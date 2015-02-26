/*
 * @author Kyran Adams
 */
package interpreter.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import machine.Context;
import machine.Function;
import parser.ExpressionNode;
import parser.ExpressionNode.VariableNode;
import parser.ParserException;
import type.APNumber;
import type.APValue;
import type.APValueBool;
import type.APValueChar;
import type.APValueList;
import type.APValueNum;
import type.APValueType;

// TODO: Auto-generated Javadoc
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
        toStringFunction(context);
        castingFunctions(context);
        isaFunction(context);
        listFunctions(context);
        printFunctions(context);
        mathFunctions(context);
        return context;
    }
    
    /**
     * Isa function.
     *
     * @param context
     *            the context
     */
    private static void isaFunction(final Context context) {
        final String arg1 = "arg1";
        final String typeArg = "type";
        context.putFunction(
                new Function("isa", Arrays.asList(new VariableNode(arg1),
                        new VariableNode(typeArg)), new ExpressionNode<Void>(
                        null) {
                    @Override
                    public APValue getValue(final Context context) {
                        final APValue value = new VariableNode(arg1)
                                .getValue(context);
                        final APValue type = new VariableNode(typeArg)
                                .getValue(context);
                        if (!(type instanceof APValueType)) {
                            throw new ParserException(
                                    "Type argument must be a type, was " + type);
                        }
                        if (((APValueType) type).valueIsType(value)) {
                            return new APValueBool(true);
                        }
                        return new APValueBool(false);
                    }
                }), false);
        context.putFunction(
                new Function("type", Arrays.asList(new VariableNode(arg1)),
                        new ExpressionNode<Void>(null) {
                    @Override
                    public APValue getValue(final Context context) {
                        final APValue value = new VariableNode(arg1)
                        .getValue(context);
                        return new APValueType(value.getType());
                    }
                }), false);
    }
    
    /**
     * Casting functions.
     *
     * @param context
     *            the context
     */
    private static void castingFunctions(final Context context) {
        final String theNum = "num";
        context.putFunction(
                new Function("char", Arrays.asList(new VariableNode(theNum)),
                        new ExpressionNode<Void>(null) {
                    @Override
                    public APValue getValue(final Context context) {
                        final APValue value = new VariableNode(theNum)
                        .getValue(context);
                        if (!(value instanceof APValueNum)
                                && !(value instanceof APValueChar)) {
                            throw new ParserException(
                                    "Can not pass arg "
                                            + value.getClass()
                                            + " to function char");
                        }
                        if (value instanceof APValueNum) {
                            return new APValueChar(
                                    (char) ((APNumber) value.getValue())
                                    .intValueExact());
                        } else {
                            return value;
                        }

                    }
                }), false);
        final String theInput = "input";
        context.putFunction(
                new Function("num", Arrays.asList(new VariableNode(theInput)),
                        new ExpressionNode<Void>(null) {
                    @Override
                    public APValue getValue(final Context context) {
                        final APValue value = new VariableNode(theInput)
                        .getValue(context);
                        if (!(value instanceof APValueNum)
                                && !(value instanceof APValueChar)) {
                            throw new ParserException(
                                    "Can not pass arg "
                                            + value.getClass()
                                            + " to function num");
                        }
                        if (value instanceof APValueNum) {
                            return value;
                        } else {
                            return new APValueNum(new APNumber(
                                    (char) value.getValue()));
                        }

                    }
                }), false);
    }
    
    /**
     * List functions.
     *
     * @param context
     *            the context
     */
    private static void listFunctions(final Context context) {
        sublistFunction(context);
        foreachFunction(context);
        mapFunctions(context);
        foldlFunction(context);
        inFunction(context);
        lengthFunction(context);
    }
    
    /**
     * Length function.
     *
     * @param context
     *            the context
     */
    private static void lengthFunction(final Context context) {
        final String argName = "a";
        context.putFunction(
                new Function("length",
                        Arrays.asList(new VariableNode(argName)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                        final APValue value = new VariableNode(argName)
                                        .getValue(context);
                                if (value instanceof APValueList) {
                                    final List<ExpressionNode> numArg = (List<ExpressionNode>) value
                                            .getValue();
                                    
                                    return new APValueNum(new APNumber(numArg
                                            .size()));
                                }
                                throw new ParserException(
                                        "Function list cannot take parameter of type "
                                                + value.getClass());
                            }
                        }), false);
    }
    
    /**
     * In function.
     *
     * @param context
     *            the context
     */
    private static void inFunction(final Context context) {
        final String arg1 = "member";
        final String arg2 = "list";
        context.putFunction(
                new Function("in", Arrays.asList(new VariableNode(arg1),
                        new VariableNode(arg2)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                final APValue value = new VariableNode(arg1)
                                        .getValue(context);
                                final List<ExpressionNode> numArg = (List<ExpressionNode>) new VariableNode(
                                        arg2).getValue(context).getValue();
                                for (final ExpressionNode node : numArg) {
                                    if (value.equals(node.getValue(context))) {
                                        return new APValueBool(true);
                                    }
                                }
                                return new APValueBool(false);
                            }
                        }), false);
    }
    
    /**
     * To string function.
     *
     * @param context
     *            the context
     */
    private static void toStringFunction(final Context context) {
        final String argName = "a";
        context.putFunction(
                new Function("toString", Arrays
                        .asList(new VariableNode(argName)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                final List<ExpressionNode> characters = new ArrayList<>();
                                final APValue value = new VariableNode(argName)
                                        .getValue(context);
                                
                                final String n = value.toString();
                                for (final char c : n.toCharArray()) {
                                    characters.add(new ConstantNode(
                                            new APValueChar(c)));
                                }
                                return new APValueList(characters);
                            }
                        }), false);
    }
    
    /**
     * Foreach function.
     *
     * @param context
     *            the context
     */
    private static void foreachFunction(final Context context) {
        final String arg1 = "list";
        final String arg2 = "func";
        context.putFunction(
                new Function("foreach", Arrays.asList(new VariableNode(arg1),
                        new VariableNode(arg2)),
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
                                    c.setVariables(new HashMap<>(context
                                            .getVariables()));
                                    
                                    c.putFunction(function.parameters.get(0)
                                            .getName(), v.getValue(c), false);
                                    function.body.getValue(c);
                                }
                                
                                return APValue.VOID;
                            }
                        }), false);
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

        context.putFunction(
                new Function("foldl", Arrays.asList(new VariableNode(arg1),
                        new VariableNode(arg2)),
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
                                    final ExpressionNode second = numArg
                                            .get(i + 1);
                                    
                                    final Context c = new Context(context
                                            .getOutputStream());
                                    
                                    // give parent functions
                                    c.setVariables(new HashMap<>(context
                                            .getVariables()));
                                    
                                    c.putFunction(function.parameters.get(0)
                                            .getName(), first.getValue(c),
                                    false);
                                    c.putFunction(function.parameters.get(1)
                                            .getName(), second.getValue(c),
                                    false);
                                    numArg.set(i + 1, new ConstantNode(
                                            function.body.getValue(c)));
                                }
                                
                                return numArg.get(numArg.size() - 1).getValue(
                                        context);
                            }
                        }), false);
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
        context.putFunction(
                new Function("map", Arrays.asList(new VariableNode(arg1),
                        new VariableNode(arg2)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                final List<ExpressionNode> numArg = (List<ExpressionNode>) new VariableNode(
                                        arg1).getValue(context).getValue();
                                final Function function = (Function) new VariableNode(
                                        arg2).getValue(context).getValue();
                                final List<ExpressionNode> result = new ArrayList<>(
                                        numArg.size());
                                
                                if (function.parameters.size() != 1) {
                                    throw new ParserException(
                                            "Lambda function passed to map must only have one argument");
                                }
                                
                                for (final ExpressionNode v : numArg) {
                                    final Context c = new Context(context
                                            .getOutputStream());
                                    
                                    // give parent functions
                                    c.setVariables(new HashMap<>(context
                                            .getVariables()));
                                    
                                    c.getVariables().remove(
                                    function.parameters.get(0)
                                    .getName());
                                    
                                    c.putFunction(function.parameters.get(0)
                                            .getName(), v.getValue(c), false);
                                    result.add(new ConstantNode(function.body
                                            .getValue(c)));
                                }
                                
                                return new APValueList(result);
                            }
                        }), false);
        context.putFunction(
                new Function("mapWithIndex", Arrays.asList(new VariableNode(
                        arg1), new VariableNode(arg2)),
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
                                APNumber index = APNumber.ZERO;
                                for (final ExpressionNode v : numArg) {
                                    final Context c = new Context(context
                                            .getOutputStream());
                                    
                                    // give parent functions
                                    c.setVariables(new HashMap<>(context
                                            .getVariables()));
                                    
                                    c.putFunction(function.parameters.get(0)
                                            .getName(), v.getValue(c), false);
                                    c.putFunction(function.parameters.get(1)
                                            .getName(), new APValueNum(index),
                                    false);
                                    result.add(new ConstantNode(function.body
                                            .getValue(c)));
                                    index = index.add(APNumber.ONE);
                                }
                                
                                return new APValueList(result);
                            }
                        }), false);
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
        context.putFunction(
                new Function("sqrt", Arrays.asList(new VariableNode(argName)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                final APNumber numArg = (APNumber) new VariableNode(
                                        argName).getValue(context).getValue();
                                return new APValueNum(numArg.sqrt());
                            }
                        }), false);
        context.putFunction(
                new Function("sin", Arrays.asList(new VariableNode(argName)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                final APNumber numArg = (APNumber) new VariableNode(
                                        argName).getValue(context).getValue();
                                return new APValueNum(numArg.sin());
                            }
                        }), false);
        context.putFunction(
                new Function("cos", Arrays.asList(new VariableNode(argName)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                final APNumber numArg = (APNumber) new VariableNode(
                                        argName).getValue(context).getValue();
                                return new APValueNum(numArg.cos());
                            }
                        }), false);
        context.putFunction(
                new Function("tan", Arrays.asList(new VariableNode(argName)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                final APNumber numArg = (APNumber) new VariableNode(
                                        argName).getValue(context).getValue();
                                return new APValueNum(numArg.tan());
                            }
                        }), false);
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
        
        context.putFunction(
                new Function("sublist", Arrays.asList(
                        new VariableNode(listArg), new VariableNode(
                                firstIndexArg),
                        new VariableNode(secondIndexArg)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                final int firstIndex = ((APNumber) new VariableNode(
                                        firstIndexArg).getValue(context)
                                        .getValue()).intValueExact();
                                final int secondIndex = ((APNumber) new VariableNode(
                                        secondIndexArg).getValue(context)
                                        .getValue()).intValueExact();
                                
                                final List<ExpressionNode> list = (List<ExpressionNode>) new VariableNode(
                                        listArg).getValue(context).getValue();
                                return new APValueList(list.subList(firstIndex,
                                        secondIndex));
                            }
                        }), false);
    }
    
    /**
     * Println and print function.
     *
     * @param context
     *            the context
     */
    private static void printFunctions(final Context context) {
        final String argName = "a";
        
        context.putFunction(
                new Function("println", Arrays
                        .asList(new VariableNode(argName)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                context.getOutputStream().println(
                                        new VariableNode(argName)
                                                .getValue(context));
                                return APValue.VOID;
                            }
                        }), false);
        context.putFunction(
                new Function("print", Arrays.asList(new VariableNode(argName)),
                        new ExpressionNode<Void>(null) {
                            @Override
                            public APValue getValue(final Context context) {
                                context.getOutputStream().print(
                                        new VariableNode(argName).getValue(
                                                context).getValue());
                                return APValue.VOID;
                            }
                        }), false);
    }
}
