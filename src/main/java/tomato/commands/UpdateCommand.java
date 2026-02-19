package tomato.commands;

import java.time.LocalDateTime;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.ui.Ui;

/**
 * Represents a command that updates one or more fields of an existing task.
 */
public class UpdateCommand extends Command {
    public static final String COMMAND_WORD = "update";
    public static final String MESSAGE_USAGE = "update {task number} /{argument to update} {new argument}\n" +
            "e.g.(update 2 /by 3/3/2024 1900)";
    private final int taskNum;

    /**
     * Represents updatable task fields.
     */
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


    /**
     * Creates an update command for task description.
     *
     * @param argToUpdate Field to update.
     * @param taskNum Index of the task to update.
     * @param taskDescription New description value.
     */
    public UpdateCommand(Argument argToUpdate, int taskNum, String taskDescription) {
        this.argToUpdate = argToUpdate;
        this.taskNum = taskNum;
        this.taskDescription = taskDescription;
    }

    /**
     * Creates an update command for a single date-time field.
     *
     * @param argToUpdate Field to update.
     * @param taskNum Index of the task to update.
     * @param datetime New date-time value.
     */
    public UpdateCommand(Argument argToUpdate, int taskNum, LocalDateTime datetime) {
        this.argToUpdate = argToUpdate;
        this.taskNum = taskNum;
        this.datetime = datetime;
    }

    /**
     * Creates an update command for an event time range.
     *
     * @param argToUpdate Field to update.
     * @param taskNum Index of the task to update.
     * @param from New start date-time.
     * @param to New end date-time.
     */
    public UpdateCommand(Argument argToUpdate, int taskNum, LocalDateTime from, LocalDateTime to) {
        this.argToUpdate = argToUpdate;
        this.taskNum = taskNum;
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a concrete update command based on the target update argument.
     *
     * @return Concrete update command.
     */
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
