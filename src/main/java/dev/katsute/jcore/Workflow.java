/*
 * Copyright (C) 2021-2022 Katsute <https://github.com/Katsute>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package dev.katsute.jcore;

import java.util.*;
import java.util.function.Supplier;

/**
 * The workflow class replicates the functionality of GitHub workflow commands.
 *
 * @author Katsute
 * @since 1.0.0
 * @version 1.3.0
 */
@SuppressWarnings("GrazieInspection")
public abstract class Workflow {

    private Workflow(){ }

    // ----- variables ---------------

    /**
     * Hides a certain phrase from the logs. Alias for {@link #setSecret(String)}.
     *
     * @param mask the phrase to hide
     *
     * @see #setSecret(String)
     * @since 1.0.0
     */
    public static void addMask(final String mask){
        setSecret(mask);
    }

    /**
     * Hides a certain phrase from the logs.
     *
     * @param secret the phrase to hide
     *
     * @see #addMask(String)
     * @since 1.0.0
     */
    public static void setSecret(final String secret){
        issueCommand("add-mask", secret);
    }

    /**
     * Returns a specified workflow input or null.
     *
     * @param name name of input
     *
     * @return value of input or null
     *
     * @see #getInput(String, boolean)
     * @see #getInput(String, boolean, boolean)
     * @since 1.0.0
     */
    public static String getInput(final String name){
        return getInput(name, false, true);
    }

    /**
     * Returns a specified workflow input or null.
     *
     * @param name name of input
     * @param required if true, a {@link NullPointerException} will be thrown if the value is null
     * @return value of input
     *
     * @see #getInput(String)
     * @see #getInput(String, boolean, boolean)
     * @since 1.0.0
     */
    public static String getInput(final String name, final boolean required){
        return getInput(name, required, true);
    }

    /**
     * Returns a specified workflow input or null.
     *
     * @param name name of input
     * @param required if true, a {@link NullPointerException} will be thrown if the value is null
     * @param trimWhitespace whether to trim the value or not, true by default
     * @return value of input
     *
     * @see #getInput(String)
     * @see #getInput(String, boolean)
     * @since 1.0.0
     */
    public static String getInput(final String name, final boolean required, final boolean trimWhitespace){
        final String input = name != null ? "INPUT_" + name.replace(' ', '_').toUpperCase() : "";
        final String value = System.getenv(input);
        if(required && value == null)
            throw new NullPointerException("Input '" + name + "' is required and not supplied");
        else
            return value != null
                ? trimWhitespace
                    ? value.trim()
                    : value
                : null;
    }

    /**
     * Returns a specified workflow input split into lines.
     *
     * @param name name of input
     * @return value of input in lines
     *
     * @see #getMultilineInput(String, boolean)
     * @see #getMultilineInput(String, boolean, boolean)
     * @since 1.0.0
     */
    public static String[] getMultilineInput(final String name){
        return getMultilineInput(name, false, true);
    }

    /**
     * Returns a specified workflow input split into lines.
     *
     * @param name name of input
     * @param required if true, a {@link NullPointerException} will be thrown if the value is null
     * @return value of input in lines
     *
     * @see #getMultilineInput(String)
     * @see #getMultilineInput(String, boolean, boolean)
     * @since 1.0.0
     */
    public static String[] getMultilineInput(final String name, final boolean required){
        return getMultilineInput(name, required, true);
    }

    /**
     * Returns a specified workflow input split into lines.
     *
     * @param name name of input
     * @param required if true, a {@link NullPointerException} will be thrown if the value is null
     * @param trimWhitespace whether to trim the whole value or not, true by default
     * @return value of input in lines
     *
     * @see #getMultilineInput(String)
     * @see #getMultilineInput(String, boolean)
     * @since 1.0.0
     */
    public static String[] getMultilineInput(final String name, final boolean required, final boolean trimWhitespace){
        final String input = getInput(name, required, trimWhitespace);
        return input == null
            ? new String[0]
            : Arrays.stream(input
                .split("\\n"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }

    /**
     * Returns the value of a workflow input as a boolean.
     *
     * @param name name of input
     * @return value of input
     *
     * @see #getBooleanInput(String, boolean)
     * @see #getBooleanInput(String, boolean, boolean)
     * @since 1.0.0
     */
    public static boolean getBooleanInput(final String name){
        return getBooleanInput(name, false, true);
    }

    /**
     * Returns the value of a workflow input as a boolean.
     *
     * @param name name of input
     * @param required if true, a {@link NullPointerException} will be thrown if the value is null
     * @return value of input
     *
     * @see #getBooleanInput(String)
     * @see #getBooleanInput(String, boolean, boolean)
     * @since 1.0.0
     */
    public static boolean getBooleanInput(final String name, final boolean required){
        return getBooleanInput(name, required, true);
    }

    /**
     * Returns the value of a workflow input as a boolean.
     *
     * @param name name of input
     * @param required if true, a {@link NullPointerException} will be thrown if the value is null
     * @param trimWhitespace whether to trim the value or not, true by default
     * @return value of input
     *
     * @see #getBooleanInput(String)
     * @see #getBooleanInput(String, boolean)
     * @since 1.0.0
     */
    public static boolean getBooleanInput(final String name, final boolean required, final boolean trimWhitespace){
        final String input = getInput(name, required, trimWhitespace);

        if(input != null)
            if(input.equalsIgnoreCase("true"))
                return true;
            else if(input.equalsIgnoreCase("false"))
                return false;
            else
                throw new IllegalArgumentException("Input '" + name + "' is not a boolean type");
        else
            return false;
    }

    /**
     * Sets the workflow step output.
     *
     * @param name output key name
     * @param value output value
     *
     * @since 1.0.0
     */
    public static void setOutput(final String name, final Object value){
        issueCommand("set-output", new LinkedHashMap<String,Object>(){{
            put("name", name);
        }}, value);
    }

    /**
     * Toggles command echo. This does not disable commands.
     *
     * @param enabled whether commands are echoed or not
     *
     * @since 1.0.0
     */
    public static void setCommandEcho(final boolean enabled){
        issueCommand("echo", enabled ? "on" : "off");
    }

    // ----- results ---------------

    /**
     * Prints an error message and sets the error code to 1.
     *
     * @param error error message
     *
     * @see #error(String)
     * @see #error(Throwable)
     * @see #errorSupplier(String)
     * @see #throwError(Throwable)
     * @since 1.0.0
     */
    public static void setFailed(final String error){
        final Throwable throwable = new Throwable();
        error(Arrays.copyOfRange(throwable.getStackTrace(), 1, throwable.getStackTrace().length), error);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.exit(1)));
    }

    // ----- logging commands ---------------

    /**
     * Prints a message.
     *
     * @param message message to print
     *
     * @since 1.0.0
     */
    public static void info(final String message){
        System.out.println(message);
    }

    /**
     * Returns if the runner is in debug mode.
     *
     * @return debug status
     *
     * @since 1.0.0
     */
    public static boolean isDebug(){
        return "1".equals(System.getenv("RUNNER_DEBUG"));
    }

    /**
     * Prints a debug message.
     *
     * @param debug message to print
     *
     * @since 1.0.0
     */
    public static void debug(final String debug){
        issueCommand("debug", debug);
    }

    /**
     * Prints a notice annotation.
     *
     * @param notice message to print
     *
     * @see #notice(String)
     * @see #noticeSupplier(String)
     * @see #noticeSupplier(String, AnnotationProperties)
     * @since 1.1.0
     */
    public static void notice(final String notice){
        notice(notice, null);
    }

    /**
     * Prints a notice annotation.
     *
     * @param notice message to print
     * @param properties optional {@link AnnotationProperties}
     *
     * @see AnnotationProperties
     * @see #notice(String)
     * @see #noticeSupplier(String)
     * @see #noticeSupplier(String, AnnotationProperties)
     * @since 1.1.0
     */
    public static void notice(final String notice, final AnnotationProperties properties){
        issueCommand("notice", properties == null ? null : new LinkedHashMap<String,Object>(){{
            if(properties.title != null)
                put("title", properties.title);
            if(properties.file != null)
                put("file", properties.file);
            if(properties.startColumn != null)
                put("col", properties.startColumn);
            if(properties.endColumn != null)
                put("endColumn", properties.endColumn);
            if(properties.startLine != null)
                put("line", properties.startLine);
            if(properties.endLine != null)
                put("endLine", properties.endLine);
        }}, notice);
    }

    /**
     * Creates a supplier that returns a notice message. Prints notice if running on CI.
     *
     * @param notice notice to print
     * @return notice message
     *
     * @see #notice(String)
     * @see #notice(String, AnnotationProperties)
     * @see #noticeSupplier(String, AnnotationProperties)
     * @since 1.2.0
     */
    public static Supplier<String> noticeSupplier(final String notice){
        return noticeSupplier(notice, null);
    }

    /**
     * Creates a supplier that returns a notice message. Prints notice if running on CI.
     *
     * @param notice notice to print
     * @param properties optional {@link AnnotationProperties}
     * @return notice message
     *
     * @see AnnotationProperties
     * @see #notice(String)
     * @see #notice(String, AnnotationProperties)
     * @see #noticeSupplier(String)
     * @since 1.2.0
     */
    public static Supplier<String> noticeSupplier(final String notice, AnnotationProperties properties){
        return () -> {
            if(CI)
                notice(notice, properties);
            return notice;
        };
    }

    /**
     * Prints a warning message.
     *
     * @param warning message to print
     *
     * @see #warning(String, AnnotationProperties)
     * @see #warning(Throwable)
     * @see #warningSupplier(String)
     * @see #throwWarning(Throwable)
     * @since 1.0.0
     */
    public static void warning(final String warning){
        final Throwable throwable = new Throwable();
        warning(Arrays.copyOfRange(throwable.getStackTrace(), 1, throwable.getStackTrace().length), warning);
    }

    /**
     * Prints a warning message.
     *
     * @param warning message to print
     * @param properties optional {@link AnnotationProperties}
     *
     * @see AnnotationProperties
     * @see #warning(String)
     * @see #warning(Throwable)
     * @see #warningSupplier(String)
     * @see #throwWarning(Throwable)
     * @since 1.1.0
     */
    public static void warning(final String warning, final AnnotationProperties properties){
        issueCommand("warning", properties == null ? null : new LinkedHashMap<String,Object>(){{
            if(properties.title != null)
                put("title", properties.title);
            if(properties.file != null)
                put("file", properties.file);
            if(properties.startColumn != null)
                put("col", properties.startColumn);
            if(properties.endColumn != null)
                put("endColumn", properties.endColumn);
            if(properties.startLine != null)
                put("line", properties.startLine);
            if(properties.endLine != null)
                put("endLine", properties.endLine);
        }}, warning);
    }

    /**
     * Prints a warning message.
     *
     * @param throwable throwable
     *
     * @see #warning(String)
     * @see #warning(String, AnnotationProperties)
     * @see #warningSupplier(String)
     * @see #throwWarning(Throwable)
     * @since 1.0.0
     */
    public static void warning(final Throwable throwable){
        warning(throwable.getStackTrace(), throwable.getMessage());
    }

    /**
     * Prints a warning message and rethrows the exception.
     *
     * @param throwable throwable
     * @param <T> type of throwable
     * @throws T throwable
     *
     * @see #warning(String)
     * @see #warning(String, AnnotationProperties)
     * @see #warning(Throwable)
     * @see #warningSupplier(String)
     * @since 1.0.0
     */
    public static <T extends Throwable> void throwWarning(final T throwable) throws T{
        warning(throwable);
        throw throwable;
    }

    /**
     * Creates a supplier that returns a warning message. Prints warning if running on CI.
     *
     * @param warning message to print
     * @return warning message
     *
     * @see #warning(String)
     * @see #warning(String, AnnotationProperties)
     * @see #warning(Throwable)
     * @see #throwWarning(Throwable)
     * @since 1.0.0
     */
    public static Supplier<String> warningSupplier(final String warning){
        final Throwable throwable = new Throwable();
        return () -> {
            if(CI)
                warning(Arrays.copyOfRange(throwable.getStackTrace(), 1, throwable.getStackTrace().length), warning);
            return warning;
        };
    }

    private static void warning(final StackTraceElement[] trace, final String message){
        issueCommand("warning", new LinkedHashMap<String,Object>(){{
            put("file", getFile(trace[0]));
            put("line", trace[0].getLineNumber());
            put("col", 1);
        }}, getTraceMessage(trace, message));
    }

    /**
     * Prints an error message.
     *
     * @param error message to print
     *
     * @see #error(String, AnnotationProperties)
     * @see #error(Throwable)
     * @see #errorSupplier(String)
     * @see #throwError(Throwable)
     * @see #setFailed(String)
     * @since 1.0.0
     */
    public static void error(final String error){
        final Throwable throwable = new Throwable();
        error(Arrays.copyOfRange(throwable.getStackTrace(), 1, throwable.getStackTrace().length), error);
    }

    /**
     * Prints an error message.
     *
     * @param error message to print
     * @param properties optional {@link AnnotationProperties}
     *
     * @see AnnotationProperties
     * @see #error(String)
     * @see #error(Throwable)
     * @see #errorSupplier(String)
     * @see #throwError(Throwable)
     * @see #setFailed(String)
     * @since 1.1.0
     */
    public static void error(final String error, final AnnotationProperties properties){
        issueCommand("error", properties == null ? null : new LinkedHashMap<String,Object>(){{
            if(properties.title != null)
                put("title", properties.title);
            if(properties.file != null)
                put("file", properties.file);
            if(properties.startColumn != null)
                put("col", properties.startColumn);
            if(properties.endColumn != null)
                put("endColumn", properties.endColumn);
            if(properties.startLine != null)
                put("line", properties.startLine);
            if(properties.endLine != null)
                put("endLine", properties.endLine);
        }}, error);
    }

    /**
     * Prints an error message.
     *
     * @param throwable throwable
     *
     * @see #error(String, AnnotationProperties)
     * @see #error(String)
     * @see #errorSupplier(String)
     * @see #throwError(Throwable)
     * @see #setFailed(String)
     * @since 1.0.0
     */
    public static void error(final Throwable throwable){
        error(throwable.getStackTrace(), throwable.getMessage());
    }

    /**
     * Prints an error message and rethrows the exception.
     *
     * @param throwable throwable
     * @param <T> type of throwable
     * @throws T throwable
     *
     * @see #error(String)
     * @see #error(String, AnnotationProperties)
     * @see #error(Throwable)
     * @see #errorSupplier(String)
     * @see #setFailed(String)
     * @since 1.0.0
     */
    public static <T extends Throwable> void throwError(final T throwable) throws T{
        error(throwable);
        throw throwable;
    }

    /**
     * Creates a supplier that returns an error message. Prints error if running on CI.
     *
     * @param error message to print
     * @return error message
     *
     * @see #error(String)
     * @see #error(String, AnnotationProperties)
     * @see #error(Throwable)
     * @see #throwError(Throwable)
     * @see #setFailed(String)
     * @since 1.0.0
     */
    public static Supplier<String> errorSupplier(final String error){
        final Throwable throwable = new Throwable();
        return () -> {
            if(CI)
                error(Arrays.copyOfRange(throwable.getStackTrace(), 1, throwable.getStackTrace().length), error);
            return error;
        };
    }

    private static void error(final StackTraceElement[] trace, final String message){
        issueCommand("error", new LinkedHashMap<String,Object>(){{
            put("file", getFile(trace[0]));
            put("line", trace[0].getLineNumber());
            put("col", 1);
        }}, getTraceMessage(trace, message));
    }

    /**
     * Starts a group.
     *
     * @param name name of group
     *
     * @see #startGroup(String, Runnable)
     * @see #endGroup()
     * @since 1.0.0
     */
    public static void startGroup(final String name){
        issueCommand("group", name);
    }

    /**
     * Starts a group in a {@link Runnable} and ends it after it finishes.
     *
     * @param name name of group
     * @param runnable runnable
     *
     * @see #startGroup(String)
     * @since 1.0.0
     */
    public static void startGroup(final String name, final Runnable runnable){
        startGroup(name);
        try{
            runnable.run();
        }finally{
            endGroup();
        }
    }

    /**
     * Ends the currently opened group.
     *
     * @see #startGroup(String)
     * @since 1.0.0
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static void endGroup(){
        issueCommand("endgroup");
    }

    // ----- state ---------------

    /**
     * Saves a state.
     *
     * @param name name of state
     * @param value state value
     *
     * @see #getState(String)
     * @since 1.0.0
     */
    public static void saveState(final String name, final Object value){
        issueCommand("save-state", new LinkedHashMap<String,Object>(){{
            put("name", name);
        }}, value);
    }

    /**
     * Retrieves a state.
     *
     * @param state name of state
     * @return state value
     *
     * @see #saveState(String, Object)
     * @since 1.0.0
     */
    public static String getState(final String state){
        return System.getenv("STATE_" + state);
    }

    // ----- commands ---------------

    /**
     * Stops workflow commands until a token is passed.
     *
     * @param token token to restart commands
     *
     * @see #startCommand(String)
     * @since 1.0.0
     */
    public static void stopCommand(final String token){
        issueCommand("stop-commands", token);
    }

    /**
     * Starts workflow commands.
     *
     * @param token token used to stop commands
     *
     * @see #stopCommand(String)
     * @since 1.0.0
     */
    public static void startCommand(final String token){
        issueCommand(token);
    }

    /**
     * Adds a matcher.
     *
     * @param matcher path to matcher json
     *
     * @see #removeMatcher(String)
     * @since 1.0.0
     */
    public static void addMatcher(final String matcher){
        issueCommand("add-matcher", null, matcher);
    }

    /**
     * Removes a matcher.
     *
     * @param owner the owner of the matcher as specified in the json
     *
     * @see #addMatcher(String)
     * @since 1.0.0
     */
    public static void removeMatcher(final String owner){
        issueCommand("remove-matcher", new LinkedHashMap<String,Object>(){{
            put("owner", owner);
        }}, null);
    }

    // ----- test integration ---------------

    private static final Class<AssertionError> assertion = AssertionError.class;

    public static void annotateTest(final ThrowingRunnable runnable){
        try{
            runnable.run();
        }catch(final Throwable e){
            final boolean assumption = e.getClass().getSimpleName().equals("TestAbortedException");
            final boolean assertion  = Workflow.assertion.isAssignableFrom(e.getClass());
            final StackTraceElement[] trace = assumption || assertion ? Arrays.copyOfRange(e.getStackTrace(), 3, e.getStackTrace().length): e.getStackTrace();

            if(assumption)
                warning(trace, e.getMessage());
            else
                error(trace, e.getMessage());
            rethrow(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void rethrow(final Throwable e) throws T{
        throw (T) e;
    }

    // ----- utility ---------------

    private static final String workspace   = System.getenv("GITHUB_WORKSPACE");
    private static final String repository  = System.getenv("GITHUB_REPOSITORY");
    private static final String SHA         = System.getenv("GITHUB_SHA");

    private static final boolean CI = "true".equals(System.getenv("CI"));

    @SuppressWarnings("ConstantConditions")
    private static String getFile(final StackTraceElement traceElement){
        return Workflow.class.getClassLoader().getResource(traceElement.getClassName().replace('.', '/') + ".class")
            .getPath()
            .replaceFirst(workspace != null ? workspace : "", "")
            .replaceFirst("^/", "")
            .replaceFirst("target/test-classes", "src/test/java")
            .replaceFirst("target/classes", "src/main/java")
            .replaceAll("class$", "java");
    }

    private static String getTraceMessage(final StackTraceElement[] stacktrace, final String message){
        final StackTraceElement cause = stacktrace[0];
        final StringBuilder output = new StringBuilder();
        if(CI)
            output
                .append("https://github.com/")
                .append(repository).append('/').append("blob").append('/')
                .append(SHA).append('/');
        output
            .append(getFile(cause))
            .append("#L").append(cause.getLineNumber());
        if(message != null)
            output.append(" : ").append(message);
        output.append('\n');

        boolean first = true;
        for(final StackTraceElement stackTraceElement : stacktrace){
            if(!first)
                output.append("\n\t").append("at").append(' ');
            first = false;
            output.append(stackTraceElement.toString());
        }

        return output.toString();
    }

    // ----- command ---------------

    private static void issueCommand(final String command){
        issueCommand(command, null, null);
    }

    private static void issueCommand(final String command, final String message){
        issueCommand(command, null, message);
    }

    private static void issueCommand(final String command, final Map<String,Object> properties, final Object message){
        System.out.println(toCommand(command, properties, message));
    }

    private static final String commandString = "::";

    private static String toCommand(final String command, final Map<String,Object> properties, final Object message){
        final StringBuilder commandString = new StringBuilder(Workflow.commandString + (command != null ? command : "missing.command"));

        if(properties != null && !properties.isEmpty()){
            commandString.append(' ');
            boolean first = true;
            for(final String key : properties.keySet()){
                final Object value = properties.get(key);
                if(value != null){
                    if(first)
                        first = false;
                    else
                        commandString.append(',');
                    commandString
                        .append(key)
                        .append('=')
                        .append(
                            toCommandValue(value)
                                .replaceAll("%", "%25")
                                .replaceAll("\\r", "%0D")
                                .replaceAll("\\n", "%0A")
                                .replaceAll(":", "%3A")
                                .replaceAll(",", "%2C")
                        );
                }
            }
        }
        commandString.append(Workflow.commandString);
        commandString.append(
            message != null
                ? toCommandValue(message)
                    .replaceAll("%", "%25")
                    .replaceAll("\\r", "%0D")
                    .replaceAll("\\n", "%0A")
                : ""
        );

        return commandString.toString();
    }

    private static String toCommandValue(final Object obj){
        if(obj == null)
            return "";
        else if(obj instanceof String)
            return (String) obj;
        else
            return obj.toString();
    }

}
