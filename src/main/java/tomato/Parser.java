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
            Ui.printExitMessage();
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
        }

        return result;
    }

    /**
     * Parses and handles create Todo.
     * @param splitInput string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private String handleCreateTodo(String[] splitInput) throws TomatoException {
        if (splitInput.length == 1) {
            throw new TomatoException("Todo description is required! Please provide it.");
        }
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
        if (splitInput.length == 1) {
            throw new TomatoException("Deadline arguments is required! Please provide it.");
        }
        String[] args = splitInput[1].split("/by|\\|");
        if (args.length < 2) {
            throw new TomatoException("deadline requires more arguments! Please provide them.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        LocalDateTime dateTime;

        try {
            dateTime = LocalDateTime.parse(args[1].trim(), formatter);
        } catch (DateTimeException e) {
            try {
                dateTime = LocalDateTime.parse(args[1].trim());
            } catch (Exception e2) {
                throw new TomatoException("Unable to parse date!");
            }
        }

        String res = taskList.createDeadline(args[1], dateTime);
        storage.saveToDisk(taskList.getTaskList());
        return res;
    }

    /**
     * Parses and handles create Event.
     * @param splitInput string[] of input arguments.
     * @throws TomatoException if arguments are insufficient or invalid.
     */
    private String handleCreateEvent(String[] splitInput) throws TomatoException {
        if (splitInput.length == 1) {
            throw new TomatoException("event arguments is required! Please provide it.");
        }
        String[] args = splitInput[1].split("/from|\\/to|\\|");
        if(args.length < 3) {
            throw new TomatoException("event requires more arguments! Please provide them.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        LocalDateTime from;
        LocalDateTime to;

        try {
            from = LocalDateTime.parse(args[1].trim(), formatter);
            to = LocalDateTime.parse(args[2].trim(), formatter);
        } catch (DateTimeException e) {

            try {
                from = LocalDateTime.parse(args[1].trim());
                to = LocalDateTime.parse(args[2].trim());
            } catch (Exception e2) {
                throw new TomatoException("Unable to parse date! " +
                        "Please enter a date and time in this format: 2/12/2019 1800");
            }
        }

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
        int taskNum;
        try {
            taskNum = Integer.parseInt(splitInput[1]) - 1;
        } catch (Exception e) {
            throw new TomatoException("You must provide a task number!");
        }

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
        int taskNum;
        try {
            taskNum = Integer.parseInt(splitInput[1]) - 1;
        } catch (Exception e) {
            throw new TomatoException("You must provide a task number!");
        }
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
        int taskNum;
        try {
            taskNum = Integer.parseInt(splitInput[1]) - 1;
        } catch (Exception e) {
            throw new TomatoException("You must provide a task number!");
        }
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
        if (splitInput.length == 1) {
            throw new TomatoException("keyword is required! Please provide it.");
        }
        return taskList.printMatchingTasks(splitInput[1]);
    }
}