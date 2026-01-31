package tomato;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
     * Modifies the state of a specified Task instance an either marked or unmarked.
     * @param splitInput Array of string arguments, 1st is command type, 2nd is task index number, e.g.["mark", "1"].
     * @throws TaskListException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public void markTask(String[] splitInput) throws TaskListException {
        int taskNum;
        try {
            taskNum = Integer.parseInt(splitInput[1]) - 1;
        } catch (Exception e) {
            throw new TaskListException("You must provide a task number!");
        }

        if (taskNum >= tasks.size()) {
            throw new TaskListException("That task number doesn't exist!");
        }

        Task t = tasks.get(taskNum);
        if(splitInput[0].equals("mark")) {
            t.setDone();
            System.out.println(TAB + "Nice! I've marked this task as done:");
        } else if(splitInput[0].equals("unmark")) {
            t.setNotDone();
            System.out.println(TAB + "OK! I've marked this task as not done yet:");
        }
        System.out.println(TAB + t);
    }

    /**
     * Deletes an instance of a specified Task instance of the list of tasks.
     * @param args string representing the task index number.
     * @throws TaskListException If a given task index is invalid/out of bound, i.e. <0 or > number of tasks in list.
     */
    public void deleteTask(String args) throws TaskListException {
        int taskNum;
        try {
            taskNum = Integer.parseInt(args) - 1;
        } catch (Exception e) {
            throw new TaskListException("You must provide a task number!");
        }

        if (taskNum >= tasks.size()) {
            throw new TaskListException("That task number doesn't exist!");
        }
        Task t = tasks.get(taskNum);
        String taskName = t.toString();
        if(tasks.remove(t)) {
            System.out.println(TAB + "Noted. I've removed this task:");
            System.out.println(TAB + taskName);
            System.out.println(numOfTasks());
        } else {
            throw new TaskListException("This task cannot be removed, it doesn't exist!");
        }
    }

    /**
     * Adds an instance of a task object into the list of tasks.
     * @param t
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
     * @param args String arguments required i.e. "return books /by 2/2/2025 1900".
     * @throws TaskListException If insufficient arguments are given or if invalid arguments unable to be parsed.
     */
    public void createDeadline(String args) throws TaskListException {
        String[] splitArgs = args.split("/by|\\|");
        if(splitArgs.length < 2) {
            throw new TaskListException("deadline requires more arguments! Please provide them.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        LocalDateTime dateTime;

        try {
            dateTime = LocalDateTime.parse(splitArgs[1].trim(), formatter);
        } catch (DateTimeException e) {
            try {
                dateTime = LocalDateTime.parse(splitArgs[1].trim());
            } catch (Exception e2) {
                throw new TaskListException("Unable to parse date!");
            }
        }
        Task t = new Deadline(splitArgs[0], dateTime);
        AddTask(t);
    }

    /**
     * Creates an instance of Event object and adds it into the list of tasks.
     * @param args String arguments required i.e. "meeting /from 2/2/2025 1900 /to 2/2/2025 2000".
     * @throws TaskListException If insufficient arguments are given or if invalid arguments unable to be parsed.
     */
    public void createEvent(String args) throws TaskListException {
        String[] splitArgs = args.split("/from|\\/to|\\|");
        if(splitArgs.length < 3) {
            throw new TaskListException("event requires more arguments! Please provide them.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        LocalDateTime from;
        LocalDateTime to;

        try {
            from = LocalDateTime.parse(splitArgs[1].trim(), formatter);
            to = LocalDateTime.parse(splitArgs[2].trim(), formatter);
        } catch (DateTimeException e) {

            try {
                from = LocalDateTime.parse(splitArgs[1].trim());
                to = LocalDateTime.parse(splitArgs[2].trim());
            } catch (Exception e2) {
                throw new TaskListException("Unable to parse date! " +
                        "Please enter a date and time in this format: 2/12/2019 1800");
            }
        }

        Task t = new Event(splitArgs[0], from, to);
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
