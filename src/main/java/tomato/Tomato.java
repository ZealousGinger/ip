package tomato;

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
        ui = new Ui();
        storage = new Storage(filePath);

        try {
            tasks = new TaskList(storage.load());
        } catch (FileNotFoundException | TaskListException | TomatoException e) {
            ui.showLoadingError(e);
            tasks = new TaskList();
        }

        parser = new Parser(tasks, storage);
        ui.printStartMessage();
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
                isExit = parser.parseAndExecute(input);
            } catch (TomatoException | TaskListException e) {
                ui.showLoadingError(e);
            }
            System.out.println(SPACER);
        }
    }
}