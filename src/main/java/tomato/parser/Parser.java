package tomato.parser;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import tomato.TomatoException;
import tomato.commands.ByeCommand;
import tomato.commands.Command;
import tomato.commands.DeadlineCommand;
import tomato.commands.DeleteCommand;
import tomato.commands.EventCommand;
import tomato.commands.FindCommand;
import tomato.commands.InvalidCommand;
import tomato.commands.ListCommand;
import tomato.commands.MarkCommand;
import tomato.commands.TodoCommand;
import tomato.commands.UnmarkCommand;
import tomato.commands.UpdateCommand;
import tomato.commands.UpdateDeadlineCommand;
import tomato.commands.UpdateDescriptionCommand;
import tomato.commands.UpdateEventFromCommand;
import tomato.commands.UpdateEventTimeCommand;
import tomato.commands.UpdateEventToCommand;
import tomato.task.Deadline;
import tomato.task.Event;
import tomato.task.Task;
import tomato.task.Todo;

/**
 * Parses user input and constructs command objects.
 */
public class Parser {
    private static final String REGEX_EMPTY = "";
    private static final String REGEX_DEFAULT = "\\|";
    private static final String REGEX_BY = "/by|";
    private static final String REGEX_FROM_TO = "/from|\\\\/to|";
    private static final String REGEX_DEADLINE = "/by|\\|";
    private static final String REGEX_EVENT = "/from|\\/to|\\|";
    private static final String DATE_TIME_FORMAT = "d/M/yyyy HHmm";


    /**
     * Creates a parser instance.
     */
    public Parser() {
    }

    /**
     * Returns a command parsed from the given user input.
     *
     * @param input string representing the command to be executed.
     * @return Parsed command.
     * @throws TomatoException If parsing fails due to invalid arguments.
     */
    public Command parse(String input) throws TomatoException {
        String[] args = input.split(" ", 2);
        String stringCommand = args[0].toLowerCase();

        switch (stringCommand) {
        case ByeCommand.COMMAND_WORD:
            return new ByeCommand();
        case ListCommand.COMMAND_WORD:
            return new ListCommand();
        case FindCommand.COMMAND_WORD:
            return handleFindTasks(args);
        case MarkCommand.COMMAND_WORD:
            return handleMarkTask(args);
        case UnmarkCommand.COMMAND_WORD:
            return handleUnmarkTask(args);
        case TodoCommand.COMMAND_WORD:
            return handleCreateTodo(args);
        case DeadlineCommand.COMMAND_WORD:
            return handleCreateDeadline(args);
        case EventCommand.COMMAND_WORD:
            return handleCreateEvent(args);
        case DeleteCommand.COMMAND_WORD:
            return handleDeleteTask(args);
        case UpdateCommand.COMMAND_WORD:
            return handleUpdateTask(args);
        default:
            return handleInvalidCommand(args);
        }
    }

    /**
     * Checks whether the given argument array has the required length.
     *
     * @param args String array of arguments.
     * @param len length required by the command.
     * @param cmdUsage string name of the command.
     * @throws TomatoException If insufficient arguments are given.
     */
    public void checkArgLength(String[] args, int len, String cmdUsage) throws TomatoException {
        if (args.length < len) {
            throw new TomatoException("Incorrect number of arguments provided! " +
                    "Please give the correct arguments.\n" + cmdUsage);
        }
    }

    /**
     * Returns arguments split by the given keyword pattern.
     *
     * @param arg input string.
     * @param keyword regex to split arguments.
     * @return array of string.
     */
    public String[] parseArgs(String arg, String keyword) {
        return arg.split(keyword + REGEX_DEFAULT);
    }

    /**
     * Returns deadline arguments split by `/by`.
     *
     * @param arg Raw deadline argument string.
     * @return Split argument array.
     */
    private String[] parseSlashDeadline(String arg) {
        return arg.split(REGEX_DEADLINE);
    }

    /**
     * Returns event arguments split by `/from` and `/to`.
     *
     * @param arg Raw event argument string.
     * @return Split argument array.
     */
    private String[] parseSlashEvent(String arg) {
        return arg.split(REGEX_EVENT);
    }

    /**
     * Returns validated and parsed arguments for creating a deadline task.
     *
     * @param args Raw command arguments.
     * @return Parsed deadline arguments.
     * @throws TomatoException If the arguments are invalid.
     */
    private String[] parseDeadline(String[] args) throws TomatoException {
        checkArgLength(args, 2, DeadlineCommand.MESSAGE_USAGE);
        String[] parsedArgs = parseSlashDeadline(args[1]);
        checkArgLength(parsedArgs, 2, DeadlineCommand.MESSAGE_USAGE);
        return parsedArgs;
    }

    /**
     * Returns validated and parsed arguments for creating an event task.
     *
     * @param args Raw command arguments.
     * @return Parsed event arguments.
     * @throws TomatoException If the arguments are invalid.
     */
    private String[] parseEvent(String[] args) throws TomatoException {
        checkArgLength(args, 2, EventCommand.MESSAGE_USAGE);
        String[] parsedArgs = parseSlashEvent(args[1]);
        checkArgLength(parsedArgs, 3, EventCommand.MESSAGE_USAGE);
        return parsedArgs;
    }

    /**
     * Returns a parsed date-time from the given input string.
     *
     * @param arg String input.
     * @return LocalDateTime parsed object.
     * @throws TomatoException If unable to parse input.
     */
    public LocalDateTime parseDate(String arg) throws TomatoException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        String trimmedArg = arg.trim();

        try {
            return LocalDateTime.parse(trimmedArg, formatter);
        } catch (DateTimeException exception) {
            // move on and try the second parser
        }

        try {
            return LocalDateTime.parse(trimmedArg);
        } catch (DateTimeException exception) {
            throw new TomatoException("Unable to parse date: " + arg + "\n" +
                    "Please give the datetime in the following format: " +
                    "DD-MM-YYYY HHMM (e.g. 2/10/2025 1900)", arg);
        }
    }

    /**
     * Returns a zero-based task index parsed from the given task number input.
     *
     * @param arg Input task number string.
     * @return Zero-based task index.
     * @throws TomatoException If the input is not a valid task number.
     */
    private int parseTaskNo(String arg) throws TomatoException {
        try {
            return Integer.parseInt(arg) - 1;
        } catch (Exception exception) {
            throw new TomatoException("You must provide a task number!");
        }
    }

    /**
     * Returns a command for creating a todo task.
     *
     * @param args string[] of input arguments.
     * @return Todo command.
     * @throws TomatoException If arguments are insufficient or invalid.
     */
    private Command handleCreateTodo(String[] args) throws TomatoException {
        checkArgLength(args, 2, TodoCommand.MESSAGE_USAGE);
        String description = args[1];
        return new TodoCommand(description);
    }

    /**
     * Returns a command for creating a deadline task.
     *
     * @param args string[] of input arguments.
     * @return Deadline command.
     * @throws TomatoException If arguments are insufficient or invalid.
     */
    private Command handleCreateDeadline(String[] args) throws TomatoException {
        String[] deadlineArgs = parseDeadline(args);
        String description = deadlineArgs[0];
        LocalDateTime dateTime = parseDate(deadlineArgs[1]);
        return new DeadlineCommand(description, dateTime);
    }

    /**
     * Returns a command for creating an event task.
     *
     * @param args string[] of input arguments.
     * @return Event command.
     * @throws TomatoException If arguments are insufficient or invalid.
     */
    private Command handleCreateEvent(String[] args) throws TomatoException {
        String[] eventArgs = parseEvent(args);
        String description = eventArgs[0];
        LocalDateTime from = parseDate(eventArgs[1]);
        LocalDateTime to = parseDate(eventArgs[2]);
        return new EventCommand(description, from, to);
    }

    /**
     * Returns a command for deleting a task.
     *
     * @param args string[] of input arguments.
     * @return Delete command.
     * @throws TomatoException If task number is not provided or invalid.
     */
    private Command handleDeleteTask(String[] args) throws TomatoException {
        checkArgLength(args, 2, DeleteCommand.MESSAGE_USAGE);
        int taskNum = parseTaskNo(args[1]);
        return new DeleteCommand(taskNum);
    }

    /**
     * Returns a command for marking a task as done.
     *
     * @param args string[] of input arguments.
     * @return Mark command.
     * @throws TomatoException If task number is not provided or invalid.
     */
    private Command handleMarkTask(String[] args) throws TomatoException {
        checkArgLength(args, 2, MarkCommand.MESSAGE_USAGE);
        int taskNum = parseTaskNo(args[1]);
        return new MarkCommand(taskNum);
    }

    /**
     * Returns a command for marking a task as not done.
     *
     * @param args string[] of input arguments.
     * @return Unmark command.
     * @throws TomatoException If task number is not provided or invalid.
     */
    private Command handleUnmarkTask(String[] args) throws TomatoException {
        checkArgLength(args, 2, UnmarkCommand.MESSAGE_USAGE);
        int taskNum = parseTaskNo(args[1]);
        return new UnmarkCommand(taskNum);
    }

    /**
     * Returns a command for finding tasks by keyword.
     *
     * @param args string[] of input arguments.
     * @return Find command.
     * @throws TomatoException If keyword argument is not provided.
     */
    private Command handleFindTasks(String[] args) throws TomatoException {
        checkArgLength(args, 2, FindCommand.MESSAGE_USAGE);
        String keyword = args[1];
        return new FindCommand(keyword);
    }

    private UpdateCommand.Argument parseUpdateArgument(String arg) {
        UpdateCommand.Argument enumArg;
        try {
            enumArg = UpdateCommand.Argument.valueOf(arg.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new RuntimeException(exception);
        }
        return enumArg;
    }

    private int parseUpdateTaskNum(String[] args) throws TomatoException {
        checkArgLength(args, 2, UpdateCommand.MESSAGE_USAGE);
        String[] updateArgs = args[1].split(" /");
        return parseTaskNo(updateArgs[0]);
    }

    private String[] parseUpdateArgValues(String[] args) throws TomatoException {
        String[] updateArgs = args[1].split(" /");
        return updateArgs[1].split(" ", 2);
    }

    private String[] parseUpdateTime(String[] args) throws TomatoException {
        checkArgLength(args, 2, UpdateCommand.MESSAGE_USAGE);
        String[] updateArgs = args[1].split(" /");
        checkArgLength(updateArgs, 3, UpdateCommand.MESSAGE_USAGE);
        String[] timeArgs = updateArgs[2].split(" ", 2);
        checkArgLength(timeArgs, 2, UpdateCommand.MESSAGE_USAGE);
        String[] argAndValues = parseUpdateArgValues(args);
        String from = argAndValues[1];
        String to = timeArgs[1];
        return new String[]{from, to};
    }
    private Command handleUpdateTask(String[] args) throws TomatoException {
        int taskNum = parseUpdateTaskNum(args);
        String[] argAndValues = parseUpdateArgValues(args);
        UpdateCommand.Argument arg = parseUpdateArgument(argAndValues[0]);

        switch (arg) {
            case DESCRIPTION:
                checkArgLength(argAndValues, 2, UpdateDescriptionCommand.MESSAGE_USAGE);
                return new UpdateCommand(UpdateCommand.Argument.DESCRIPTION, taskNum, argAndValues[1]);
            case BY:
                checkArgLength(argAndValues, 2, UpdateDeadlineCommand.MESSAGE_USAGE);
                return new UpdateCommand(UpdateCommand.Argument.BY, taskNum, parseDate(argAndValues[1]));
            case FROM:
                checkArgLength(argAndValues, 2, UpdateEventFromCommand.MESSAGE_USAGE);
                return new UpdateCommand(UpdateCommand.Argument.FROM, taskNum, parseDate(argAndValues[1]));
            case TO:
                checkArgLength(argAndValues, 2, UpdateEventToCommand.MESSAGE_USAGE);
                return new UpdateCommand(UpdateCommand.Argument.TO, taskNum, parseDate(argAndValues[1]));
            case TIME:
                String[] fromToValues = parseUpdateTime(args);
                checkArgLength(fromToValues, 2, UpdateEventTimeCommand.MESSAGE_USAGE);
                return new UpdateCommand(UpdateCommand.Argument.TIME, taskNum,
                        parseDate(fromToValues[0]), parseDate(fromToValues[1]));
            default:
                throw new TomatoException(UpdateCommand.MESSAGE_USAGE);
        }
    }

    private Command handleInvalidCommand(String[] args) throws TomatoException {
        String invalidCmd = args[0];
        return new InvalidCommand(invalidCmd);
    }

    /**
     * Returns a todo task decoded from the given storage string.
     *
     * @param args String arguments e.g. "T|1|read book".
     * @return Todo Task object.
     * @throws TomatoException If the stored string is invalid.
     */
    private Task decodeTodo(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_EMPTY);
        checkArgLength(splitArgs, 3, TodoCommand.MESSAGE_USAGE);
        return new Todo(splitArgs[2], (Integer.parseInt(splitArgs[1]) == 1));
    }

    /**
     * Returns a deadline task decoded from the given storage string.
     *
     * @param args String arguments e.g. "D|1|return books |2025-02-02T19:00".
     * @return Deadline Task object.
     * @throws TomatoException If the stored string is invalid.
     */
    private Task decodeDeadline(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_BY);
        checkArgLength(splitArgs, 2, DeadlineCommand.MESSAGE_USAGE);
        LocalDateTime dateTime = parseDate(splitArgs[3]);
        return new Deadline(splitArgs[2], (Integer.parseInt(splitArgs[1]) == 1), dateTime);
    }

    /**
     * Returns an event task decoded from the given storage string.
     *
     * @param args String arguments e.g. "E|0|book shopping |2025-03-03T16:00|2025-03-03T18:00".
     * @return Event Task object.
     * @throws TomatoException If the stored string is invalid.
     */
    private Task decodeEvent(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_FROM_TO);
        checkArgLength(splitArgs, 3, EventCommand.MESSAGE_USAGE);
        LocalDateTime from = parseDate(splitArgs[3]);
        LocalDateTime to = parseDate(splitArgs[4]);
        return new Event(splitArgs[2], (Integer.parseInt(splitArgs[1]) == 1), from, to);
    }

    /**
     * Returns a task decoded from split storage fields.
     *
     * @param args array of string.
     * @return Task object.
     * @throws TomatoException If unable to parse arguments.
     */
    public Task decodeTask(String[] args) throws TomatoException {
        switch (args[0]) {
        case "T":
            return decodeTodo(args[1]);
        case "D":
            return decodeDeadline(args[1]);
        case "E":
            return decodeEvent(args[1]);
        default:
            assert false : "code should not reach here";
        }

        assert false : "code should not reach here";
        return null;
    }
}
