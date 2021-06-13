package com.kttdevelopment.jcore;

import org.junit.jupiter.api.*;

@SuppressWarnings({"SimplifiableAssertion", "ConstantConditions"})
public final class SampleTests {

    @Test
    public void testJUnit(){
        Assumptions.assumeTrue(System.getenv("sample_only") != null && System.getenv("sample_only").equals("false"));

        Assertions.assertTrue(false, "expected expression to be true");
    }

    @Test
    public void testJCore(){
        Assumptions.assumeTrue(System.getenv("sample_only") != null && System.getenv("sample_only").equals("true"));

        Assertions.assertTrue(false, Workflow.errorSupplier("expected expression to be true"));
    }

}
