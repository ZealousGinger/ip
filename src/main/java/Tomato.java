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

    private void init() {
        printStartMessage();

        Scanner sc = new Scanner(System.in);
        String input;

        // loop logic
        while(sc.hasNextLine()) {
            input = sc.nextLine();
            String[] splitInput = input.split(" ");
            String cmd = splitInput[0];
            System.out.println(spacer);

            if(cmd.equals("bye")) {
                printExitMessage(); break;
            } else if (cmd.equals("list")) {
                printTasks();
            } else if (cmd.contains("mark")) {
                markTask(splitInput);
            } else {
                Task t = new Task(input);
                tasks.add(t);
                System.out.println(tab + "added: " + input);
            }

            System.out.println(spacer);
        }
    }

    private void printStartMessage() {
        System.out.println(spacer);
        System.out.println(tab + "Hello! I'm Tomato \uD83C\uDF45 ! \n" + tab + "What can I do for you?");
        System.out.println(spacer);
    }

    private void printExitMessage() {
        System.out.println(tab + "Bye. Hope to see you again soon!");
        System.out.println(spacer);
    }

    private void printTasks() {
        for(int i = 0; i < this.tasks.size(); ++i) {
            System.out.println(tab + (i+1) + "." + this.tasks.get(i));
        }
    }

    private void markTask(String[] splitInput) {
        int taskNum = Integer.parseInt(splitInput[1]) - 1;
        if (taskNum >= tasks.size()) {
            System.out.println("That task doesn't exist!"); return;
        }
        Task t = tasks.get(taskNum);
        if(splitInput[0].equals("mark")) {
            t.markAsDone();
            System.out.println(tab + "Nice! I've marked this task as done: ");
        } else if(splitInput[0].equals("unmark")) {
            t.markAsNotdone();
            System.out.println(tab + "OK! I've marked this task as not done yet: ");
        }
        System.out.println(tab + t);
    }
}
