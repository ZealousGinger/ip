package tomato;

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
    private static final String REGEX_DEADLINE = "/by|\\|";
    private static final String REGEX_EVENT = "/from|\\/to|\\|";
    private static final String DATE_TIME_FORMAT = "d/M/yyyy HHmm";


    /**
     * Instantiates the Parser class with specified TaskList and Storage.
     * @param taskList list of tasks retrieved from storage.
     * @param storage storage class that handles saving changes of tasks to disk.
     */
    public Parser(TaskList taskList, Storage storage) {
        this.taskList = taskList;
        this.storage = storage;
    }

    /**
     * Returns a boolean value that represents whether to stop parsing and exit the chatbot loop.
     * Parses the given input string and executes the command if valid.
     * @param input string representing the command to be executed.
     * @return boolean value to stop parsing and exit the chat loop.
     * @throws TomatoException If unable to parse arguments, or invalid arguments.
     */
    public String parseAndExecute(String input) throws TomatoException {
        String[] splitInput = input.split(" ", 2);
        String cmd = splitInput[0];
        Parser.Command enumCmd;

        try {
            enumCmd = Parser.Command.valueOf(cmd.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TomatoException("Unknown command given, please try a given command: " +
                    "[bye, list, mark, todo, deadline, event, delete, find]. ");
        }

        String result = "";

        switch (enumCmd) {
        case BYE:
            return null;
            // Immediately exists the method and stop chat loop.
        case LIST:
            result = taskList.toString();
            break;
        case FIND:
            result = handleFindTasks(splitInput);
            break;
        case MARK:
            result = handleMarkTask(splitInput);
            break;
        case UNMARK:
            result = handleUnmarkTask(splitInput);
            break;
        case TODO:
            result = handleCreateTodo(splitInput);
            break;
        case DEADLINE:
            result = handleCreateDeadline(splitInput);
            break;
        case EVENT:
            result = handleCreateEvent(splitInput);
            break;
        case DELETE:
            result = handleDeleteTask(splitInput);
            break;
        default:
            assert false : "Code execution is not supposed to reach here";
        }

        return result;
    }

    private void checkArgLength(String[] args, int len, String commandName) throws TomatoException {
        if (args.length < len) {
            throw new TomatoException(commandName + " arguments is required! Please provide it.");
        }
    }

    private String[] parseSlashDeadline(String arg) {
        return arg.split(REGEX_DEADLINE);
    }

    private String[] parseSlashEvent(String arg) {
        return arg.split(REGEX_EVENT);
    }

    private String[] parseDeadline(String[] args) throws TomatoException {
        checkArgLength(args, 2, String.valueOf(Command.DEADLINE));
        String[] arr = parseSlashDeadline(args[1]);
        checkArgLength(arr, 2, String.valueOf(Command.DEADLINE));
        return arr;
    }

    private String[] parseEvent(String[] args) throws TomatoException {
        checkArgLength(args, 2, String.valueOf(Command.EVENT));
        String[] arr = parseSlashEvent(args[1]);
        checkArgLength(arr, 3, String.valueOf(Command.EVENT));
        return arr;
    }

    private LocalDateTime parseDate(String arg) throws TomatoException {
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

    private int parseTaskNo(String arg) throws TomatoException {
        try {
            return Integer.parseInt(arg) - 1;
        } catch (Exception e) {
            throw new TomatoException("You must provide a task number!");
        }
    }

    /**
     * Parses and handles create Todo.
     * @param splitInput string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private String handleCreateTodo(String[] splitInput) throws TomatoException {
        checkArgLength(splitInput, 2, String.valueOf(Command.TODO));
        String res = taskList.createTodo(splitInput[1]);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles create Deadline.
     * @param splitInput string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private String handleCreateDeadline(String[] splitInput) throws TomatoException {
        String[] args = parseDeadline(splitInput);
        LocalDateTime dateTime = parseDate(args[1]);

        String res = taskList.createDeadline(args[0], dateTime);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles create Event.
     * @param splitInput string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private String handleCreateEvent(String[] splitInput) throws TomatoException {
        String[] args = parseEvent(splitInput);
        LocalDateTime from = parseDate(args[1]);
        LocalDateTime to = parseDate(args[2]);

        String res = taskList.createEvent(args[0], from, to);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles delete task.
     * @param splitInput string[] of input arguments.
     * @throws TomatoException if task number is not provided or invalid.
     */
    private String handleDeleteTask(String[] splitInput) throws TomatoException {
        int taskNum = parseTaskNo(splitInput[1]);
        String res = taskList.deleteTask(taskNum);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles mark task.
     * @param splitInput string[] of input arguments.
     * @throws TomatoException if task number is not provided or invalid.
     */
    private String handleMarkTask(String[] splitInput) throws TomatoException {
        int taskNum = parseTaskNo(splitInput[1]);
        String res = taskList.markTask(taskNum);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles unmark task.
     * @param splitInput string[] of input arguments.
     * @throws TomatoException if task number is not provided or invalid.
     */
    private String handleUnmarkTask(String[] splitInput) throws TomatoException {
        int taskNum = parseTaskNo(splitInput[1]);
        String res = taskList.unmarkTask(taskNum);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles find task.
     * @param splitInput string[] of input arguments.
     * @throws TomatoException if keyword argument is not provided.
     */
    private String handleFindTasks(String[] splitInput) throws TomatoException {
        checkArgLength(splitInput, 2, String.valueOf(Command.FIND));
        return taskList.printMatchingTasks(splitInput[1]);
    }
}