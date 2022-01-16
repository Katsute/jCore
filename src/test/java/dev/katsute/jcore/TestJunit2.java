package dev.katsute.jcore;

import org.junit.jupiter.api.*;

import java.io.IOException;

public class TestJunit2 {

    @Test
    public final void testWarning(){
        Workflow.annotateTest(() -> Assumptions.assumeTrue(false));
    }

    @Test
    public final void testWarning2(){
        Workflow.annotateTest(() -> Assumptions.assumeTrue(false, "false?"));
    }

    @Test
    public final void testError(){
        Workflow.annotateTest(() -> Assertions.assertTrue(false));
    }

    @Test
    public final void testError2(){
        Workflow.annotateTest(() -> Assertions.assertTrue(false, "false?"));
    }

    @Test
    public final void testException(){
        Workflow.annotateTest(() -> {
            throw new IOException("uncaught");
        });
    }

}
