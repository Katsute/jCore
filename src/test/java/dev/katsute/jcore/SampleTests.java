package dev.katsute.jcore;

import org.junit.jupiter.api.*;

@SuppressWarnings({"SimplifiableAssertion", "ConstantConditions"})
public final class SampleTests {

    @Test
    public void testJUnitWarning(){
        Assumptions.assumeTrue("false".equals(System.getenv("sample_only")));

        Assumptions.assumeTrue(false, "expected expression to be true");
    }

    @Test
    public void testJUnitError(){
        Assumptions.assumeTrue("false".equals(System.getenv("sample_only")));

        Assertions.assertTrue(false, "expected expression to be true");
    }

    @Test
    public void testJCoreWarning(){
        Assumptions.assumeTrue("true".equals(System.getenv("sample_only")));

        Assumptions.assumeTrue(false, Workflow.warningSupplier("expected expression to be true"));
    }

        @Test
    public void testJCoreError(){
        Assumptions.assumeTrue("true".equals(System.getenv("sample_only")));

        Assertions.assertTrue(false, Workflow.errorSupplier("expected expression to be true"));
    }

}
