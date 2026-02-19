package tomato.commands;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.ui.Ui;

/**
 * Represents a command that lists all tasks.
 */
public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";

    /**
     * Creates a list command.
     */
    public ListCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        ui.showTomatoDialog(tasks.toString());
    }
}
