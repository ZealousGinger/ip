package tomato.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.task.Task;
import tomato.ui.UserInterface;

/**
 * Represents a command that updates an event task's start date-time.
 */
public class UpdateEventFromCommand extends Command {
    public static final String COMMAND_WORD = "from";
    public static final String MESSAGE_USAGE = "update {task number} /from {DD/MM/YYYY HHMM}\n" +
            "e.g.(update 2 /from 3/3/2024 1900)";
    private final int taskNum;
    private final LocalDateTime dateTime;

    /**
     * Creates an event start-time update command.
     *
     * @param taskNum Index of the task to update.
     * @param dateTime New start date-time.
     */
    public UpdateEventFromCommand(int taskNum, LocalDateTime dateTime) {
        this.taskNum = taskNum;
        this.dateTime = dateTime;
    }

    @Override
    public void execute(TaskList tasks, UserInterface ui, Storage storage) throws TomatoException {
        String res = tasks.updateEventFrom(taskNum, dateTime);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}
