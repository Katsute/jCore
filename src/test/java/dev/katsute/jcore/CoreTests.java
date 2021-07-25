package dev.katsute.jcore;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class CoreTests {

    private static final PrintStream SysOUT = System.out;
    private final ByteArrayOutputStream OUT = new ByteArrayOutputStream();

    @SuppressWarnings("SpellCheckingInspection")
    @BeforeAll
    public static void beforeAll(){
        Assumptions.assumeTrue(System.getenv("sample_only") == null);
        //System.out.println("::start-group::ENV" + '\n' + System.getenv() + '\n' + "::endgroup::");
        System.out.println("::stop-commands::stop-key");
    }

    @AfterAll
    public static void afterAll(){
        System.out.println("::stop-key::");
    }

    @BeforeEach
    public void beforeEach(){
        System.setOut(new PrintStream(new MultiOutputStream(SysOUT, OUT)));
    }

    @AfterEach
    public void afterEach(){
        System.setOut(SysOUT);
    }

    private String firstLine(){
        return OUT.toString().trim().split("%0A")[0];
    }

    // --------------- variables ---------------

    @Test
    public void testSecret(){
        Workflow.setSecret("secret val");
        Assertions.assertEquals("::add-mask::secret val", OUT.toString().trim());
    }

    @Test
    public void testSecretMask(){
        Workflow.addMask("secret val");
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
        System.out.println("::stop-key::");

        Workflow.setOutput("some output", "some value");
        Assertions.assertEquals("::set-output name=some output::some value", OUT.toString().trim().split("\n")[1]);

        System.out.println("::stop-commands::stop-key");
    }

    @Test
    public void testSetOutputBooleans(){
        System.out.println("::stop-key::");

        Workflow.setOutput("some output", false);
        Assertions.assertEquals("::set-output name=some output::false", OUT.toString().trim().split("\n")[1]);

        System.out.println("::stop-commands::stop-key");
    }

    @Test
    public void testSetOutputNumbers(){
        System.out.println("::stop-key::");

        Workflow.setOutput("some output", 1.01);
        Assertions.assertEquals("::set-output name=some output::1.01", OUT.toString().trim().split("\n")[1]);

        System.out.println("::stop-commands::stop-key");
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

    //              (tested on CI)

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
        final String first = firstLine();
        Assertions.assertTrue(first.startsWith("::warning "));
        Assertions.assertTrue(first.endsWith(": Warning"));
    }

    @Test
    public void testWarningEscapes(){
        Workflow.warning("warning\r");
        final String first = firstLine();
        Assertions.assertTrue(first.startsWith("::warning "));
        Assertions.assertTrue(first.endsWith(": warning%0D"));
    }

    @Test
    public void testWarningException(){
        final NullPointerException exception = new NullPointerException("NPE");
        Workflow.warning(exception);
        final String first = firstLine();
        Assertions.assertTrue(first.startsWith("::warning "));
        Assertions.assertTrue(first.endsWith(": NPE"));
    }

    @Test
    public void testWarningThrowable(){
        final NullPointerException exception = new NullPointerException("NPE");
        Assertions.assertThrows(NullPointerException.class, () -> Workflow.throwWarning(exception));
        final String first = firstLine();
        Assertions.assertTrue(first.startsWith("::warning "));
        Assertions.assertTrue(first.endsWith(": NPE"));
    }

    // ----- error ----------

    @Test
    public void testError(){
        Workflow.error("Error message");
        final String first = firstLine();
        Assertions.assertTrue(first.startsWith("::error "));
        Assertions.assertTrue(first.endsWith(": Error message"));
    }

    @Test
    public void testErrorEscapes(){
        Workflow.error("Error message\r");
        final String first = firstLine();
        Assertions.assertTrue(first.startsWith("::error "));
        Assertions.assertTrue(first.endsWith(": Error message%0D"));
    }

    @Test
    public void testErrorException(){
        final NullPointerException exception = new NullPointerException("NPE");
        Workflow.error(exception);
        final String first = firstLine();
        Assertions.assertTrue(first.startsWith("::error "));
        Assertions.assertTrue(first.endsWith(": NPE"));
    }

    @Test
    public void testErrorThrowable(){
        final NullPointerException exception = new NullPointerException("NPE");
        Assertions.assertThrows(NullPointerException.class, () -> Workflow.throwError(exception));
        final String first = firstLine();
        Assertions.assertTrue(first.startsWith("::error "));
        Assertions.assertTrue(first.endsWith(": NPE"));
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

    @Test
    public void testMatcherAdd(){
        Workflow.addMatcher("matcher.json");
        Assertions.assertEquals("::add-matcher::matcher.json", OUT.toString().trim());
    }

    @Test
    public void testMatcherRemove(){
        Workflow.removeMatcher("owner");
        Assertions.assertEquals("::remove-matcher owner=owner::", OUT.toString().trim());
    }

}
