package com.kttdevelopment.jcore;

import org.junit.jupiter.api.*;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.TestAbortedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public final class JunitTests {

    private static final PrintStream SysOUT = System.out;
    private final ByteArrayOutputStream OUT = new ByteArrayOutputStream();

    @SuppressWarnings("SpellCheckingInspection")
    @BeforeAll
    public static void beforeAll(){
        Assumptions.assumeTrue(System.getenv("sample_only") == null);
        //System.out.println("::start-group::ENV" + '\n' + System.getenv() + '\n' + "::endgroup::");
        System.out.println("::stop-commands::stop-key");
    }

    @AfterAll
    public static void afterAll(){
        System.out.println("::stop-key::");
    }

    @BeforeEach
    public void beforeEach(){
        System.setOut(new PrintStream(new MultiOutputStream(SysOUT, OUT)));
    }

    @AfterEach
    public void afterEach(){
        System.setOut(SysOUT);
    }

    @Test
    public void testAssertion(){
        try{
            Assertions.assertEquals("true", "false", Workflow.errorSupplier("true is not equal to false"));
        }catch(final AssertionFailedError ignored){ }

        final String first = OUT.toString().trim().split("%0A")[0];
        Assertions.assertTrue(first.startsWith("::error "));
        Assertions.assertTrue(first.endsWith(": true is not equal to false"));
    }

    @Test
    public void testAssumption(){
        try{
            Assumptions.assumeTrue(false, Workflow.warningSupplier("expected false to be true"));
        }catch(final TestAbortedException ignored){ }

        final String first = OUT.toString().trim().split("%0A")[0];
        Assertions.assertTrue(first.startsWith("::warning "));
        Assertions.assertTrue(first.endsWith(": expected false to be true"));
    }

}
