package tomato;

import java.time.LocalDateTime;

import java.util.ArrayList;

/**
 * Represents a list of Task(s) instances along with relevant methods to maintain the list.
 * Methods include creating, un/marking, deleting, counting, listing, tasks.
 */
public class TaskList {
    private static final String TAB = "    ";
    private final ArrayList<Task> tasks;

    /**
     * Instantiates the class with an empty list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Instantiates the class with the specified array list of tasks.
     * @param tasks ArrayList of Task instances from storage task file.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * returns all the tasks in the current list in a human-readable format along with tasks information.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(TAB + "Here are the tasks in your list:\n");
        for(int i = 0; i < tasks.size(); ++i) {
            str.append(TAB + (i+1) + "." + tasks.get(i) + "\n");
        }
        return str.toString();
    }

    private Task getTask(int idx) throws TomatoException {
        if (idx > tasks.size() || idx < 0) {
            throw new TomatoException("That task number doesn't exist!");
        }
        return tasks.get(idx);
    }

    /**
     * Modifies the state of a specified Task instance marked.
     * @param idx integer task index
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public String markTask(int idx) throws TomatoException {
        Task t = getTask(idx);
        t.setDone();
        return TAB + "Nice! I've marked this task as done:" + TAB + t;
    }

    /**
     * Modifies the state of a specified Task instance unmarked.
     * @param idx integer task index
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public String unmarkTask(int idx) throws TomatoException {
        Task t = getTask(idx);
        t.setNotDone();
        return TAB + "OK! I've marked this task as not done yet:" + TAB + t;
    }

    /**
     * Deletes an instance of a specified Task instance of the list of tasks.
     * @param idx integer task index
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public String deleteTask(int idx) throws TomatoException {
        Task t = getTask(idx);
        String taskName = t.toString();
        StringBuilder str = new StringBuilder();
        if(tasks.remove(t)) {
            str.append(TAB + "Noted. I've removed this task:\n");
            str.append(TAB + taskName + "\n");
            str.append(numOfTasks() + "\n");
        }
        return str.toString();
    }

    /**
     * Adds an instance of a task object into the list of tasks.
     * @param t Task instance to add.
     */
    private String addTask(Task t) {
        StringBuilder str = new StringBuilder();
        tasks.add(t);
        str.append(TAB + "Got it. I've added this task:\n" + TAB + t.toString() + "\n");
        str.append(numOfTasks() + "\n");
        return str.toString();
    }

    /**
     * Creates an instance of Todo object and adds it into the list of tasks.
     * @param args String description of the Todo task.
     */
    public String createTodo(String args) {
        Task t = new Todo(args);
        return addTask(t);
    }

    /**
     * Creates an instance of Deadline object and adds it into the list of tasks.
     * @param description string description of task.
     * @param by datetime of deadline.
     */
    public String createDeadline(String description, LocalDateTime by) {
        Task t = new Deadline(description, by);
        return addTask(t);
    }

    /**
     * Creates an instance of Event object and adds it into the list of tasks.
     * @param description string description of task.
     * @param from datetime of start of event.
     * @param to datetime of end of event.
     */
    public String createEvent(String description, LocalDateTime from, LocalDateTime to) {
        Task t = new Event(description, from, to);
        return addTask(t);
    }

    /**
     * Returns the number of tasks in the list.
     * @return number of tasks.
     */
    public String numOfTasks() {
        return TAB + "Now you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Returns the ArrayList of Tasks.
     * @return array list of tasks.
     */
    public ArrayList<Task> getTaskList() {
        return tasks;
    }

    public String printMatchingTasks(String keyword) {
        StringBuilder str = new StringBuilder();
        ArrayList<Task> matchingTasks = getMatchingTasks(keyword);
        str.append(TAB + "Here are the matching tasks in your list:" + "\n");
        for(int i = 0; i < matchingTasks.size(); ++i) {
            str.append(TAB + (i+1) + "." + matchingTasks.get(i) + "\n");
        }
        return str.toString();
    }

    private ArrayList<Task> getMatchingTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.toString().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
