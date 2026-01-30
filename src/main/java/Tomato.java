import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tomato {
    private static final String SPACER = "   ____________________________________________________________";
    private boolean isExit = false;
    private TaskList tasks;
    private Ui ui;
    private Storage storage;
    private Parser parser;

    public Tomato(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            this.tasks = new TaskList(this.storage.load());
        } catch (FileNotFoundException | TaskListException | TomatoException e) {
            this.ui.showLoadingError(e);
            this.tasks = new TaskList();
        }
        this.parser = new Parser(this.tasks, this.storage);
        this.ui.printStartMessage();
    }

    public static void main(String[] args) {
        Tomato tomato = new Tomato("data/TaskList.txt");
        tomato.run();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        String input;

        while (!isExit) {
            input = sc.nextLine();
            try {
                this.isExit = this.parser.parseAndExecute(input);
            } catch (TomatoException | TaskListException e) {
                this.ui.showLoadingError(e);
            }
            System.out.println(SPACER);
        }
    }
}