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

