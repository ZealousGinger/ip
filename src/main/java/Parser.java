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
            throw new TomatoException("Unknown command given, please try a given command: [bye, list, mark, todo, deadline, event, delete]. ");
        }
        switch (enumCmd) {
            case BYE:
                Ui.printExitMessage();
                return true;
            case LIST:
                this.taskList.printTasks();
                break;
            case MARK:
            case UNMARK:
                this.taskList.markTask(splitInput);
                this.storage.saveToDisk(this.taskList.getTaskList());
                break;
            case TODO:
                if (splitInput.length == 1) {
                    throw new TomatoException("Todo description is required! Please provide it.");
                }
                this.taskList.createTodo(splitInput[1]);
                this.storage.saveToDisk(this.taskList.getTaskList());
                break;
            case DEADLINE:
                if (splitInput.length == 1) {
                    throw new TomatoException("Deadline arguments is required! Please provide it.");
                }
                this.taskList.createDeadline(splitInput[1]);
                this.storage.saveToDisk(this.taskList.getTaskList());
                break;
            case EVENT:
                if (splitInput.length == 1) {
                    throw new TomatoException("event arguments is required! Please provide it.");
                }
                this.taskList.createEvent(splitInput[1]);
                this.storage.saveToDisk(this.taskList.getTaskList());
                break;
            case DELETE:
                this.taskList.deleteTask(splitInput[1]);
                this.storage.saveToDisk(this.taskList.getTaskList());
                break;
        }
        return false;
    }
}