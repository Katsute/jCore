package dev.katsute.jcore;

import org.junit.jupiter.api.*;
import org.opentest4j.*;

import java.io.*;

final class AnnotatedTests {

    private static final PrintStream SysOUT = System.out;
    private final ByteArrayOutputStream OUT = new ByteArrayOutputStream();

    @SuppressWarnings("SpellCheckingInspection")
    @BeforeAll
    static void beforeAll(){
        Assumptions.assumeTrue(System.getenv("sample_only") == null);
        //System.out.println("::start-group::ENV" + '\n' + System.getenv() + '\n' + "::endgroup::");
        System.out.println("::stop-commands::stop-key");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("::stop-key::");
    }

    @BeforeEach
    final void beforeEach(){
        System.setOut(new PrintStream(new MultiOutputStream(SysOUT, OUT)));
    }

    @AfterEach
    final void afterEach(){
        System.setOut(SysOUT);
    }

    @Nested
    final class TestJUnit {

        @SuppressWarnings("SimplifiableAssertion")
        @Test
        final void testAssertion(){
            try{
                Workflow.annotateTest(() -> Assertions.assertTrue(false));
            }catch(final AssertionFailedError ignored){}

            final String first = OUT.toString().trim().split("%0A")[0];
            Assertions.assertTrue(first.startsWith("::error "));
            Assertions.assertTrue(first.endsWith(": expected: <true> but was: <false>"));
        }

        @Test
        final void testAssumption(){
            try{
                Workflow.annotateTest(() -> Assumptions.assumeTrue(false));
            }catch(final TestAbortedException ignored){}

            final String first = OUT.toString().trim().split("%0A")[0];
            Assertions.assertTrue(first.startsWith("::warning "));
            Assertions.assertTrue(first.endsWith(": Assumption failed: assumption is not true"));
        }

    }

    @Nested
    final class TestAlternativeException {

        @Test
        final void testAssertionError(){
            try{
                Workflow.annotateTest(() -> {throw new AssertionError("assertion error");});
            }catch(final AssertionError ignored){}

            final String first = OUT.toString().trim().split("%0A")[0];
            Assertions.assertTrue(first.startsWith("::error "));
            Assertions.assertTrue(first.endsWith(": assertion error"));
        }

        @Test
        final void testSkippedException(){
            try{
                Workflow.annotateTest(() -> {throw new TestSkippedException("test warning");});
            }catch(final TestSkippedException ignored){}

            final String first = OUT.toString().trim().split("%0A")[0];
            Assertions.assertTrue(first.startsWith("::warning "));
            Assertions.assertTrue(first.endsWith(": test warning"));
        }

        @Test
        final void testIncompleteExecutionException(){
            try{
                Workflow.annotateTest(() -> {throw new IncompleteExecutionException("test warning");});
            }catch(final IncompleteExecutionException ignored){}

            final String first = OUT.toString().trim().split("%0A")[0];
            Assertions.assertTrue(first.startsWith("::warning "));
            Assertions.assertTrue(first.endsWith(": test warning"));
        }

        @Test
        final void testNonTestException(){
            try{
                Workflow.annotateTest(() -> {throw new IOException("uncaught exception");});
            }catch(final Throwable ignored){}

            final String first = OUT.toString().trim().split("%0A")[0];
            Assertions.assertTrue(first.startsWith("::error "));
            Assertions.assertTrue(first.endsWith(": uncaught exception"));
        }

    }

}
