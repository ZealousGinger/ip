package commands;

import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

public class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";
    private final int taskNum;

    public DeleteCommand(int taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        String res = tasks.deleteTask(taskNum);
        storage.saveToDisk(tasks.getTaskList());
        ui.showTomatoDialog(res);
    }
}