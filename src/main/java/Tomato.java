import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tomato {
    private static final String SPACER = "   ____________________________________________________________";
    private boolean isExit = false;
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

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

    public Tomato(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            this.tasks = new TaskList(this.storage.load());
        } catch (FileNotFoundException | TaskListException | TomatoException e) {
            this.ui.showLoadingError(e);
            this.tasks = new TaskList();
        }
        this.ui.printStartMessage();
    }

    public static void main(String[] args) {
        Tomato tomato = new Tomato("data/TaskList.txt");
        tomato.run();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        String input;

        while (!isExit) {
            input = sc.nextLine();
            try {
                preParse(input);
            } catch (TomatoException | TaskListException e) {
                this.ui.showLoadingError(e);
            }
            System.out.println(SPACER);
        }
    }

    private void preParse(String input) throws TomatoException {
        String[] splitInput = input.split(" ", 2);
        String cmd = splitInput[0];
        Command enumCmd;
        try {
            enumCmd = Command.valueOf(cmd.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TomatoException("Unknown command given, please try a given command: [bye, list, mark, todo, deadline, event, delete]. ");
        }
        switch (enumCmd) {
        case BYE:
            this.ui.printExitMessage();
            this.isExit = true;
            break;
        case LIST:
            this.tasks.printTasks();
            break;
        case MARK:
        case UNMARK:
            this.tasks.markTask(splitInput);
            this.storage.saveToDisk(this.tasks.getTaskList());
            break;
        case TODO:
            if (splitInput.length == 1) {
                throw new TomatoException("Todo description is required! Please provide it.");
            }
            this.tasks.createTodo(splitInput[1]);
            this.storage.saveToDisk(this.tasks.getTaskList());
            break;
        case DEADLINE:
            if (splitInput.length == 1) {
                throw new TomatoException("Deadline arguments is required! Please provide it.");
            }
            this.tasks.createDeadline(splitInput[1]);
            this.storage.saveToDisk(this.tasks.getTaskList());
            break;
        case EVENT:
            if (splitInput.length == 1) {
                throw new TomatoException("event arguments is required! Please provide it.");
            }
            this.tasks.createEvent(splitInput[1]);
            this.storage.saveToDisk(this.tasks.getTaskList());
            break;
        case DELETE:
            this.tasks.deleteTask(splitInput[1]);
            this.storage.saveToDisk(this.tasks.getTaskList());
            break;
        }
    }
}