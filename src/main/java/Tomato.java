import java.util.ArrayList;
import java.util.Scanner;

public class Tomato {
    private static final String spacer = "   ____________________________________________________________";
    private static final String tab = "    ";
    private ArrayList<Task> tasks = new ArrayList<>();
    private boolean toExit = false;

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
            System.out.println(spacer);
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
        System.out.println(spacer);
        System.out.println(tab + "Hello! I'm Tomato \uD83C\uDF45 !\n" + tab + "What can I do for you?");
        System.out.println(spacer);
    }

    private void printExitMessage() {
        System.out.println(tab + "Bye. Hope to see you again soon!");
        System.out.println(spacer);
    }

    // print all tasks
    private void printTasks() {
        System.out.println(tab + "Here are the tasks in your list:");
        for(int i = 0; i < this.tasks.size(); ++i) {
            System.out.println(tab + (i+1) + "." + this.tasks.get(i));
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
            System.out.println(tab + "Nice! I've marked this task as done:");
        } else if(splitInput[0].equals("unmark")) {
            t.markAsNotdone();
            System.out.println(tab + "OK! I've marked this task as not done yet:");
        }
        System.out.println(tab + t);
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
            System.out.println(tab + "Noted. I've removed this task:");
            System.out.println(tab + taskName);
            System.out.println(numOfTasks());
        } else {
            throw new TomatoException("This task cannot be removed, it doesn't exist!");
        }



    }

    private void AddTask(Task t) {
        this.tasks.add(t);
        System.out.println(tab + "Got it. I've added this task:\n" + tab + t.toString());
        System.out.println(numOfTasks());
    }

    private void createTodo(String args) {
        Task t = new Todo(args);
        AddTask(t);
    }

    private void createDeadline(String args) throws TomatoException {
        String[] splitArgs = args.split("/by");
        if(splitArgs.length < 2) throw new TomatoException("deadline requires more arguments! Please provide them.");
        Task t = new Deadline(splitArgs[0], splitArgs[1]);
        AddTask(t);
    }

    private void createEvent(String args) throws TomatoException {
        String[] splitArgs = args.split("/from|\\/to");
        if(splitArgs.length < 3) throw new TomatoException("event requires more arguments! Please provide them.");
        Task t = new Event(splitArgs[0], splitArgs[1], splitArgs[2]);
        AddTask(t);
    }

    private String numOfTasks() {
        return tab + "Now you have " + tasks.size() + " tasks in the list.";
    }
}
