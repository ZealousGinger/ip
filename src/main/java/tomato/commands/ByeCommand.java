package tomato.commands;

import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.ui.UserInterface;

/**
 * Represents a command that exits Tomato.
 */
public class ByeCommand extends Command {
    public static final String COMMAND_WORD = "bye";

    /**
     * Creates an exit command.
     */
    public ByeCommand() {
    }

    @Override
    public void execute(TaskList tasks, UserInterface ui, Storage storage) {
        ui.exit();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
