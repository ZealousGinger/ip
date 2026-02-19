package tomato;

import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of Task(s) instances along with relevant methods to maintain the list.
 * Methods include creating, un/marking, deleting, counting, listing, tasks.
 */
public class TaskList {
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
        str.append("Here are the tasks in your list:\n");
        for(int i = 0; i < tasks.size(); ++i) {
            str.append((i+1) + ". " + tasks.get(i) + "\n");
        }
        return str.toString();
    }

    private Task getTask(int idx) throws TomatoException {
        if (idx >= tasks.size() || idx < 0) {
            throw new TomatoException("That task number " + (idx + 1) + " doesn't exist!\n" +
                    "Pick a task number from " + 1 + " to " + tasks.size()
                    , Integer.toString(idx + 1));
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
        return "Nice! I've marked this task as done:" + t;
    }

    /**
     * Modifies the state of a specified Task instance unmarked.
     * @param idx integer task index
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public String unmarkTask(int idx) throws TomatoException {
        Task t = getTask(idx);
        t.setNotDone();
        return "OK! I've marked this task as not done yet:" + t;
    }

    /**
     * Updates the description of a task.
     * @param idx integer task index.
     * @param description new description to update.
     * @return String result.
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public String updateDescription(int idx, String description) throws TomatoException {
        if (description.isBlank()) {
            throw new TomatoException("Description cannot be blank!");
        }
        Task t = getTask(idx);
        t.setDescription(description);
        return "OK! I've updated your description !" + t;
    }

    /**
     * Updates the due datetime of a deadline.
     * @param idx integer task index.
     * @param by new datetime to update.
     * @return String result.
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public String updateDeadlineTime(int idx, LocalDateTime by) throws TomatoException {
        Task t = getTask(idx);
        if (!(t instanceof Deadline)) {
            throw new TomatoException("This Task is not a Deadline task! You cannot update it's by!");
        }
        Deadline d = (Deadline) t;
        d.setDateTimeBy(by);
        return "OK! I've updated your deadline due datetime !" + d;
    }

    /**
     * Updates from datetime of an event.
     * @param idx integer task index.
     * @param from new datetime to update.
     * @return String result.
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public String updateEventFrom(int idx, LocalDateTime from) throws TomatoException {
        Task t = getTask(idx);
        if (!(t instanceof Event)) {
            throw new TomatoException("This Task is not a Event task! You cannot update it's from!");
        }
        Event e = (Event) t;
        e.setDateTimeFrom(from);
        return "OK! I've updated your event from datetime !" + e;
    }

    /**
     * Updates to datetime of an event.
     * @param idx integer task index.
     * @param to new datetime to update.
     * @return String result.
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public String updateEventTo(int idx, LocalDateTime to) throws TomatoException {
        Task t = getTask(idx);
        if (!(t instanceof Event)) {
            throw new TomatoException("This Task is not a Event task! You cannot update it's to!");
        }
        Event e = (Event) t;
        e.setDateTimeTo(to);
        return "OK! I've updated your event to datetime !" + e;
    }

    /**
     * Updates from and to datetimes of an event.
     * @param idx integer task index.
     * @param from new from datetime to update.
     * @param to new to datetime to update.
     * @return String result.
     * @throws TomatoException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public String updateEventTime(int idx, LocalDateTime from, LocalDateTime to) throws TomatoException {
        Task t = getTask(idx);
        if (!(t instanceof Event)) {
            throw new TomatoException("This Task is not a Event task! You cannot update it's time!");
        }
        Event e = (Event) t;
        e.setDateTimeFrom(from);
        e.setDateTimeTo(to);
        return "OK! I've updated your event datetime !" + e;
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
            str.append("Noted. I've removed this task:\n");
            str.append(taskName + "\n");
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
        str.append("Got it. I've added this task:\n" + t.toString() + "\n");
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
        return "Now you have " + tasks.size() + " tasks in the list.";
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
        str.append("Here are the matching tasks in your list:" + "\n");
        for(int i = 0; i < matchingTasks.size(); ++i) {
            str.append((i+1) + "." + matchingTasks.get(i) + "\n");
        }
        return str.toString();
    }

    /** Returns tasks matching with given keyword */
    private ArrayList<Task> getMatchingTasks(String keyword) {
        List<Task> matchingTasks = tasks.stream()
                .filter(t -> t.toString().contains(keyword))
                .toList();
        return new ArrayList<Task>(matchingTasks);
    }
}
