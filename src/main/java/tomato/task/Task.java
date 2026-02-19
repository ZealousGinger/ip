package tomato.task;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the specified description.
     *
     * @param description String to describe task.
     */
    public Task(String description) {
        assert description != null : "Description should not be null";
        assert !description.isBlank() : "Description should not be blank";

        this.description = description;
        this.isDone = false;
    }

    /**
     * Creates a task with the specified description and completion status.
     *
     * @param description String to describe task.
     * @param isDone boolean status.
     */
    public Task(String description, boolean isDone) {
        assert description != null : "Description should not be null";
        assert !description.isBlank() : "Description should not be blank";

        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns the current status icon of this task.
     *
     * @return string status representation.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Sets this task as done.
     */
    public void setDone() {
        isDone = true;
    }

    /**
     * Sets this task as not done.
     */
    public void setNotDone() {
        isDone = false;
    }

    /**
     * Sets the task description.
     *
     * @param s New task description.
     */
    public void setDescription(String s) {
        assert description != null : "Description should not be null";
        assert !description.isBlank() : "Description should not be blank";
        description = s;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns the storage format representation of this task.
     *
     * @return storage string representation.
     */
    public String toSave() {
        return (isDone ? 1 : 0) + "|" + description;
    }
}
