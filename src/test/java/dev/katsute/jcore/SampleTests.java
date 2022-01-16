package dev.katsute.jcore;

import org.junit.jupiter.api.*;

@SuppressWarnings({"SimplifiableAssertion", "ConstantConditions"})
final class SampleTests {

    @Nested
    final class JunitSampleTest {

        @BeforeEach
        final void beforeEach(){
            Assumptions.assumeTrue("false".equals(System.getenv("sample_only")));
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

        @BeforeEach
        final void beforeEach(){
            Assumptions.assumeTrue("true".equals(System.getenv("sample_only")));
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
