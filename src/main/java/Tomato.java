import java.util.ArrayList;
import java.util.Scanner;

public class Tomato {
    public static void main(String[] args) {
        String spacer = "   ____________________________________________________________";
        System.out.println(spacer);
        System.out.println("    Hello! I'm Tomato \uD83C\uDF45 ! \n    What can I do for you?");
        System.out.println(spacer);

        Scanner sc = new Scanner(System.in);
        String input;
        ArrayList<Task> tasks = new ArrayList<>();
        // loop logic until exiting
        while(sc.hasNextLine()) {
            input = sc.nextLine();
            String firstWord = input.split(" ")[0];

            System.out.println(spacer);
            if(input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                for(int i = 0; i < tasks.size(); ++i) {
                    System.out.println(i+1 + "." + tasks.get(i));
                }
            } else if (firstWord.equals("mark")) {
                int taskNum = Integer.parseInt(input.split(" ")[1]);
                Task t = tasks.get(taskNum);
                t.markAsDone();
                System.out.println("Nice! I've marked this task as done: ");
                System.out.println(t);
            } else if (firstWord.equals("unmark")) {
                int taskNum = Integer.parseInt(input.split(" ")[1]);
                Task t = tasks.get(taskNum);
                t.markAsUndone();
                System.out.println("OK! I've marked this task as not done yet: ");
                System.out.println(t);
            }
            else {
                Task t = new Task(input);
                tasks.add(t);
                System.out.println("    added: " + input);
            }

            System.out.println(spacer);
        }
//        System.out.println(spacer);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(spacer);
    }
}
