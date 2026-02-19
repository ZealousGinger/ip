package tomato.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.task.Task;
import tomato.ui.UserInterface;

/**
 * Represents a command that updates an event task's full time range.
 */
public class UpdateEventTimeCommand extends Command {
    public static final String COMMAND_WORD = "time";
    public static final String MESSAGE_USAGE = "update_event_time {task number} /time " +
            "{DD/MM/YYYY HHMM} /to {DD/MM/YYYY HHMM}\n" +
            "e.g.(update 2 /time 3/3/2024 1900 /to 3/3/2024 2000)";
    private final int taskNum;
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event time-range update command.
     *
     * @param taskNum Index of the task to update.
     * @param from New start date-time.
     * @param to New end date-time.
     */
    public UpdateEventTimeCommand(int taskNum, LocalDateTime from, LocalDateTime to) {
        this.taskNum = taskNum;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, UserInterface ui, Storage storage) throws TomatoException {
        String res = tasks.updateEventTime(taskNum, from, to);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}
