package tomato;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from= from;
        this.to = to;
    }

    public Event(String description, boolean done, LocalDateTime from, LocalDateTime to) {
        super(description, done);
        this.from= from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() +
                " (from: " + from.format(DateTimeFormatter.ofPattern("MMM d yyyy h:mma")) +
                " to: " + to.format(DateTimeFormatter.ofPattern("MMM d yyyy h:mma")) + ")";
    }

    public String toSave() {
        return "E|" + super.toSave() + "|" + from + "|" + to;
    }
}
