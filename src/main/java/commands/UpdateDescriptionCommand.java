package commands;

import task.Task;
import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

import java.util.ArrayList;

public class UpdateDescriptionCommand extends Command {
    public static final String COMMAND_WORD = "update_description";
    private final int taskNum;
    private final String description;

    public UpdateDescriptionCommand(int taskNum, String description) {
        this.taskNum = taskNum;
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        String res = tasks.updateDescription(taskNum, description);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}
