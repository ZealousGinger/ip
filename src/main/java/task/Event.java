package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Event Task object.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Instantiates Event object with description, starting and ending datetime.
     * @param description String to describe task.
     * @param from LocalDateTime of start of event.
     * @param to LocalDateTime of end of event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        assert from != null : "DateTime from should not be null";
        assert to != null : "DateTime to should not be null";
        this.from= from;
        this.to = to;
    }

    /**
     * Instantiates Event object with description, starting and ending datetime.
     * Used when loading tasks from storage.
     * @param description String to describe task.
     * @param isDone boolean status
     * @param from LocalDateTime of start of event.
     * @param to LocalDateTime of end of event.
     */
    public Event(String description, boolean isDone, LocalDateTime from, LocalDateTime to) {
        super(description, isDone);
        assert from != null : "DateTime from should not be null";
        assert to != null : "DateTime to should not be null";
        this.from= from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() +
                " (from: " + from.format(DateTimeFormatter.ofPattern("MMM d yyyy h:mma")) +
                " to: " + to.format(DateTimeFormatter.ofPattern("MMM d yyyy h:mma")) + ")";
    }

    /**
     * Returns string representation for storage format of task for saving.
     * @return storage string representation.
     */
    public String toSave() {
        return "E|" + super.toSave() + "|" + from + "|" + to;
    }
}
