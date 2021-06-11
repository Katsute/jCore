package com.kttdevelopment.jcore;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class CoreTests {

    private static final PrintStream SysOUT = System.out;
    private final ByteArrayOutputStream OUT = new ByteArrayOutputStream();

    @BeforeAll
    public static void beforeAll(){
        System.out.println(System.getenv());
    }

    @BeforeEach
    public void beforeEach(){
        System.setOut(new PrintStream(new MultiOutputStream(SysOUT, OUT)));
    }

    @AfterEach
    public void afterEach(){
        System.setOut(SysOUT);
    }

    // --------------- variables ---------------

    @Test
    public void testSecret(){
        Workflow.setSecret("secret val");
        Assertions.assertEquals("::add-mask::secret val", OUT.toString().trim());
    }

    // ----- input ---------------

    @Test
    public void testInput(){
        Assertions.assertEquals("val", Workflow.getInput("input"));
    }

    @Test
    public void testInputRequired(){
        Assertions.assertEquals("val", Workflow.getInput("input", true));
    }

    @Test
    public void testInputRequiredMissing(){
        Assertions.assertThrows(NullPointerException.class, () -> Workflow.getInput("missing", true));
    }

    @Test
    public void testInputMissing(){
        Assertions.assertNull(Workflow.getInput("missing"));
    }

    @Test
    public void testInputCaseInsensitive(){
        Assertions.assertEquals("val", Workflow.getInput("InPuT"));
    }

    @Test
    public void testInputSpecialCharacters(){
        Assertions.assertEquals("'\t\"\\ response", Workflow.getInput("special chars"));
    }

    @Test
    public void testInputMultipleSpaces(){
        Assertions.assertEquals("I have multiple spaces", Workflow.getInput("multiple space variable"));
    }

    @Test
    public void testInputMultipleLines(){
        Assertions.assertArrayEquals(new String[]{"val1", "val2", "val3"}, Workflow.getMultilineInput("multiple lines"));
    }

    @Test
    public void testInputWhitespaceDefault(){
        Assertions.assertEquals("some val", Workflow.getInput("whitespace"));
    }

    @Test
    public void testInputWhitespaceTrue(){
        Assertions.assertEquals("some val", Workflow.getInput("whitespace", true, true));
    }

    @Test
    public void testInputWhitespaceFalse(){
        Assertions.assertEquals("  some val  ", Workflow.getInput("whitespace", true, false));
    }

    @Test
    public void testInputBoolean(){
        Assertions.assertTrue(Workflow.getBooleanInput("true1"));
        Assertions.assertTrue(Workflow.getBooleanInput("true2"));
        Assertions.assertTrue(Workflow.getBooleanInput("true3"));
        Assertions.assertFalse(Workflow.getBooleanInput("false3"));
        Assertions.assertFalse(Workflow.getBooleanInput("false3"));
        Assertions.assertFalse(Workflow.getBooleanInput("false3"));
    }

    @Test
    public void testInputBooleanWrong(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> Workflow.getBooleanInput("wrong"));
    }

    // ----- output ---------------

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

    // ----- echo ---------------

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

    // --------------- results ---------------

    //              (skipped)

    // --------------- logging ---------------

    @Test
    public void testInfo(){
        Workflow.info("info");
        Assertions.assertEquals("info", OUT.toString().trim());
    }

    // ----- debug ---------------

    @Test
    public void testDebug(){
        Workflow.debug("Debug");
        Assertions.assertEquals("::debug::Debug", OUT.toString().trim());
    }

    @Test
    public void testDebugBoolean(){
        Assertions.assertTrue(Workflow.isDebug());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testDebugEscapes(){
        Workflow.debug("\r\ndebug\n");
        Assertions.assertEquals("::debug::%0D%0Adebug%0A", OUT.toString().trim());
    }

    // ----- warning ---------------

    @Test
    public void testWarning(){
        Workflow.warning("Warning");
        Assertions.assertTrue(OUT.toString().trim().startsWith("::warning "));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::Warning"));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testWarningEscapes(){
        Workflow.warning("\r\nwarning\n");
        Assertions.assertTrue(OUT.toString().trim().startsWith("::warning "));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::%0D%0Awarning%0A"));
    }

    @Test
    public void testWarningException(){
        final NullPointerException exception = new NullPointerException("NPE");
        Workflow.warning(exception);
        Assertions.assertTrue(OUT.toString().trim().startsWith("::warning "));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::NPE"));
    }

    // ----- error ----------

    @Test
    public void testError(){
        Workflow.error("Error message");
        Assertions.assertTrue(OUT.toString().trim().startsWith("::error "));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::Error message"));
    }

    @Test
    public void testErrorEscapes(){
        Workflow.error("Error message\r\n\n");
        Assertions.assertTrue(OUT.toString().trim().startsWith("::error "));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::Error message%0D%0A%0A"));
    }

    @Test
    public void testErrorException(){
        final NullPointerException exception = new NullPointerException("NPE");
        Workflow.error(exception);
        Assertions.assertTrue(OUT.toString().trim().startsWith("::error "));
        Assertions.assertTrue(OUT.toString().trim().endsWith("::NPE"));
    }

    // ----- group ---------------

    @SuppressWarnings("SpellCheckingInspection")
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
        Assertions.assertEquals("::group::mygroup\nin my group\n::endgroup::", OUT.toString().trim().replace("\r", ""));
    }

    // --------------- state ---------------

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

    // --------------- commands ---------------

    @Test
    public void testStopCommand(){
        Workflow.stopCommand("my-stop-command");
        Assertions.assertEquals("::stop-commands::my-stop-command", OUT.toString().trim());

        Workflow.startCommand("my-stop-command");
        Assertions.assertEquals("::my-stop-command::", OUT.toString().trim().split("\\n")[1]);
    }


}
