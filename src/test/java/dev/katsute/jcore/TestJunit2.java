package dev.katsute.jcore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class TestJunit2 {

    @Test
    public final void test(){
        rethrow(() -> Assertions.assertTrue(false));
    }

    @Test
    public final void test2(){
        Assertions.assertTrue(false);
    }

    static void rethrow(final ThrowingRunnable runnable){
        try{
            runnable.run();
        }catch(final AssertionFailedError e){
            System.out.println("--- begin stack trace ---");
            e.printStackTrace();
            System.out.println("--- end stack trace ---");
            throw e;
        }
    }

    interface ThrowingRunnable {

        void run() throws AssertionFailedError;

    }

}
