package tomato.commands;

import tomato.task.Task;
import tomato.storage.Storage;
import tomato.data.TaskList;
import tomato.TomatoException;
import tomato.ui.Ui;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DeadlineCommand extends Command {
    public static final String COMMAND_WORD = "deadline";
    public static final String MESSAGE_USAGE = "deadline {task_description} /by {DD/MM/YYYY HHMM}\n" +
            "e.g.(deadline return books /by 2/2/2025 1945)";
    private final String taskName;
    private final LocalDateTime dateTime;

    public DeadlineCommand(String taskName, LocalDateTime dateTime) {
        this.taskName = taskName;
        this.dateTime = dateTime;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        String res = tasks.createDeadline(taskName, dateTime);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(tasks.getTaskList());
        ui.showTomatoDialog(res);
    }
}