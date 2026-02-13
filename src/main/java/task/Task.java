package task;

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
        return (isDone ? "X" : " ");
    }

    /**
     * Sets task as done.
     */
    public void setDone() {
        isDone = true;
    }

    /**
     * Sets task as done.
     */
    public void setNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns string representation for storage format of task for saving.
     * @return storage string representation.
     */
    public String toSave() {
        return (isDone ? 1 : 0) + "|" + description;
    }
}
