package tomato.commands;

import java.time.LocalDateTime;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.ui.UserInterface;

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
    public static enum UpdateField {
        DESCRIPTION,
        BY,
        FROM,
        TO,
        TIME
    }

    private UpdateField updateField;
    private String taskDescription;
    private LocalDateTime datetime;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;


    /**
     * Creates an update command for task description.
     *
     * @param updateField Field to update.
     * @param taskNum Index of the task to update.
     * @param taskDescription New description value.
     */
    public UpdateCommand(UpdateField updateField, int taskNum, String taskDescription) {
        this.updateField = updateField;
        this.taskNum = taskNum;
        this.taskDescription = taskDescription;
    }

    /**
     * Creates an update command for a single date-time field.
     *
     * @param updateField Field to update.
     * @param taskNum Index of the task to update.
     * @param datetime New date-time value.
     */
    public UpdateCommand(UpdateField updateField, int taskNum, LocalDateTime datetime) {
        this.updateField = updateField;
        this.taskNum = taskNum;
        this.datetime = datetime;
    }

    /**
     * Creates an update command for an event time range.
     *
     * @param updateField Field to update.
     * @param taskNum Index of the task to update.
     * @param startDateTime New start date-time.
     * @param endDateTime New end date-time.
     */
    public UpdateCommand(UpdateField updateField, int taskNum, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.updateField = updateField;
        this.taskNum = taskNum;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public void execute(TaskList tasks, UserInterface ui, Storage storage) throws TomatoException {
        switch (updateField) {
        case DESCRIPTION:
            new UpdateDescriptionCommand(taskNum, taskDescription).execute(tasks, ui, storage);
            return;
        case BY:
            new UpdateDeadlineCommand(taskNum, datetime).execute(tasks, ui, storage);
            return;
        case FROM:
            new UpdateEventFromCommand(taskNum, datetime).execute(tasks, ui, storage);
            return;
        case TO:
            new UpdateEventToCommand(taskNum, datetime).execute(tasks, ui, storage);
            return;
        case TIME:
            new UpdateEventTimeCommand(taskNum, startDateTime, endDateTime).execute(tasks, ui, storage);
            return;
        default:
            assert false : "code should not reach here";
        }
    }
}
