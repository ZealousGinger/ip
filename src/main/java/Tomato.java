import java.util.Scanner;
public class Tomato {
    public static void main(String[] args) {
        String spacer = "   ____________________________________________________________";
        System.out.println(spacer);
        System.out.println("    Hello! I'm Tomato \uD83C\uDF45 ! \n    What can I do for you?");
        System.out.println(spacer);

        Scanner sc = new Scanner(System.in);
        String input;

        // loop logic until exiting
        while(sc.hasNextLine()) {
            input = sc.nextLine();
            if(input.equals("bye")) break;

            System.out.println(spacer);
            System.out.println("    " + input);
            System.out.println(spacer);
        }
        System.out.println(spacer);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(spacer);
    }
}
