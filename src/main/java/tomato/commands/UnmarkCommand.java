package tomato.commands;

import java.util.ArrayList;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.task.Task;
import tomato.ui.UserInterface;

/**
 * Represents a command that marks a task as not done.
 */
public class UnmarkCommand extends Command {
    public static final String COMMAND_WORD = "unmark";
    public static final String MESSAGE_USAGE = "unmark {task number} e.g.(unmark 1, unmark 3)";
    private final int taskNum;

    /**
     * Creates an unmark command.
     *
     * @param taskNum Index of the task to unmark.
     */
    public UnmarkCommand(int taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public void execute(TaskList tasks, UserInterface ui, Storage storage) throws TomatoException {
        String res = tasks.unmarkTask(taskNum);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}
