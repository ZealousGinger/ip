package tomato;

/**
 * Represents Task object class, stores description and status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Instantiates Task object with description.
     * @param description String to describe task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Instantiates Task object with description and status, used when loading tasks from storage.
     * @param description String to describe task.
     * @param isDone boolean status
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns string representation of task's current status.
     * @return string status representation.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks task as not done.
     */
    public void markAsNotdone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }

    /**
     * Returns string representation for storage format of task for saving.
     * @return
     */
    public String toSave() {return (isDone ? 1 : 0) + "|" + this.description;}
}
