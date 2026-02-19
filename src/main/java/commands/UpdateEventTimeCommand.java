package commands;

import task.Task;
import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UpdateEventTimeCommand extends Command {
    public static final String COMMAND_WORD = "update_event_time";
    public static final String MESSAGE_USAGE = "update_event_time {task number}|" +
            "{DD/MM/YYYY HHMM}|{DD/MM/YYYY HHMM}\n" +
            "e.g.(update_event_from 2|3/3/2024 1900|3/3/2024 2000)";
    private final int taskNum;
    private final LocalDateTime from;
    private final LocalDateTime to;

    public UpdateEventTimeCommand(int taskNum, LocalDateTime from, LocalDateTime to) {
        this.taskNum = taskNum;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        String res = tasks.updateEventTime(taskNum, from, to);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}

