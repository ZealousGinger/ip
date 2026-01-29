public class Ui {
    private static final String SPACER = "   ____________________________________________________________";
    private static final String TAB = "    ";

    public Ui() {

    }

    public void printStartMessage() {
        System.out.println(SPACER);
        System.out.println(TAB + "Hello! I'm Tomato \uD83C\uDF45 !\n" + TAB + "What can I do for you?");
        System.out.println(SPACER);
    }

    public void printExitMessage() {
        System.out.println(TAB + "Bye. Hope to see you again soon!");
        System.out.println(SPACER);
    }

    public void showLoadingError(Exception e) {
        System.out.println("Error loading: " + e);
    }
}
