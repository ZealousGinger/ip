package tomato.commands;

import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.ui.Ui;

public class ByeCommand extends Command {
    public static final String COMMAND_WORD = "bye";

    public ByeCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.exit();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
