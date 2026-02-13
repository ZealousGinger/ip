package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline Task object.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Instantiates Deadline object with description, and deadline.
     * @param description String to describe task.
     * @param by LocalDateTime of deadline.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        assert by != null : "DateTime by should not be null";
        this.by = by;
    }

    /**
     * Instantiates Deadline object with description, status, and deadline, used when loading tasks from storage.
     * @param description String to describe task.
     * @param isDone boolean status
     * @param by LocalDateTime of deadline.
     */
    public Deadline(String description, boolean isDone, LocalDateTime by) {
        super(description, isDone);
        assert by != null : "DateTime by should not be null";
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " +
                by.format(DateTimeFormatter.ofPattern("MMM d yyyy h:mma"))+ ")";
    }

    /**
     * Returns string representation for storage format of task for saving.
     * @return storage string representation.
     */
    public String toSave() {
        return "D|" + super.toSave() + "|" + by;
    }
}
