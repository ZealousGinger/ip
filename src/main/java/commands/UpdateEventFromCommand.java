package commands;

import task.Task;
import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UpdateEventFromCommand extends Command {
    public static final String COMMAND_WORD = "from";
    public static final String MESSAGE_USAGE = "update {task number} /from {DD/MM/YYYY HHMM}\n" +
            "e.g.(update 2 /from 3/3/2024 1900)";
    private final int taskNum;
    private final LocalDateTime dateTime;

    public UpdateEventFromCommand(int taskNum, LocalDateTime dateTime) {
        this.taskNum = taskNum;
        this.dateTime = dateTime;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        String res = tasks.updateEventFrom(taskNum, dateTime);
        ArrayList<Task> updatedTaskList = tasks.getTaskList();
        assert !updatedTaskList.isEmpty() : "Updated Task List should be not be empty";
        storage.saveToDisk(updatedTaskList);
        ui.showTomatoDialog(res);
    }
}
