package tomato;

public class Parser {
    private enum Command {
        BYE,
        LIST,
        MARK,
        UNMARK,
        TODO,
        DEADLINE,
        EVENT,
        DELETE
    }
    private TaskList taskList;
    private Storage storage;

    public Parser(TaskList taskList, Storage storage) {
        this.taskList = taskList;
        this.storage = storage;
    }

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