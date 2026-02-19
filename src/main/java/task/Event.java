package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates an event task with the specified description and time range.
     *
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
     * Creates an event task with the specified description, status, and time range.
     *
     * @param description String to describe task.
     * @param isDone boolean status.
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

    /**
     * Sets the start date-time of this event task.
     *
     * @param from New start date-time.
     */
    public void setDateTimeFrom(LocalDateTime from) {
        this.from = from;
    }

    /**
     * Sets the end date-time of this event task.
     *
     * @param to New end date-time.
     */
    public void setDateTimeTo(LocalDateTime to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() +
                " (from: " + from.format(DateTimeFormatter.ofPattern("MMM d yyyy h:mma")) +
                " to: " + to.format(DateTimeFormatter.ofPattern("MMM d yyyy h:mma")) + ")";
    }

    /**
     * Returns the storage format representation of this event task.
     *
     * @return storage string representation.
     */
    public String toSave() {
        return "E|" + super.toSave() + "|" + from + "|" + to;
    }
}
