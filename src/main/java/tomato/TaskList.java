package tomato;

import java.time.LocalDateTime;

import java.util.ArrayList;

/**
 * Represents a list of Task(s) instances along with relevant methods to maintain the list.
 * Methods include creating, un/marking, deleting, counting, listing, tasks.
 */
public class TaskList {
    private static final String TAB = "    ";
    private ArrayList<Task> tasks;

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
     * Prints all the tasks in the current list in a human-readable format along with tasks information.
     */
    public void printTasks() {
        System.out.println(TAB + "Here are the tasks in your list:");
        for(int i = 0; i < tasks.size(); ++i) {
            System.out.println(TAB + (i+1) + "." + tasks.get(i));
        }
    }

    /**
     * Modifies the state of a specified Task instance marked.
     * @param idx integer task index
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public void markTask(int idx) throws TomatoException {
        if (idx > tasks.size() || idx < 0) {
            throw new TomatoException("That task number doesn't exist!");
        }
        Task t = tasks.get(idx);
        t.setDone();
        System.out.println(TAB + "Nice! I've marked this task as done:");
        System.out.println(TAB + t);
    }

    /**
     * Modifies the state of a specified Task instance unmarked.
     * @param idx integer task index
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public void unmarkTask(int idx) throws TomatoException {
        if (idx > tasks.size() || idx < 0) {
            throw new TomatoException("That task number doesn't exist!");
        }
        Task t = tasks.get(idx);
        t.setNotDone();
        System.out.println(TAB + "OK! I've marked this task as not done yet:");
        System.out.println(TAB + t);
    }

    /**
     * Deletes an instance of a specified Task instance of the list of tasks.
     * @param idx integer task index
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public void deleteTask(int idx) throws TomatoException {
        if (idx > tasks.size() || idx < 0) {
            throw new TomatoException("That task number doesn't exist!");
        }
        Task t = tasks.get(idx);
        String taskName = t.toString();
        if(tasks.remove(t)) {
            System.out.println(TAB + "Noted. I've removed this task:");
            System.out.println(TAB + taskName);
            System.out.println(numOfTasks());
        }
    }

    /**
     * Adds an instance of a task object into the list of tasks.
     * @param t Task instance to add.
     */
    private void AddTask(Task t) {
        tasks.add(t);
        System.out.println(TAB + "Got it. I've added this task:\n" + TAB + t.toString());
        System.out.println(numOfTasks());
    }

    /**
     * Creates an instance of Todo object and adds it into the list of tasks.
     * @param args String description of the Todo task.
     */
    public void createTodo(String args) {
        Task t = new Todo(args);
        AddTask(t);
    }

    /**
     * Creates an instance of Deadline object and adds it into the list of tasks.
     * @param description string description of task.
     * @param by datetime of deadline.
     */
    public void createDeadline(String description, LocalDateTime by) {
        Task t = new Deadline(description, by);
        AddTask(t);
    }

    /**
     * Creates an instance of Event object and adds it into the list of tasks.
     * @param description string description of task.
     * @param from datetime of start of event.
     * @param to datetime of end of event.
     */
    public void createEvent(String description, LocalDateTime from, LocalDateTime to) {
        Task t = new Event(description, from, to);
        AddTask(t);
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

    public void printMatchingTasks(String keyword) {
        ArrayList<Task> matchingTasks = getMatchingTasks(keyword);
        System.out.println(TAB + "Here are the matching tasks in your list:");
        for(int i = 0; i < matchingTasks.size(); ++i) {
            System.out.println(TAB + (i+1) + "." + matchingTasks.get(i));
        }
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
