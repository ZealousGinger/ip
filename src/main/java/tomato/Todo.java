package tomato;

/**
 * Represents a Todo Task object.
 */
public class Todo extends Task {

    /**
     * Instantiates Todo object with description.
     * @param description String to describe task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Instantiates Todo object with description and status, used when loading tasks from storage.
     * @param description String to describe task.
     * @param isDone boolean status
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns string representation for storage format of task for saving.
     * @return storage string representation.
     */
    public String toSave() {
        return "T|" + super.toSave();
    }
}
