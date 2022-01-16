package dev.katsute.jcore;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@TestMethodOrder(MethodOrderer.MethodName.class)
final class CoreTests {

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

    @Nested
    final class TestSecret {

        @Test
        final void testSecret(){
            Workflow.setSecret("secret val");
            Assertions.assertEquals("::add-mask::secret val", OUT.toString().trim());
        }

        @Test
        final void testSecretMask(){
            Workflow.addMask("secret val");
            Assertions.assertEquals("::add-mask::secret val", OUT.toString().trim());
        }

    }

    @Nested
    final class TestInput {

        @Test
        final void testInput(){
            Assertions.assertEquals("val", Workflow.getInput("input"));
        }

        @Test
        final void testInputRequired(){
            Assertions.assertEquals("val", Workflow.getInput("input", true));
        }

        @Test
        final void testInputRequiredMissing(){
            Assertions.assertThrows(NullPointerException.class, () -> Workflow.getInput("missing", true));
        }

        @Test
        final void testInputMissing(){
            Assertions.assertNull(Workflow.getInput("missing"));
        }

        @Test
        final void testInputCaseInsensitive(){
            Assertions.assertEquals("val", Workflow.getInput("InPuT"));
        }

        @Test
        final void testInputSpecialCharacters(){
            Assertions.assertEquals("'\t\"\\ response", Workflow.getInput("special chars"));
        }

        @Test
        final void testInputMultipleSpaces(){
            Assertions.assertEquals("I have multiple spaces", Workflow.getInput("multiple space variable"));
        }

        @Test
        final void testInputMultipleLines(){
            Assertions.assertArrayEquals(new String[]{"val1", "val2", "val3"}, Workflow.getMultilineInput("multiple lines"));
        }

        @Test
        final void testInputWhitespaceDefault(){
            Assertions.assertEquals("some val", Workflow.getInput("whitespace"));
        }

        @Test
        final void testInputWhitespaceTrue(){
            Assertions.assertEquals("some val", Workflow.getInput("whitespace", true, true));
        }

        @Test
        final void testInputWhitespaceFalse(){
            Assertions.assertEquals("  some val  ", Workflow.getInput("whitespace", true, false));
        }

        @Test
        final void testInputBoolean(){
            Assertions.assertTrue(Workflow.getBooleanInput("true1"));
            Assertions.assertTrue(Workflow.getBooleanInput("true2"));
            Assertions.assertTrue(Workflow.getBooleanInput("true3"));
            Assertions.assertFalse(Workflow.getBooleanInput("false3"));
            Assertions.assertFalse(Workflow.getBooleanInput("false3"));
            Assertions.assertFalse(Workflow.getBooleanInput("false3"));
        }

        @Test
        final void testInputBooleanWrong(){
            Assertions.assertThrows(IllegalArgumentException.class, () -> Workflow.getBooleanInput("wrong"));
        }

    }

    @Nested
    final class TestOutput {

        @Test
        final void testSetOutput(){
            System.out.println("::stop-key::");

            Workflow.setOutput("some output", "some value");
            Assertions.assertEquals("::set-output name=some output::some value", OUT.toString().trim().split("\n")[1]);

            System.out.println("::stop-commands::stop-key");
        }

        @Test
        final void testSetOutputBooleans(){
            System.out.println("::stop-key::");

            Workflow.setOutput("some output", false);
            Assertions.assertEquals("::set-output name=some output::false", OUT.toString().trim().split("\n")[1]);

            System.out.println("::stop-commands::stop-key");
        }

        @Test
        final void testSetOutputNumbers(){
            System.out.println("::stop-key::");

            Workflow.setOutput("some output", 1.01);
            Assertions.assertEquals("::set-output name=some output::1.01", OUT.toString().trim().split("\n")[1]);

            System.out.println("::stop-commands::stop-key");
        }

    }

    @Nested
    final class TestEcho {

        @Test
        final void testEcho(){
            Workflow.setCommandEcho(true);
            Assertions.assertEquals("::echo::on", OUT.toString().trim());
        }

        @Test
        final void testEchoDisabled(){
            Workflow.setCommandEcho(false);
            Assertions.assertEquals("::echo::off", OUT.toString().trim());
        }

    }

    @Nested
    final class TestInfo {

        @Test
        final void testInfo(){
            Workflow.info("info");
            Assertions.assertEquals("info", OUT.toString().trim());
        }

    }

    @Nested
    final class TestDebug {

        @Test
        final void testDebug(){
            Workflow.debug("Debug");
            Assertions.assertEquals("::debug::Debug", OUT.toString().trim());
        }

        @Test
        final void testDebugBoolean(){
            Assertions.assertTrue(Workflow.isDebug());
        }

        @SuppressWarnings("SpellCheckingInspection")
        @Test
        final void testDebugEscapes(){
            Workflow.debug("\r\ndebug\n");
            Assertions.assertEquals("::debug::%0D%0Adebug%0A", OUT.toString().trim());
        }

    }

    @Nested
    final class TestNotice {

        @Test
        final void testNotice(){
            Workflow.notice("Notice");
            Assertions.assertEquals(firstLine(), "::notice::Notice");
        }

        @Test
        final void testNoticeNullProperties(){
            Workflow.notice("Notice", null);
            Assertions.assertEquals(firstLine(), "::notice::Notice");
        }

        @Test
        final void testNoticeProperties(){
            Workflow.notice(
                "Notice",
                new AnnotationProperties.Builder()
                    .title("A title")
                    .file("root/test.txt")
                    .startColumn(1)
                    .endColumn(2)
                    .startLine(3)
                    .endLine(4)
                    .build());
            Assertions.assertEquals(firstLine(), "::notice title=A title,file=root/test.txt,col=1,endColumn=2,line=3,endLine=4::Notice");
        }

    }

    @Nested
    final class TestWarning {

        @Test
        final void testWarning(){
            Workflow.warning("Warning");
            final String first = firstLine();
            Assertions.assertTrue(first.startsWith("::warning "));
            Assertions.assertTrue(first.endsWith(": Warning"));
        }

        @Test
        final void testWarningNullProperties(){
            Workflow.warning("Warning", null);
            Assertions.assertEquals(firstLine(), "::warning::Warning");
        }

        @Test
        final void testWarningProperties(){
            Workflow.warning(
                "Warning",
                new AnnotationProperties.Builder()
                    .title("A title")
                    .file("root/test.txt")
                    .startColumn(1)
                    .endColumn(2)
                    .startLine(3)
                    .endLine(4)
                    .build());
            Assertions.assertEquals(firstLine(), "::warning title=A title,file=root/test.txt,col=1,endColumn=2,line=3,endLine=4::Warning");
        }

        @Test
        final void testWarningEscapes(){
            Workflow.warning("warning\r");
            final String first = firstLine();
            Assertions.assertTrue(first.startsWith("::warning "));
            Assertions.assertTrue(first.endsWith(": warning%0D"));
        }

        @Test
        final void testWarningException(){
            final NullPointerException exception = new NullPointerException("NPE");
            Workflow.warning(exception);
            final String first = firstLine();
            Assertions.assertTrue(first.startsWith("::warning "));
            Assertions.assertTrue(first.endsWith(": NPE"));
        }

        @Test
        final void testWarningThrowable(){
            final NullPointerException exception = new NullPointerException("NPE");
            Assertions.assertThrows(NullPointerException.class, () -> Workflow.throwWarning(exception));
            final String first = firstLine();
            Assertions.assertTrue(first.startsWith("::warning "));
            Assertions.assertTrue(first.endsWith(": NPE"));
        }

    }

    @Nested
    final class TestError {

        @Test
        final void testError(){
            Workflow.error("Error message");
            final String first = firstLine();
            Assertions.assertTrue(first.startsWith("::error "));
            Assertions.assertTrue(first.endsWith(": Error message"));
        }

        @Test
        final void testErrorNullProperties(){
            Workflow.error("Error", null);
            Assertions.assertEquals(firstLine(), "::error::Error");
        }

        @Test
        final void testErrorProperties(){
            Workflow.error(
                "Error",
                new AnnotationProperties.Builder()
                    .title("A title")
                    .file("root/test.txt")
                    .startColumn(1)
                    .endColumn(2)
                    .startLine(3)
                    .endLine(4)
                    .build());
            Assertions.assertEquals(firstLine(), "::error title=A title,file=root/test.txt,col=1,endColumn=2,line=3,endLine=4::Error");
        }

        @Test
        final void testErrorEscapes(){
            Workflow.error("Error message\r");
            final String first = firstLine();
            Assertions.assertTrue(first.startsWith("::error "));
            Assertions.assertTrue(first.endsWith(": Error message%0D"));
        }

        @Test
        final void testErrorException(){
            final NullPointerException exception = new NullPointerException("NPE");
            Workflow.error(exception);
            final String first = firstLine();
            Assertions.assertTrue(first.startsWith("::error "));
            Assertions.assertTrue(first.endsWith(": NPE"));
        }

        @Test
        final void testErrorThrowable(){
            final NullPointerException exception = new NullPointerException("NPE");
            Assertions.assertThrows(NullPointerException.class, () -> Workflow.throwError(exception));
            final String first = firstLine();
            Assertions.assertTrue(first.startsWith("::error "));
            Assertions.assertTrue(first.endsWith(": NPE"));
        }

    }

    @Nested
    final class TestGroup {

        @SuppressWarnings("SpellCheckingInspection")
        @Test
        final void testGroup(){
            Workflow.startGroup("my-group");
            Assertions.assertEquals("::group::my-group", OUT.toString().trim());

            Workflow.endGroup();
            Assertions.assertEquals("::endgroup::", OUT.toString().trim().split("\\n")[1]);
        }

        @SuppressWarnings("SpellCheckingInspection")
        @Test
        final void testGroupWrap(){
            Workflow.startGroup("mygroup", () -> System.out.println("in my group"));
            Assertions.assertEquals("::group::mygroup\nin my group\n::endgroup::", OUT.toString().trim().replace("\r", ""));
        }

    }

    @Nested
    final class TestState {

        @Test
        final void testSaveState(){
            Workflow.saveState("state_1", "some value");
            Assertions.assertEquals("::save-state name=state_1::some value", OUT.toString().trim());
        }

        @Test
        final void testSaveStateNumbers(){
            Workflow.saveState("state_1", 1);
            Assertions.assertEquals("::save-state name=state_1::1", OUT.toString().trim());
        }

        @Test
        final void testSaveStateBooleans(){
            Workflow.saveState("state_1", true);
            Assertions.assertEquals("::save-state name=state_1::true", OUT.toString().trim());
        }

        @Test
        final void testGetState(){
            Assertions.assertEquals("state_val", Workflow.getState("TEST_1"));
        }

    }

    @Nested
    final class TestCommands {

        @Test
        final void testStopCommand(){
            Workflow.stopCommand("my-stop-command");
            Assertions.assertEquals("::stop-commands::my-stop-command", OUT.toString().trim());

            Workflow.startCommand("my-stop-command");
            Assertions.assertEquals("::my-stop-command::", OUT.toString().trim().split("\\n")[1]);
        }

        @Test
        final void testMatcherAdd(){
            Workflow.addMatcher("matcher.json");
            Assertions.assertEquals("::add-matcher::matcher.json", OUT.toString().trim());
        }

        @Test
        final void testMatcherRemove(){
            Workflow.removeMatcher("owner");
            Assertions.assertEquals("::remove-matcher owner=owner::", OUT.toString().trim());
        }

    }

    @Nested
    final class TestCI {

        @Test
        final void testCI(){
            Assertions.assertEquals("true".equals(System.getenv("CI")), Workflow.isCI());
        }

    }

}
