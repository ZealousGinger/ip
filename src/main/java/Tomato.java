import java.util.Scanner;
public class Tomato {
    public static void main(String[] args) {
        String spacer = "   ____________________________________________________________";
        System.out.println(spacer);
        System.out.println("    Hello! I'm Tomato \uD83C\uDF45 ! \n    What can I do for you?");
        System.out.println(spacer);

        Scanner sc = new Scanner(System.in);
        String[] input = new String[100];
        int inputIdx = 0;
        // loop logic until exiting
        while(sc.hasNextLine()) {
            input[inputIdx++] = sc.nextLine();
            if(input[inputIdx - 1].equals("bye")) break;

            System.out.println(spacer);

            if(input[inputIdx - 1].equals("list")) {
                for(int i = 0; i < inputIdx -1; ++i) {
                    System.out.println(i+1 + ". " + input[i]);
                }
            } else {
                System.out.println("    added: " + input[inputIdx - 1]);
            }

            System.out.println(spacer);
        }
        System.out.println(spacer);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(spacer);
    }
}
