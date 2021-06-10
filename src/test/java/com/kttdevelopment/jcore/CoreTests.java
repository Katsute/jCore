package com.kttdevelopment.jcore;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class CoreTests {

    private static final PrintStream SysOUT = System.out;
    private final ByteArrayOutputStream OUT = new ByteArrayOutputStream();

    @BeforeEach
    public void beforeEach(){
        System.setOut(new PrintStream(new MultiOutputStream(SysOUT, OUT)));
    }

    @AfterEach
    public void afterEach(){
        System.setOut(SysOUT);
    }

    @Test
    public void testSecret(){
        Workflow.setSecret("secret val");
        Assertions.assertEquals("::add-mask::secret val", OUT.toString().trim());
    }

    @Test
    public void testInput(){
        Assertions.assertEquals("val", Workflow.getInput("my-input"));
    }

    @Test
    public void testRequiredInput(){
        Assertions.assertEquals("val", Workflow.getInput("my-input", true));
    }

    @Test
    public void testMissingInputRequired(){
        Assertions.assertThrows(NullPointerException.class, () -> Workflow.getInput("missing", true));
    }

    @Test
    public void testMissingInputNotRequired(){
        Assertions.assertNull(Workflow.getInput("missing"));
    }

    @Test
    public void testInputCaseInsensitive(){
        Assertions.assertEquals("val", Workflow.getInput("My InPuT"));
    }

    @Test
    public void testInputSpecialCharacters(){
        Assertions.assertEquals("'\t\"\\ response", Workflow.getInput("special chars_'\t\"\\"));
    }

    @Test
    public void testInputMultipleSpaces(){
        Assertions.assertEquals("I have multiple spaces", Workflow.getInput("multiple spaces variable"));
    }

    @Test
    public void testMultilineInput(){
        Assertions.assertEquals(new String[]{"val1", "val2", "val3"}, Workflow.getMultilineInput("my input list"));
    }

    @Test
    public void testInputDefaultWhitespace(){
        Assertions.assertEquals("some val", Workflow.getInput("with trailing whitespace"));
    }

    @Test
    public void testInputTrueWhitespace(){
        Assertions.assertEquals("some val", Workflow.getInput("with trailing whitespace", true));
    }

    @Test
    public void testInputFalseWhitespace(){
        Assertions.assertEquals("  some val  ", Workflow.getInput("with trailing whitespace", false));
    }

    @Test
    public void testBooleanInputRequired(){
        Assertions.assertTrue(Workflow.getBooleanInput("boolean input", true));
    }

    @Test
    public void testBooleanInput(){
        Assertions.assertTrue(Workflow.getBooleanInput("true1"));
        Assertions.assertTrue(Workflow.getBooleanInput("true2"));
        Assertions.assertTrue(Workflow.getBooleanInput("true3"));
        Assertions.assertFalse(Workflow.getBooleanInput("false3"));
        Assertions.assertFalse(Workflow.getBooleanInput("false3"));
        Assertions.assertFalse(Workflow.getBooleanInput("false3"));
    }

    @Test
    public void testWrongBooleanInput(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> Workflow.getBooleanInput("wrong boolean input"));
    }

    @Test
    public void testSetOutput(){
        Workflow.setOutput("some output", "some value");
        Assertions.assertEquals("::set-output name=some output::some value", OUT.toString().trim());
    }

    @Test
    public void testSetOutputBooleans(){
        Workflow.setOutput("some output", false);
        Assertions.assertEquals("::set-output name=some output::false", OUT.toString().trim());
    }

    @Test
    public void testSetOutputNumbers(){
        Workflow.setOutput("some output", 1.01);
        Assertions.assertEquals("::set-output name=some output::1.01", OUT.toString().trim());
    }

    @Test
    public void testError(){
        Workflow.error("Error message");
        Assertions.assertTrue(OUT.toString().trim().startsWith("::error file=CoreTests.java,line="));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::Error message"));
    }

    @Test
    public void testErrorEscapes(){
        Workflow.error("Error message\r\n\n");
        Assertions.assertTrue(OUT.toString().trim().startsWith("::error file=CoreTests.java,line="));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::Error message%0D%0A%0A"));
    }

    @Test
    public void testErrorException(){
        final NullPointerException exception = new NullPointerException("NPE");
        Workflow.error(exception);
        final String expected = "::error file=" + exception.getStackTrace()[0].getFileName() + ",line=" + exception.getStackTrace()[0].getLineNumber() + "::";
        Assertions.assertTrue(OUT.toString().startsWith(expected), "Expected:" + expected);
    }

    @Test
    public void testWarning(){
        Workflow.warning("Warning");
        Assertions.assertTrue(OUT.toString().trim().startsWith("::warning file=CoreTests.java,line="));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::Warning"));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testWarningEscapes(){
        Workflow.warning("\r\nwarning\n");
        Assertions.assertTrue(OUT.toString().trim().startsWith("::warning file=CoreTests.java,line="));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::%0D%0Awarning%0A"));
    }

    @Test
    public void testWarningException(){
        final NullPointerException exception = new NullPointerException("NPE");
        Workflow.warning(exception);
        final String expected = "::warning file=" + exception.getStackTrace()[0].getFileName() + ",line=" + exception.getStackTrace()[0].getLineNumber() + "::";
        Assertions.assertTrue(OUT.toString().startsWith(expected), "Expected:" + expected);
    }

    @Test
    public void testGroup(){
        Workflow.startGroup("my-group");
        Assertions.assertEquals("::group::my-group", OUT.toString().trim());

        Workflow.endGroup();
        Assertions.assertEquals("::endgroup::", OUT.toString().trim().split("\\n")[1]);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testGroupWrap(){
        Workflow.startGroup("mygroup", () -> System.out.println("in my group"));
        Assertions.assertEquals("::group::mygroup\r\nin my group\r\n::endgroup::", OUT.toString().trim());
    }

    @Test
    public void testDebug(){
        Workflow.debug("Debug");
        Assertions.assertEquals("::debug::Debug", OUT.toString().trim());
    }

    @Test
    public void testDebugState(){
        Assertions.assertTrue(Workflow.isDebug());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testDebugEscapes(){
        Workflow.debug("\r\ndebug\n");
        Assertions.assertEquals("::debug::%0D%0Adebug%0A", OUT.toString().trim());
    }

    @Test
    public void testSaveState(){
        Workflow.saveState("state_1", "some value");
        Assertions.assertEquals("::save-state name=state_1::some value", OUT.toString().trim());
    }

    @Test
    public void testSaveStateNumbers(){
        Workflow.saveState("state_1", 1);
        Assertions.assertEquals("::save-state name=state_1::1", OUT.toString().trim());
    }

    @Test
    public void testSaveStateBooleans(){
        Workflow.saveState("state_1", true);
        Assertions.assertEquals("::save-state name=state_1::true", OUT.toString().trim());
    }

    @Test
    public void testGetState(){
        Assertions.assertEquals("state_val", Workflow.getState("TEST_1"));
    }

    @Test
    public void testEcho(){
        Workflow.setCommandEcho(true);
        Assertions.assertEquals("::echo::on", OUT.toString().trim());
    }

    @Test
    public void testEchoDisabled(){
        Workflow.setCommandEcho(false);
        Assertions.assertEquals("::echo::off", OUT.toString().trim());
    }

    @Test
    public void testStopCommand(){
        Workflow.stopCommand("my-stop-command");
        Assertions.assertEquals("::stop-commands::my-stop-command", OUT.toString().trim());

        Workflow.startCommand("my-stop-command");
        Assertions.assertEquals("::my-stop-command::", OUT.toString().trim().split("\\n")[1]);
    }


}
