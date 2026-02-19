package tomato.commands;

import java.util.ArrayList;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.task.Task;
import tomato.ui.UserInterface;

/**
 * Represents a command that updates a task description.
 */
public class UpdateDescriptionCommand extends Command {
    public static final String COMMAND_WORD = "description";
    public static final String MESSAGE_USAGE = "update {task number} /description {new description}\n" +
            "e.g.(update 1 /description buy book)";
    private final int taskNum;
    private final String description;

    /**
     * Creates a description update command.
     *
     * @param taskNum Index of the task to update.
     * @param description New description value.
     */
    public UpdateDescriptionCommand(int taskNum, String description) {
        this.taskNum = taskNum;
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, UserInterface ui, Storage storage) throws TomatoException {
        String res = tasks.updateDescription(taskNum, description);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}
