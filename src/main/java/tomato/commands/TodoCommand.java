package tomato.commands;

import tomato.task.Task;
import tomato.storage.Storage;
import tomato.data.TaskList;
import tomato.TomatoException;
import tomato.ui.Ui;

import java.util.ArrayList;

public class TodoCommand extends Command {
    public static final String COMMAND_WORD = "todo";
    public static final String MESSAGE_USAGE = "todo {task_description} e.g.(todo read books, todo shopping)";
    private final String taskName;

    public TodoCommand(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        String res = tasks.createTodo(taskName);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(tasks.getTaskList());
        ui.showTomatoDialog(res);
    }
}