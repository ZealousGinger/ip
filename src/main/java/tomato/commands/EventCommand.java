package tomato.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.task.Task;
import tomato.ui.UserInterface;

/**
 * Represents a command that creates an event task.
 */
public class EventCommand extends Command {
    public static final String COMMAND_WORD = "event";
    public static final String MESSAGE_USAGE = "event {task_description} /from {DD/MM/YYYY HHMM} " +
            "/to {DD-MM-YYYY HHMM}\n" +
            "e.g.(event team meeting /from 2/2/2025 1945 /to 2/2/2025 2045)";
    private final String taskName;
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event creation command.
     *
     * @param taskName Description of the event task.
     * @param from Start date-time of the event task.
     * @param to End date-time of the event task.
     */
    public EventCommand(String taskName, LocalDateTime from, LocalDateTime to) {
        this.taskName = taskName;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, UserInterface ui, Storage storage) throws TomatoException {
        String res = tasks.createEvent(taskName, from, to);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(tasks.getTaskList());
        ui.showTomatoDialog(res);
    }
}
