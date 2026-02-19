package commands;

import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

// Class below is AI generated simple abstract template of the Command class
public abstract class Command {
    public static String COMMAND_WORD;
    public static String MESSAGE_USAGE;
    /**
     * Executes the command with the given task list, UI, and storage.
     *
     * @param tasks The current list of tasks.
     * @param ui The UI handler for displaying messages.
     * @param storage The storage handler for saving or loading tasks.
     * @throws TomatoException If an error occurs during execution.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException;

    /**
     * Returns whether this command signals the app to exit.
     * Override in ExitCommand to return true.
     *
     * @return False by default; true only for exit commands.
     */
    public boolean isExit() {
        return false;
    }
}
