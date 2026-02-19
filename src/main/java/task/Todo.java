package task;

/**
 * Represents a todo task.
 */
public class Todo extends Task {

    /**
     * Creates a todo task with the specified description.
     *
     * @param description String to describe task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates a todo task with the specified description and completion status.
     *
     * @param description String to describe task.
     * @param isDone boolean status.
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns the storage format representation of this todo task.
     *
     * @return storage string representation.
     */
    public String toSave() {
        return "T|" + super.toSave();
    }
}
