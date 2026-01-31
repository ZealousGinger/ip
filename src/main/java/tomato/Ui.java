package tomato;

/**
 * Represents the User Interface class.
 */
public class Ui {
    public static final String SPACER = "   ____________________________________________________________";
    public static final String TAB = "    ";

    /**
     * Instantiates the User interface class.
     */
    public Ui() {
    }

    /**
     * Prints the start message.
     */
    public static void printStartMessage() {
        System.out.println(SPACER);
        System.out.println(TAB + "Hello! I'm Tomato \uD83C\uDF45 !\n" + TAB + "What can I do for you?");
        System.out.println(SPACER);
    }

    /**
     * Prints the exit message.
     */
    public static void printExitMessage() {
        System.out.println(TAB + "Bye. Hope to see you again soon!");
        System.out.println(SPACER);
    }

    /**
     * Prints loading error message.
     * @param e Exception of the error.
     */
    public void showLoadingError(Exception e) {
        System.out.println("Error loading: " + e);
    }
}
