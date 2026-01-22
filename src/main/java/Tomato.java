import java.util.ArrayList;
import java.util.Scanner;

public class Tomato {
    private static final String spacer = "   ____________________________________________________________";
    private static final String tab = "    ";
    private ArrayList<Task> tasks = new ArrayList<>();

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
        while(sc.hasNextLine()) {
            input = sc.nextLine();
            String[] splitInput = input.split(" ", 2);
            String cmd = splitInput[0];
            System.out.println(spacer);

            try {
                if (cmd.equals("bye")) {
                    printExitMessage();
                    break;
                } else if (cmd.equals("list")) {
                    printTasks();
                } else if (cmd.equals("mark") || cmd.equals("unmark")) {
                    markTask(splitInput);
                } else if (cmd.equals("todo")) {
                    if (splitInput.length == 1) {
                        throw new TomatoException("Todo description is required! Please provide it.");
                    }
                    createTodo(splitInput[1]);
                } else if (cmd.equals("deadline")) {
                    if (splitInput.length == 1) {
                        throw new TomatoException("Deadline arguments is required! Please provide it.");
                    }
                    createDeadline(splitInput[1]);
                } else if (cmd.equals("event")) {
                    if (splitInput.length == 1) {
                        throw new TomatoException("event arguments is required! Please provide it.");
                    }
                    createEvent(splitInput[1]);
                } else {
                    throw new TomatoException("Unknown command given, please try a given command: [bye, list, mark, todo, deadline, event]. ");
                }
            } catch (TomatoException e) {
                System.out.println(e);
            }

            System.out.println(spacer);
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
        int taskNum = Integer.parseInt(splitInput[1]) - 1;
        if (taskNum >= tasks.size()) {
            throw new TomatoException("That task number doesn't exist!");
        }
        Task t = tasks.get(taskNum);
        if(splitInput[0].equals("mark")) {
            t.markAsDone();
            System.out.println(tab + "Nice! I've marked this task as done:");
        } else if(splitInput[0].equals("unmark")) {
            t.markAsNotdone();
            System.out.println(tab + "OK! I've marked this task as not done yet:");
        }
        System.out.println(tab + t);
    }

    private void AddTask(Task t) {
        this.tasks.add(t);
        System.out.println(tab + "Got it. I've added this task:\n" + tab + t.toString());
        System.out.println(numOfTasks());
    }

    private void createTodo(String input) {
        Task t = new Todo(input);
        AddTask(t);
    }

    private void createDeadline(String input) throws TomatoException {
        String[] splitInput = input.split("/by");
        if(splitInput.length < 2) throw new TomatoException("deadline requires more arguments! Please provide them.");
        Task t = new Deadline(splitInput[0], splitInput[1]);
        AddTask(t);
    }

    private void createEvent(String input) throws TomatoException {
        String[] splitInput = input.split("/from|\\/to");
        if(splitInput.length < 3) throw new TomatoException("event requires more arguments! Please provide them.");
        Task t = new Event(splitInput[0], splitInput[1], splitInput[2]);
        AddTask(t);
    }

    private String numOfTasks() {
        return tab + "Now you have " + tasks.size() + " tasks in the list.";
    }
}
