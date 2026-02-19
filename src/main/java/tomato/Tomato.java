package tomato;

import commands.ByeCommand;
import commands.Command;
import ui.Ui;

import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents an instance of a task tracking chatbot called Tomato.
 */
public class Tomato {
    private Ui ui;
    private Parser parser;
    private Storage storage;
    private TaskList tasks;

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
        storage = new Storage(filePath);
        tasks = loadTaskList(storage);
        parser = new Parser();
    }

    public void setGui(Ui gui) {
        ui = gui;
        ui.showStartDialog();
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
        Command cmd = null;

        while(true) {
            input = sc.nextLine();
            try {
                cmd = parser.parse(input);
                if (cmd.isExit()) {
                    break;
                }
                cmd.execute(tasks, ui, storage);
            } catch (TomatoException e) {
                ui.showErrorDialog(e);
            }
        }
    }


    /**
     * Handles a response for the user's chat message.
     */
    public void handleResponse(String input) {
        ui.showUserDialog(input);
        parseAndExecuteResponse(input);
    }

    /**
     * Parses and executes the user's response.
     */
    public void parseAndExecuteResponse(String input) {
        Command cmd;
            try {
                cmd = parser.parse(input);
                cmd.execute(tasks, ui, storage);
            } catch (Exception e) {
                ui.showErrorDialog(e);
            }
    }

}