package commands;

import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

// Class below is AI generated simple abstract template of the Command class
public abstract class Command {

    /**
     * Executes the command with the given task list, UI, and storage.
     *
     * @param tasks   the current list of tasks
     * @param ui      the UI handler for displaying messages
     * @param storage the storage handler for saving/loading tasks
     * @throws TomatoException if an error occurs during execution
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException;

    /**
     * Returns whether this command signals the app to exit.
     * Override in ExitCommand to return true.
     *
     * @return false by default; true only for ExitCommand
     */
    public boolean isExit() {
        return false;
    }
}