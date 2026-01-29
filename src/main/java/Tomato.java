import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class Tomato {
    private static final String SPACER = "   ____________________________________________________________";
    private static final String TAB = "    ";
    private ArrayList<Task> tasks = new ArrayList<>();
    private boolean toExit = false;
    private File taskFile;
    private boolean isLoadingTask;

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

    public static void main(String[] args) {
        Tomato tomato = new Tomato();
        tomato.init();
    }

    // main initialization
    private void init() {
        printStartMessage();
        try {
            this.taskFile = loadTaskFile();
            loadTasks();
        } catch (FileNotFoundException | TomatoException e) {
            if(e instanceof TomatoException) {
                System.out.println("Unable to load tasks due to corrupted/improper format.");
            }
            System.out.println("creating a new file: " + e);
            try {
                this.taskFile = createTaskFile();
            } catch (IOException e2) {
                System.out.println("Error creating file: " + e2);
            }
        }


        Scanner sc = new Scanner(System.in);
        String input;

        // loop logic
        while(!toExit) {
            input = sc.nextLine();
            try {
                preParse(input);
            } catch (TomatoException e) {
                System.out.println(e);
            }
            System.out.println(SPACER);
        }
    }

    private File loadTaskFile() throws FileNotFoundException {
        String root = System.getProperty("user.dir");
        java.nio.file.Path path = java.nio.file.Paths.get(root, "data", "TaskList.txt");
        boolean fileExists = java.nio.file.Files.exists(path);
        if(fileExists) {
            return path.toFile();
        } else {
            throw new FileNotFoundException();
        }
    }

    private File createTaskFile() throws IOException {
        String root = System.getProperty("user.dir");
        java.nio.file.Path dataDir = java.nio.file.Paths.get(root, "data");
        if (!java.nio.file.Files.exists(dataDir)) {
            java.nio.file.Files.createDirectories(dataDir);
        }
        java.nio.file.Path taskListPath = java.nio.file.Paths.get(root, "data", "TaskList.txt");
        if (!java.nio.file.Files.exists(taskListPath)) {
            java.nio.file.Files.createFile(taskListPath);
            System.out.println("Created file: " + taskListPath.toAbsolutePath() + " true");
        }

        return taskListPath.toFile();
    }

    private void loadTasks() throws FileNotFoundException, TomatoException {
        this.isLoadingTask = true;
        System.out.println(TAB + "Loading tasks from storage......................");
        Scanner fileScanner = new Scanner(this.taskFile);
        while (fileScanner.hasNextLine()) {
            String data = fileScanner.nextLine();
            String[] args = data.split("|", 2);
            if(args[0].equals("T")) {
                createTodo(args[1]);
            } else if (args[0].equals("D")) {
                createDeadline(args[1]);
            } else if (args[0].equals("E")) {
                createEvent(args[1]);
            }
        }
        this.isLoadingTask = false;
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
                printExitMessage();
                this.toExit = true;
                break;
            case LIST:
                printTasks();
                break;
            case MARK:
            case UNMARK:
                markTask(splitInput);
                break;
            case TODO:
                if (splitInput.length == 1) {
                    throw new TomatoException("Todo description is required! Please provide it.");
                }
                createTodo(splitInput[1]);
                break;
            case DEADLINE:
                if (splitInput.length == 1) {
                    throw new TomatoException("Deadline arguments is required! Please provide it.");
                }
                createDeadline(splitInput[1]);
                break;
            case EVENT:
                if (splitInput.length == 1) {
                    throw new TomatoException("event arguments is required! Please provide it.");
                }
                createEvent(splitInput[1]);
                break;
            case DELETE:
                deleteTask(splitInput[1]);
                break;
        }
    }

    private void printStartMessage() {
        System.out.println(SPACER);
        System.out.println(TAB + "Hello! I'm Tomato \uD83C\uDF45 !\n" + TAB + "What can I do for you?");
        System.out.println(SPACER);
    }

    private void printExitMessage() {
        System.out.println(TAB + "Bye. Hope to see you again soon!");
        System.out.println(SPACER);
    }

    // print all tasks
    private void printTasks() {
        System.out.println(TAB + "Here are the tasks in your list:");
        for(int i = 0; i < this.tasks.size(); ++i) {
            System.out.println(TAB + (i+1) + "." + this.tasks.get(i));
        }
    }

    // handles mark and unmark command
    private void markTask(String[] splitInput) throws TomatoException {
        int taskNum;
        try {
            taskNum = Integer.parseInt(splitInput[1]) - 1;
        } catch (Exception e) {
            throw new TomatoException("You must provide a task number!");
        }
        if (taskNum >= this.tasks.size()) {
            throw new TomatoException("That task number doesn't exist!");
        }
        Task t = this.tasks.get(taskNum);
        if(splitInput[0].equals("mark")) {
            t.markAsDone();
            System.out.println(TAB + "Nice! I've marked this task as done:");
        } else if(splitInput[0].equals("unmark")) {
            t.markAsNotdone();
            System.out.println(TAB + "OK! I've marked this task as not done yet:");
        }
        System.out.println(TAB + t);
        saveToDisk();
    }

    private void deleteTask(String args) throws TomatoException {
        int taskNum;
        try {
            taskNum = Integer.parseInt(args) - 1;
        } catch (Exception e) {
            throw new TomatoException("You must provide a task number!");
        }

        if (taskNum >= this.tasks.size()) {
            throw new TomatoException("That task number doesn't exist!");
        }
        Task t = this.tasks.get(taskNum);
        String taskName = t.toString();
        if(this.tasks.remove(t)) {
            System.out.println(TAB + "Noted. I've removed this task:");
            System.out.println(TAB + taskName);
            System.out.println(numOfTasks());
            saveToDisk();
        } else {
            throw new TomatoException("This task cannot be removed, it doesn't exist!");
        }



    }

    private void saveToDisk() {
        try {
            FileWriter taskWriter = new FileWriter(this.taskFile);
            for (Task task : this.tasks) {
                taskWriter.write(task.toSave() + "\n");
            }
            taskWriter.close();
            taskWriter.close();
        } catch (IOException e) {
            System.out.println("unable to save to file");
            throw new RuntimeException(e);
        }
    }

    private void AddTask(Task t) {
        this.tasks.add(t);
        System.out.println(TAB + "Got it. I've added this task:\n" + TAB + t.toString());
        System.out.println(numOfTasks());
        if(!this.isLoadingTask) {
            saveToDisk();
        }
    }

    private void createTodo(String args) {
        String[] splitArgs = args.split("\\|");
        Task t;
        if(splitArgs.length > 1) {
            t = new Todo(splitArgs[2], (Integer.parseInt(splitArgs[1])==1));
        } else {
            t = new Todo(args);
        }
        AddTask(t);
    }

    private void createDeadline(String args) throws TomatoException {
        String[] splitArgs = args.split("/by|\\|");
        if(splitArgs.length < 2) throw new TomatoException("deadline requires more arguments! Please provide them.");
        Task t;
        if(splitArgs.length > 2) {
            t = new Deadline(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), splitArgs[3]);
        } else {
            t = new Deadline(splitArgs[0], splitArgs[1]);
        }
        AddTask(t);
    }

    private void createEvent(String args) throws TomatoException {
        String[] splitArgs = args.split("/from|\\/to|\\|");
        if(splitArgs.length < 3) throw new TomatoException("event requires more arguments! Please provide them.");
        Task t;
        if(splitArgs.length > 3) {
            t = new Event(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), splitArgs[3], splitArgs[4]);
        } else {
            t = new Event(splitArgs[0], splitArgs[1], splitArgs[2]);
        }
        AddTask(t);
    }

    private String numOfTasks() {
        return TAB + "Now you have " + tasks.size() + " tasks in the list.";
    }
}
