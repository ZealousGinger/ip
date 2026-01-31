package tomato;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents an instance of a task tracking chatbot called Tomato.
 */
public class Tomato {
    private static final String SPACER = "   ____________________________________________________________";
    private boolean isExit = false;
    private TaskList tasks;
    private Ui ui;
    private Storage storage;
    private Parser parser;

    /**
     * Instantiates an instance of Tomato chatbot with the specified storage file location.
     * @param filePath file path location (e.g. "data/TaskList.txt" ) to save tasks into.
     */
    public Tomato(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        try {
            tasks = new TaskList(storage.load());
        } catch (FileNotFoundException | TomatoException e) {
            ui.showLoadingError(e);
            tasks = new TaskList();
        }

        parser = new Parser(tasks, storage);
        ui.printStartMessage();
    }

    /**
     * Represents an entry point to instantiate an instance of the Tomato chatbot class.
     * @param args
     */
    public static void main(String[] args) {
        Tomato tomato = new Tomato("data/TaskList.txt");
        tomato.run();
    }

    /**
     * Instantiates and executes the looping chatbot interface.
     */
    public void run() {
        Scanner sc = new Scanner(System.in);
        String input;

        while (!isExit) {
            input = sc.nextLine();
            try {
                isExit = parser.parseAndExecute(input);
            } catch (TomatoException e) {
                ui.showLoadingError(e);
            }
            System.out.println(SPACER);
        }
    }
}