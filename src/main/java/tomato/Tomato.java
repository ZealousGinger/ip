package tomato;

import ui.Ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents an instance of a task tracking chatbot called Tomato.
 */
public class Tomato {
    private static final String SPACER = "   ____________________________________________________________";
    private Ui ui;
    private Parser parser;

    /**
     * Loads the tasklist from storage or create a new one.
     * @param s Storage object to load the file from.
     * @return TaskList object.
     */
    public TaskList loadTaskList(Storage s) {
        TaskList t;
        try {
            t = new TaskList(s.load());
        } catch (FileNotFoundException | TomatoException e) {
            t = new TaskList();
        }
        return t;
    }

    /**
     * Instantiates an instance of Tomato chatbot with the specified storage file location.
     * @param filePath file path location (e.g. "data/TaskList.txt" ) to save tasks into.
     */
    public Tomato(String filePath) {
        ui = new Ui();
        Storage storage = new Storage(filePath);
        TaskList tasks = loadTaskList(storage);
        parser = new Parser(tasks, storage);
    }

    /**
     * Represents an entry point to instantiate an instance of the Tomato chatbot class.
     * @param args arguments.
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