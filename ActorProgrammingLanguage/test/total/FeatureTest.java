package total;

import interpreter.Interpreter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.Set;

import machine.Context;
import machine.Context.VariableMapping;

import org.junit.Test;

public class FeatureTest {

    @Test
    public void test() throws IOException {
        final String file = "./test/total/program.bpl";
        
        // interpret file
        final Context c = new Interpreter(ProgramTest.getEmptyPrintStream())
        .interpret(new String(Files.readAllBytes(Paths.get(file)),
                Charset.forName("UTF-8")));
        
        // remove library functions
        final Context initialContext = ProgramTest.getEmptyContext();
        final Set<Entry<String, VariableMapping>> variables = c.getVariables()
                .entrySet();
        variables.removeAll(initialContext.getVariables().entrySet());
        
        for (final Entry<String, VariableMapping> e : variables) {
            System.out.println(e.getKey() + " -> " + e.getValue().variable
                    + " is " + (e.getValue().isMutable ? "" : "not ")
                    + " mutable");
        }
    }
}
