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
            ui.getLoadingError(e);
            tasks = new TaskList();
        }

        parser = new Parser(tasks, storage);
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
        String result = "";

        while (result != null) {
            input = sc.nextLine();
            try {
                result = parser.parseAndExecute(input);
                System.out.println(result);
            } catch (TomatoException e) {
                ui.getLoadingError(e);
            }
            System.out.println(SPACER);
        }
    }


    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        String result;
        try {
            result = parser.parseAndExecute(input);
        } catch (TomatoException e) {
            return Ui.getLoadingError(e);
        }
        return result;
    }

}