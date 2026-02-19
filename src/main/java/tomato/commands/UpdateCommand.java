package tomato.commands;

import tomato.storage.Storage;
import tomato.data.TaskList;
import tomato.TomatoException;
import tomato.ui.Ui;

import java.time.LocalDateTime;

public class UpdateCommand extends Command {
    public static final String COMMAND_WORD = "update";
    public static final String MESSAGE_USAGE = "update {task number} /{argument to update} {new argument}\n" +
            "e.g.(update 2 /by 3/3/2024 1900)";
    private final int taskNum;
    public static enum Argument {
        DESCRIPTION,
        BY,
        FROM,
        TO,
        TIME
    }

    private Argument argToUpdate ;
    private String taskDescription;
    private LocalDateTime datetime;
    private LocalDateTime from;
    private LocalDateTime to;


    // for description
    public UpdateCommand(Argument argToUpdate, int taskNum, String taskDescription) {
        this.argToUpdate = argToUpdate;
        this.taskNum = taskNum;
        this.taskDescription = taskDescription;
    }

    // for by, from , to alone
    public UpdateCommand(Argument argToUpdate, int taskNum, LocalDateTime datetime) {
        this.argToUpdate = argToUpdate;
        this.taskNum = taskNum;
        this.datetime = datetime;
    }

    // for from and to
    public UpdateCommand(Argument argToUpdate, int taskNum, LocalDateTime from, LocalDateTime to) {
        this.argToUpdate = argToUpdate;
        this.taskNum = taskNum;
        this.from = from;
        this.to = to;
    }

    private Command handleArgumentToUpdate() {
        switch (argToUpdate) {
        case DESCRIPTION:
            return new UpdateDescriptionCommand(taskNum, taskDescription);
        case BY:
            return new UpdateDeadlineCommand(taskNum, datetime);
        case FROM:
            return new UpdateEventFromCommand(taskNum, datetime);
        case TO:
            return new UpdateEventToCommand(taskNum, datetime);
        case TIME:
            return new UpdateEventTimeCommand(taskNum, from, to);
        }
        assert false : "code should not reach here";
        return null;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        Command cmd = handleArgumentToUpdate();
        cmd.execute(tasks, ui, storage);
    }
}
