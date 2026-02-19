package commands;

import task.Task;
import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

import java.util.ArrayList;

public class MarkCommand extends Command {
    public static final String COMMAND_WORD = "mark";
    public static final String MESSAGE_USAGE = "mark {task number} e.g.(mark 1, mark 3)";
    private final int taskNum;

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