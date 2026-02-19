package tomato;

import commands.ByeCommand;
import commands.Command;
import ui.Ui;

import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents the Tomato chatbot application.
 */
public class Tomato {
    private Ui ui;
    private Parser parser;
    private Storage storage;
    private TaskList tasks;

    /**
     * Returns a loaded task list, or a new empty list when loading fails.
     *
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
     * Creates a Tomato chatbot with the specified storage file path.
     *
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
     * Starts the Tomato chatbot in text UI mode.
     *
     * @param args arguments.
     */
    public static void main(String[] args) {
        Tomato tomato = new Tomato("data/TaskList.txt");
        tomato.run();
    }

    /**
     * Runs the text UI input loop for the chatbot.
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
     * Handles a single user message in the GUI.
     *
     * @param input User input text.
     */
    public void handleResponse(String input) {
        ui.showUserDialog(input);
        parseAndExecuteResponse(input);
    }

    /**
     * Parses and executes a single user input message.
     *
     * @param input User input text.
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
