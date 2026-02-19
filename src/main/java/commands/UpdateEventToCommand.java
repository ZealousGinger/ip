package commands;

import task.Task;
import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UpdateEventToCommand extends Command {
    public static final String COMMAND_WORD = "update_event_to";
    public static final String MESSAGE_USAGE = "update_event_to {task number}|{DD/MM/YYYY HHMM}\nlis" +
            "e.g.(update_event_to 2|3/3/2024 1900)";
    private final int taskNum;
    private final LocalDateTime dateTime;

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

