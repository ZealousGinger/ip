package tomato.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.task.Task;
import tomato.ui.Ui;

/**
 * Represents a command that updates an event task's end date-time.
 */
public class UpdateEventToCommand extends Command {
    public static final String COMMAND_WORD = "to";
    public static final String MESSAGE_USAGE = "update {task number} /to {DD/MM/YYYY HHMM}\nlis" +
            "e.g.(update 2 /to 3/3/2024 1900)";
    private final int taskNum;
    private final LocalDateTime dateTime;

    /**
     * Creates an event end-time update command.
     *
     * @param taskNum Index of the task to update.
     * @param dateTime New end date-time.
     */
    public UpdateEventToCommand(int taskNum, LocalDateTime dateTime) {
        this.taskNum = taskNum;
        this.dateTime = dateTime;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        String res = tasks.updateEventTo(taskNum, dateTime);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}
