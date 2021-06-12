package com.kttdevelopment.jcore;

import org.junit.jupiter.api.*;

public final class SampleTests {

    @Test
    public void testJUnit(){
        Assumptions.assumeTrue(System.getenv("sample_only") != null && System.getenv("sample_only").equals("false"));

        Assertions.assertEquals(1 + 1, 3, "Expected 1 + 1 to equal 2");
    }

    @Test
    public void testJCore(){
        Assumptions.assumeTrue(System.getenv("sample_only") != null && System.getenv("sample_only").equals("true"));

        Assertions.assertEquals(1 + 1, 3, Workflow.errorSupplier("Expected 1 + 1 to equal 2"));
    }

}
