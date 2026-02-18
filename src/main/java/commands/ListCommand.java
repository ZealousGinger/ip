package commands;

import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";

    public ListCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        ui.showTomatoDialog(tasks.toString());
    }
}