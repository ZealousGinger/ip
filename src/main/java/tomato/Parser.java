package tomato;

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
    public boolean parseAndExecute(String input) throws TomatoException {
        String[] splitInput = input.split(" ", 2);
        String cmd = splitInput[0];
        Parser.Command enumCmd;

        try {
            enumCmd = Parser.Command.valueOf(cmd.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TomatoException("Unknown command given, please try a given command: " +
                    "[bye, list, mark, todo, deadline, event, delete]. ");
        }

        switch (enumCmd) {
        case BYE:
            Ui.printExitMessage();
            return true;
            // Immediately exists the method and stop chat loop.
        case LIST:
            taskList.printTasks();
            break;
        case FIND:
            taskList.printMatchingTasks(splitInput[1]);
            break;
        case MARK:
            // Fallthrough, mark and unmark handled by markTask method.
        case UNMARK:
            taskList.markTask(splitInput);
            storage.saveToDisk(taskList.getTaskList());
            break;
        case TODO:
            if (splitInput.length == 1) {
                throw new TomatoException("tomato.Todo description is required! Please provide it.");
            }
            taskList.createTodo(splitInput[1]);
            storage.saveToDisk(taskList.getTaskList());
            break;
        case DEADLINE:
            if (splitInput.length == 1) {
                throw new TomatoException("tomato.Deadline arguments is required! Please provide it.");
            }
            taskList.createDeadline(splitInput[1]);
            storage.saveToDisk(taskList.getTaskList());
            break;
        case EVENT:
            if (splitInput.length == 1) {
                throw new TomatoException("event arguments is required! Please provide it.");
            }
            taskList.createEvent(splitInput[1]);
            storage.saveToDisk(taskList.getTaskList());
            break;
        case DELETE:
            taskList.deleteTask(splitInput[1]);
            storage.saveToDisk(taskList.getTaskList());
            break;
        }

        return false;
    }
}