package tomato.commands;

import java.util.ArrayList;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.task.Task;
import tomato.ui.Ui;

/**
 * Represents a command that marks a task as done.
 */
public class MarkCommand extends Command {
    public static final String COMMAND_WORD = "mark";
    public static final String MESSAGE_USAGE = "mark {task number} e.g.(mark 1, mark 3)";
    private final int taskNum;

    /**
     * Creates a mark command.
     *
     * @param taskNum Index of the task to mark.
     */
    public MarkCommand(int taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        String res = tasks.markTask(taskNum);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}
