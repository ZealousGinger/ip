package tomato;

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
    private enum Command {
        BYE,
        LIST,
        MARK,
        UNMARK,
        TODO,
        DEADLINE,
        EVENT,
        DELETE,
        FIND
    }

    private TaskList taskList;
    private Storage storage;

    private static final String REGEX_EMPTY = "";
    private static final String REGEX_DEFAULT = "\\|";
    private static final String REGEX_BY = "/by|";
    private static final String REGEX_FROM_TO = "/from|\\\\/to|";
    private static final String REGEX_DEADLINE = "/by|\\|";
    private static final String REGEX_EVENT = "/from|\\/to|\\|";
    private static final String DATE_TIME_FORMAT = "d/M/yyyy HHmm";


    /**
     * Instantiates the Parser class without tasklist or storage, used for parsing by Storage class.
     */
    public Parser() {

    }

    /**
     * Instantiates the Parser class with specified TaskList and Storage.
     * @param taskList list of tasks retrieved from storage.
     * @param storage storage class that handles saving changes of tasks to disk.
     */
    public Parser(TaskList taskList, Storage storage) {
        this.taskList = taskList;
        this.storage = storage;
    }

    private Command parseCommand(String arg) throws TomatoException{
        Command cmd;
        try {
            cmd = Command.valueOf(arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TomatoException("Unknown command given, please try a given command: " +
                    "[bye, list, mark, todo, deadline, event, delete, find]. ");
        }
        return cmd;
    }

    private String handleCommand(Command cmd, String[] args) throws TomatoException {
        String result = "";

        switch (cmd) {
        case BYE:
            return null;
            // Immediately exists the method and stop chat loop.
        case LIST:
            result = handleListTasks();
            break;
        case FIND:
            result = handleFindTasks(args);
            break;
        case MARK:
            result = handleMarkTask(args);
            break;
        case UNMARK:
            result = handleUnmarkTask(args);
            break;
        case TODO:
            result = handleCreateTodo(args);
            break;
        case DEADLINE:
            result = handleCreateDeadline(args);
            break;
        case EVENT:
            result = handleCreateEvent(args);
            break;
        case DELETE:
            result = handleDeleteTask(args);
            break;
        default:
            assert false : "Code execution is not supposed to reach here";
        }

        return result;
    }

    /**
     * Returns a boolean value that represents whether to stop parsing and exit the chatbot loop.
     * Parses the given input string and executes the command if valid.
     * @param input string representing the command to be executed.
     * @return boolean value to stop parsing and exit the chat loop.
     * @throws TomatoException If unable to parse arguments, or invalid arguments.
     */
    public String parseAndExecute(String input) throws TomatoException {
        String[] splitArgs = input.split(" ", 2);
        Command cmd = parseCommand(splitArgs[0]);
        return handleCommand(cmd, splitArgs);
    }

    /**
     * Checks argument length to determine if sufficient arguments are given.
     * @param args String array of arguments.
     * @param len length required by the command.
     * @param commandName string name of the command.
     * @throws TomatoException if insufficient arguments are given.
     */
    public void checkArgLength(String[] args, int len, String commandName) throws TomatoException {
        if (args.length < len) {
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
        checkArgLength(args, 2, String.valueOf(Command.DEADLINE));
        String[] arr = parseSlashDeadline(args[1]);
        checkArgLength(arr, 2, String.valueOf(Command.DEADLINE));
        return arr;
    }

    /** Checks and parses event arguments */
    private String[] parseEvent(String[] args) throws TomatoException {
        checkArgLength(args, 2, String.valueOf(Command.EVENT));
        String[] arr = parseSlashEvent(args[1]);
        checkArgLength(arr, 3, String.valueOf(Command.EVENT));
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
            throw new TomatoException("Unable to parse date!");
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

    /** Returns current list of tasks */
    private String handleListTasks() {
        return taskList.toString();
    }

    /**
     * Parses and handles create Todo.
     * @param args string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private String handleCreateTodo(String[] args) throws TomatoException {
        checkArgLength(args, 2, String.valueOf(Command.TODO));
        String res = taskList.createTodo(args[1]);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles create Deadline.
     * @param args string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private String handleCreateDeadline(String[] args) throws TomatoException {
        String[] deadlineArgs = parseDeadline(args);
        LocalDateTime dateTime = parseDate(deadlineArgs[1]);

        String res = taskList.createDeadline(deadlineArgs[0], dateTime);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles create Event.
     * @param args string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private String handleCreateEvent(String[] args) throws TomatoException {
        String[] eventArgs = parseEvent(args);
        LocalDateTime from = parseDate(eventArgs[1]);
        LocalDateTime to = parseDate(eventArgs[2]);

        String res = taskList.createEvent(eventArgs[0], from, to);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles delete task.
     * @param args string[] of input arguments.
     * @throws TomatoException if task number is not provided or invalid.
     */
    private String handleDeleteTask(String[] args) throws TomatoException {
        int taskNum = parseTaskNo(args[1]);
        String res = taskList.deleteTask(taskNum);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles mark task.
     * @param args string[] of input arguments.
     * @throws TomatoException if task number is not provided or invalid.
     */
    private String handleMarkTask(String[] args) throws TomatoException {
        int taskNum = parseTaskNo(args[1]);
        String res = taskList.markTask(taskNum);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles unmark task.
     * @param args string[] of input arguments.
     * @throws TomatoException if task number is not provided or invalid.
     */
    private String handleUnmarkTask(String[] args) throws TomatoException {
        int taskNum = parseTaskNo(args[1]);
        String res = taskList.unmarkTask(taskNum);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles find task.
     * @param args string[] of input arguments.
     * @throws TomatoException if keyword argument is not provided.
     */
    private String handleFindTasks(String[] args) throws TomatoException {
        checkArgLength(args, 2, String.valueOf(Command.FIND));
        return taskList.printMatchingTasks(args[1]);
    }

    /**
     * Returns a Todo instance from the stored todo string.
     * @param args String arguments e.g. "T|1|read book".
     * @return Todo Task object.
     */
    private Task decodeTodo(String args) {
        String[] splitArgs = parseArgs(args, REGEX_EMPTY);
        return new Todo(splitArgs[2], (Integer.parseInt(splitArgs[1])==1));
    }

    /**
     * Returns a Deadline instance from the stored deadline string.
     * @param args String arguments e.g. "D|1|return books |2025-02-02T19:00".
     * @return Deadline Task object.
     */
    private Task decodeDeadline(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_BY);
        checkArgLength(splitArgs, 2, String.valueOf(Command.DEADLINE));
        LocalDateTime dateTime = parseDate(splitArgs[3]);
        return new Deadline(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), dateTime);
    }

    /**
     * Returns a Event instance from the stored event string.
     * @param args String arguments e.g. "E|0|book shopping |2025-03-03T16:00|2025-03-03T18:00".
     * @return Event Task object.
     */
    private Task decodeEvent(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_FROM_TO);
        checkArgLength(splitArgs, 3, String.valueOf(Command.EVENT));
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