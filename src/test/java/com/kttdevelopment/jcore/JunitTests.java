package com.kttdevelopment.jcore;

import org.junit.jupiter.api.*;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.TestAbortedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class JunitTests {

    private static final PrintStream SysOUT = System.out;
    private final ByteArrayOutputStream OUT = new ByteArrayOutputStream();

    @SuppressWarnings("SpellCheckingInspection")
    @BeforeAll
    public static void beforeAll(){
        System.out.println("::start-group::ENV" + '\n' + System.getenv() + '\n' + "::endgroup::");
    }

    @BeforeEach
    public void beforeEach(){
        System.setOut(new PrintStream(new MultiOutputStream(SysOUT, OUT)));
    }

    @AfterEach
    public void afterEach(){
        System.setOut(SysOUT);
        System.out.println("----- END OF TEST ---------------");
    }

    @Test
    public void testAssertion(){
        try{
            Assertions.assertEquals("true", "false", Workflow.junitError("true is not equal to false"));
        }catch(final AssertionFailedError ignored){ }

        final String first = OUT.toString().trim().split("%0A")[0];
        Assertions.assertTrue(first.startsWith("::error "));
        Assertions.assertTrue(first.endsWith(": true is not equal to false"));
    }

    @Test
    public void testAssumption(){
        try{
            Assumptions.assumeTrue(false, Workflow.junitWarning("expected false to be true"));
        }catch(final TestAbortedException ignored){ }

        final String first = OUT.toString().trim().split("%0A")[0];
        Assertions.assertTrue(first.startsWith("::warning "));
        Assertions.assertTrue(first.endsWith(": expected false to be true"));
    }

}
