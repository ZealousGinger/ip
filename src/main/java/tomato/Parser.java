package tomato;

import commands.*;
import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the Parser class that parses the string input and executes the commands respectively.
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
     * Instantiates the Parser class
     */
    public Parser() {
    }

    /**
     * Returns a boolean value that represents whether to stop parsing and exit the chatbot loop.
     * Parses the given input string and executes the command if valid.
     * @param input string representing the command to be executed.
     * @return boolean value to stop parsing and exit the chat loop.
     * @throws TomatoException If unable to parse arguments, or invalid arguments.
     */
    public Command parse(String input) throws TomatoException {
        String[] args = input.split(" ", 2);
        String stringCommand = args[0].toLowerCase();
        Command cmd = null;

        switch (stringCommand) {
        case ByeCommand.COMMAND_WORD:
            cmd = new ByeCommand();
            break;
        case ListCommand.COMMAND_WORD:
            cmd = new ListCommand();
            break;
        case FindCommand.COMMAND_WORD:
            cmd = handleFindTasks(args);
            break;
        case MarkCommand.COMMAND_WORD:
            cmd = handleMarkTask(args);
            break;
        case UnmarkCommand.COMMAND_WORD:
            cmd = handleUnmarkTask(args);
            break;
        case TodoCommand.COMMAND_WORD:
            cmd = handleCreateTodo(args);
            break;
        case DeadlineCommand.COMMAND_WORD:
            cmd = handleCreateDeadline(args);
            break;
        case EventCommand.COMMAND_WORD:
            cmd = handleCreateEvent(args);
            break;
        case DeleteCommand.COMMAND_WORD:
            cmd = handleDeleteTask(args);
            break;
        case UpdateDescriptionCommand.COMMAND_WORD:
            cmd = handleUpdateDescription(args);
            break;
        case UpdateDeadlineCommand.COMMAND_WORD:
            cmd = handleUpdateDeadlineBy(args);
            break;
        case UpdateEventFromCommand.COMMAND_WORD:
            cmd = handleUpdateEventFrom(args);
            break;
        case UpdateEventToCommand.COMMAND_WORD:
            cmd = handleUpdateEventTo(args);
            break;
        case UpdateEventTimeCommand.COMMAND_WORD:
            cmd = handleUpdateEventTime(args);
            break;
        default:
            cmd = handleInvalidCommand(args);
            break;
        }

        return cmd;
    }

    /**
     * Checks argument length to determine if sufficient arguments are given.
     * @param args String array of arguments.
     * @param len length required by the command.
     * @param commandName string name of the command.
     * @throws TomatoException if insufficient arguments are given.
     */
    public void checkArgLength(String[] args, int len, String commandName) throws TomatoException {
        if (args.length < len && commandName.toLowerCase().contains("update")) {
            throw new TomatoException(commandName + " arguments is required! Please provide it.\n" +
                    "Separate arguments by '|' e.g. 'update_description 1|buy book'\n" +
                    "e.g. update_deadline 2|3/3/2024 1900");
        } else if (args.length < len) {
            throw new TomatoException(commandName + " arguments is required! Please provide it.");
        }
    }

    /**
     * Parse arguments according to regex.
     * @param arg input string.
     * @param keyword regex to split arguments.
     * @return array of string.
     */
    public String[] parseArgs(String arg, String keyword) {
        return arg.split(keyword + REGEX_DEFAULT);
    }

    /** Parses and splits "/by" for deadline Task */
    private String[] parseSlashDeadline(String arg) {
        return arg.split(REGEX_DEADLINE);
    }

    /** Parses and splits "/from" and "/to" for event Task */
    private String[] parseSlashEvent(String arg) {
        return arg.split(REGEX_EVENT);
    }

    /** Checks and parses deadline arguments */
    private String[] parseDeadline(String[] args) throws TomatoException {
        checkArgLength(args, 2, DeadlineCommand.COMMAND_WORD);
        String[] arr = parseSlashDeadline(args[1]);
        checkArgLength(arr, 2, DeadlineCommand.COMMAND_WORD);
        return arr;
    }

    /** Checks and parses event arguments */
    private String[] parseEvent(String[] args) throws TomatoException {
        checkArgLength(args, 2, EventCommand.COMMAND_WORD);
        String[] arr = parseSlashEvent(args[1]);
        checkArgLength(arr, 3, EventCommand.COMMAND_WORD);
        return arr;
    }

    /**
     * Parses string input into LocalDateTime.
     * @param arg String input.
     * @return LocalDateTime parsed object.
     * @throws TomatoException if unable to parse input.
     */
    public LocalDateTime parseDate(String arg) throws TomatoException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        String trimmedArg = arg.trim();

        try {
            return LocalDateTime.parse(trimmedArg, formatter);
        } catch (DateTimeException e) {
            // move on and try the second parser
        }

        try {
            return LocalDateTime.parse(trimmedArg);
        } catch (DateTimeException e) {
            throw new TomatoException("Unable to parse date: " + arg + "\n" +
                    "Please give the datetime in the following format: " +
                    "DD-MM-YYYY HHMM (e.g. 2/10/2025 1900)", arg);
        }
    }

    /** Parses input representing task number and returns task index int */
    private int parseTaskNo(String arg) throws TomatoException {
        try {
            return Integer.parseInt(arg) - 1;
        } catch (Exception e) {
            throw new TomatoException("You must provide a task number!");
        }
    }

    /**
     * Parses and handles create Todo.
     * @param args string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private Command handleCreateTodo(String[] args) throws TomatoException {
        checkArgLength(args, 2, TodoCommand.COMMAND_WORD);
        String description = args[1];
        return new TodoCommand(description);
    }

    /**
     * Parses and handles create Deadline.
     * @param args string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private Command handleCreateDeadline(String[] args) throws TomatoException {
        String[] deadlineArgs = parseDeadline(args);
        String description = deadlineArgs[0];
        LocalDateTime dateTime = parseDate(deadlineArgs[1]);
        return new DeadlineCommand(description, dateTime);
    }

    /**
     * Parses and handles create Event.
     * @param args string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private Command handleCreateEvent(String[] args) throws TomatoException {
        String[] eventArgs = parseEvent(args);
        String description = eventArgs[0];
        LocalDateTime from = parseDate(eventArgs[1]);
        LocalDateTime to = parseDate(eventArgs[2]);
        return new EventCommand(description, from, to);
    }

    /**
     * Parses and handles delete task.
     * @param args string[] of input arguments.
     * @throws TomatoException if task number is not provided or invalid.
     */
    private Command handleDeleteTask(String[] args) throws TomatoException {
        checkArgLength(args, 2, DeleteCommand.COMMAND_WORD);
        int taskNum = parseTaskNo(args[1]);
        return new DeleteCommand(taskNum);
    }

    /**
     * Parses and handles mark task.
     * @param args string[] of input arguments.
     * @throws TomatoException if task number is not provided or invalid.
     */
    private Command handleMarkTask(String[] args) throws TomatoException {
        checkArgLength(args, 2, MarkCommand.COMMAND_WORD);
        int taskNum = parseTaskNo(args[1]);
        return new MarkCommand(taskNum);
    }

    /**
     * Parses and handles unmark task.
     * @param args string[] of input arguments.
     * @throws TomatoException if task number is not provided or invalid.
     */
    private Command handleUnmarkTask(String[] args) throws TomatoException {
        checkArgLength(args, 2, UnmarkCommand.COMMAND_WORD);
        int taskNum = parseTaskNo(args[1]);
        return new UnmarkCommand(taskNum);
    }

    /**
     * Parses and handles find task.
     * @param args string[] of input arguments.
     * @throws TomatoException if keyword argument is not provided.
     */
    private Command handleFindTasks(String[] args) throws TomatoException {
        checkArgLength(args, 2, FindCommand.COMMAND_WORD);
        String keyword = args[1];
        return new FindCommand(keyword);
    }

    /** Parses and handles updating of task description */
    private Command handleUpdateDescription(String[] args) throws TomatoException {
        checkArgLength(args, 2, UpdateDescriptionCommand.COMMAND_WORD);
        String[] descriptionArgs = args[1].split(REGEX_DEFAULT);
        checkArgLength(descriptionArgs, 2, UpdateDescriptionCommand.COMMAND_WORD);
        int taskNum = parseTaskNo(descriptionArgs[0]);
        String description = descriptionArgs[1];
        return new UpdateDescriptionCommand(taskNum, description);
    }

    /** Parses and handles updating of deadline by datetime */
    private Command handleUpdateDeadlineBy(String[] args) throws TomatoException {
        checkArgLength(args, 2, UpdateDeadlineCommand.COMMAND_WORD);
        String[] deadlineArgs = args[1].split(REGEX_DEFAULT);
        checkArgLength(deadlineArgs, 2, UpdateDeadlineCommand.COMMAND_WORD);
        int taskNum = parseTaskNo(deadlineArgs[0]);
        LocalDateTime dateTime = parseDate(deadlineArgs[1]);
        return new UpdateDeadlineCommand(taskNum, dateTime);
    }

    /** Parses and handles updating of event from datetime */
    private Command handleUpdateEventFrom(String[] args) throws TomatoException {
        checkArgLength(args, 2, UpdateEventFromCommand.COMMAND_WORD);
        String[] eventArgs = args[1].split(REGEX_DEFAULT);
        checkArgLength(eventArgs, 2, UpdateEventFromCommand.COMMAND_WORD);
        int taskNum = parseTaskNo(eventArgs[0]);
        LocalDateTime dateTime = parseDate(eventArgs[1]);
        return new UpdateEventFromCommand(taskNum, dateTime);
    }

    /** Parses and handles updating of event to datetime */
    private Command handleUpdateEventTo(String[] args) throws TomatoException {
        checkArgLength(args, 2, UpdateEventToCommand.COMMAND_WORD);
        String[] eventArgs = args[1].split(REGEX_DEFAULT);
        checkArgLength(eventArgs, 2, UpdateEventToCommand.COMMAND_WORD);
        int taskNum = parseTaskNo(eventArgs[0]);
        LocalDateTime dateTime = parseDate(eventArgs[1]);
        return new UpdateEventToCommand(taskNum, dateTime);
    }

    /** Parses and handles updating of event from and to datetimes */
    private Command handleUpdateEventTime(String[] args) throws TomatoException {
        checkArgLength(args, 2, UpdateEventTimeCommand.COMMAND_WORD);
        String[] eventArgs = args[1].split(REGEX_DEFAULT);
        checkArgLength(eventArgs, 3, UpdateEventTimeCommand.COMMAND_WORD);
        int taskNum = parseTaskNo(eventArgs[0]);
        LocalDateTime from = parseDate(eventArgs[1]);
        LocalDateTime to = parseDate(eventArgs[2]);
        return new UpdateEventTimeCommand(taskNum, from, to);
    }

    private Command handleInvalidCommand(String[] args) throws TomatoException {
        String invalidCmd = args[0];
        return new InvalidCommand(invalidCmd);
    }

    /**
     * Returns a Todo instance from the stored todo string.
     * @param args String arguments e.g. "T|1|read book".
     * @return Todo Task object.
     */
    private Task decodeTodo(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_EMPTY);
        checkArgLength(splitArgs, 3, TodoCommand.COMMAND_WORD);
        return new Todo(splitArgs[2], (Integer.parseInt(splitArgs[1])==1));
    }

    /**
     * Returns a Deadline instance from the stored deadline string.
     * @param args String arguments e.g. "D|1|return books |2025-02-02T19:00".
     * @return Deadline Task object.
     */
    private Task decodeDeadline(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_BY);
        checkArgLength(splitArgs, 2, DeadlineCommand.COMMAND_WORD);
        LocalDateTime dateTime = parseDate(splitArgs[3]);
        return new Deadline(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), dateTime);
    }

    /**
     * Returns an Event instance from the stored event string.
     * @param args String arguments e.g. "E|0|book shopping |2025-03-03T16:00|2025-03-03T18:00".
     * @return Event Task object.
     */
    private Task decodeEvent(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_FROM_TO);
        checkArgLength(splitArgs, 3, EventCommand.COMMAND_WORD);
        LocalDateTime from = parseDate(splitArgs[3]);
        LocalDateTime to = parseDate(splitArgs[4]);
        return new Event(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), from, to);
    }

    /**
     * Decodes String from file storage to Task.
     * @param args array of string.
     * @return Task object.
     * @throws TomatoException if unable to parse arguments.
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