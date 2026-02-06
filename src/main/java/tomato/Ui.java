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
     * Gets the start message.
     */
    public static String getStartMessage() {
        return SPACER + "\n" + TAB + "Hello! I'm Tomato \uD83C\uDF45 !\n" + TAB + "What can I do for you?\n" + SPACER;
    }

    /**
     * Gets the exit message.
     */
    public static String getExitMessage() {
        return TAB + "Bye. Hope to see you again soon!\n" + SPACER;
    }

    /**
     * Gets loading error message.
     * @param e Exception of the error.
     */
    public static String getLoadingError(Exception e) {
        return "Error loading: " + e;
    }
}
