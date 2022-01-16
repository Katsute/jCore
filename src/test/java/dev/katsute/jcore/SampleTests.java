package dev.katsute.jcore;

import org.junit.jupiter.api.*;

@SuppressWarnings({"SimplifiableAssertion", "ConstantConditions"})
final class SampleTests {

    @Nested
    final class JunitSampleTest {

        @SuppressWarnings("SpellCheckingInspection")
        @BeforeEach
        final void beforeEach(){
            Assumptions.assumeTrue("false".equals(System.getenv("enable_jcore")));
        }

        @Test
        final void testAssertion(){
            Assertions.assertTrue(false, "expected expression to be true");
        }

        @Test
        final void testAssumption(){
            Assumptions.assumeTrue(false, "expected expression to be true");
        }

    }

    @Nested
    final class JCoreSampleTest {

        @SuppressWarnings("SpellCheckingInspection")
        @BeforeEach
        final void beforeEach(){
            Assumptions.assumeTrue("true".equals(System.getenv("enable_jcore")));
        }

        @Test
        final void testAssertion(){
            Workflow.annotateTest(() -> Assertions.assertTrue(false));
        }

        @Test
        final void testAssumption(){
            Workflow.annotateTest(() -> Assumptions.assumeTrue(false));
        }

    }

}
