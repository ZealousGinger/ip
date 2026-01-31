package tomato;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class TaskList {
    private static final String TAB = "    ";
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void printTasks() {
        System.out.println(TAB + "Here are the tasks in your list:");
        for(int i = 0; i < tasks.size(); ++i) {
            System.out.println(TAB + (i+1) + "." + tasks.get(i));
        }
    }

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

    private void AddTask(Task t) {
        tasks.add(t);
        System.out.println(TAB + "Got it. I've added this task:\n" + TAB + t.toString());
        System.out.println(numOfTasks());
    }

    public void createTodo(String args) {
        Task t = new Todo(args);
        AddTask(t);
    }

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

    public String numOfTasks() {
        return TAB + "Now you have " + tasks.size() + " tasks in the list.";
    }

    public ArrayList<Task> getTaskList() {
        return tasks;
    }
}
