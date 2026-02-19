package tomato.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Creates a deadline task with the specified description and due date-time.
     *
     * @param description String to describe task.
     * @param by LocalDateTime of deadline.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        assert by != null : "DateTime by should not be null";
        this.by = by;
    }

    /**
     * Creates a deadline task with the specified description, status, and due date-time.
     *
     * @param description String to describe task.
     * @param isDone boolean status.
     * @param by LocalDateTime of deadline.
     */
    public Deadline(String description, boolean isDone, LocalDateTime by) {
        super(description, isDone);
        assert by != null : "DateTime by should not be null";
        this.by = by;
    }

    /**
     * Sets the due date-time of this deadline task.
     *
     * @param by New due date-time.
     */
    public void setDateTimeBy(LocalDateTime by) {
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " +
                by.format(DateTimeFormatter.ofPattern("MMM d yyyy h:mma"))+ ")";
    }

    /**
     * Returns the storage format representation of this deadline task.
     *
     * @return storage string representation.
     */
    public String toSave() {
        return "D|" + super.toSave() + "|" + by;
    }
}
