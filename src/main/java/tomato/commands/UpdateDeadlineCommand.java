package tomato.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.task.Task;
import tomato.ui.Ui;

/**
 * Represents a command that updates a deadline task's due date-time.
 */
public class UpdateDeadlineCommand extends Command {
    public static final String COMMAND_WORD = "deadline";
    public static final String MESSAGE_USAGE = "update {task number} /by {DD/MM/YYYY HHMM}\n" +
            "e.g.(update 2 /by 3/3/2024 1900)";
    private final int taskNum;
    private final LocalDateTime dateTime;

    /**
     * Creates a deadline update command.
     *
     * @param taskNum Index of the task to update.
     * @param dateTime New due date-time.
     */
    public UpdateDeadlineCommand(int taskNum, LocalDateTime dateTime) {
        this.taskNum = taskNum;
        this.dateTime = dateTime;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        String res = tasks.updateDeadlineTime(taskNum, dateTime);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}
