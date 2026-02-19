package tomato;

import java.io.FileNotFoundException;
import java.util.Scanner;

import tomato.commands.Command;
import tomato.data.TaskList;
import tomato.parser.Parser;
import tomato.storage.Storage;
import tomato.ui.UserInterface;

/**
 * Represents the Tomato chatbot application.
 */
public class Tomato {
    private UserInterface ui;
    private Parser parser;
    private Storage storage;
    private TaskList tasks;

    /**
     * Creates a Tomato chatbot with the specified storage file path.
     *
     * @param filePath file path location (e.g. "data/TaskList.txt" ) to save tasks into.
     */
    public Tomato(String filePath) {
        ui = new UserInterface();
        storage = new Storage(filePath);
        tasks = loadTaskList(storage);
        parser = new Parser();
    }

    /**
     * Returns a loaded task list, or a new empty list when loading fails.
     *
     * @param storageAdapter Storage object to load the file from.
     * @return TaskList object.
     */
    public TaskList loadTaskList(Storage storageAdapter) {
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storageAdapter.load());
        } catch (FileNotFoundException | TomatoException exception) {
            loadedTasks = new TaskList();
        }
        return loadedTasks;
    }

    /**
     * Sets the GUI adapter used by Tomato.
     *
     * @param gui UI adapter instance.
     */
    public void setGui(UserInterface gui) {
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
        Scanner consoleScanner = new Scanner(System.in);
        String input;

        while (true) {
            input = consoleScanner.nextLine();
            try {
                Command cmd = parser.parse(input);

                if (cmd.isExit()) {
                    break;
                }

                cmd.execute(tasks, ui, storage);
            } catch (TomatoException exception) {
                ui.showErrorDialog(exception);
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
        } catch (Exception exception) {
            ui.showErrorDialog(exception);
        }
    }

}
