import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.File;

import java.util.ArrayList;

public class TaskList {
    private static final String TAB = "    ";
    private ArrayList<Task> tasks;
    private File taskFile;
    private boolean isLoadingTask;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void printTasks() {
        System.out.println(TAB + "Here are the tasks in your list:");
        for(int i = 0; i < this.tasks.size(); ++i) {
            System.out.println(TAB + (i+1) + "." + this.tasks.get(i));
        }
    }

    public void markTask(String[] splitInput) throws TaskListException {
        int taskNum;
        try {
            taskNum = Integer.parseInt(splitInput[1]) - 1;
        } catch (Exception e) {
            throw new TaskListException("You must provide a task number!");
        }
        if (taskNum >= this.tasks.size()) {
            throw new TaskListException("That task number doesn't exist!");
        }
        Task t = this.tasks.get(taskNum);
        if(splitInput[0].equals("mark")) {
            t.markAsDone();
            System.out.println(TAB + "Nice! I've marked this task as done:");
        } else if(splitInput[0].equals("unmark")) {
            t.markAsNotdone();
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

        if (taskNum >= this.tasks.size()) {
            throw new TaskListException("That task number doesn't exist!");
        }
        Task t = this.tasks.get(taskNum);
        String taskName = t.toString();
        if(this.tasks.remove(t)) {
            System.out.println(TAB + "Noted. I've removed this task:");
            System.out.println(TAB + taskName);
            System.out.println(numOfTasks());
        } else {
            throw new TaskListException("This task cannot be removed, it doesn't exist!");
        }
    }

    private void AddTask(Task t) {
        this.tasks.add(t);
        System.out.println(TAB + "Got it. I've added this task:\n" + TAB + t.toString());
        System.out.println(numOfTasks());
        if(!this.isLoadingTask) {
        }
    }

    public void createTodo(String args) {
        String[] splitArgs = args.split("\\|");
        Task t;
        if(splitArgs.length > 1) {
            t = new Todo(splitArgs[2], (Integer.parseInt(splitArgs[1])==1));
        } else {
            t = new Todo(args);
        }
        AddTask(t);
    }

    public void createDeadline(String args) throws TaskListException {
        String[] splitArgs = args.split("/by|\\|");
        if(splitArgs.length < 2) throw new TaskListException("deadline requires more arguments! Please provide them.");
        Task t;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        LocalDateTime dateTime;
        if(splitArgs.length > 2) {
            try {
                dateTime = LocalDateTime.parse(splitArgs[3].trim(), formatter);
            } catch (DateTimeException e) {
                try {
                    dateTime = LocalDateTime.parse(splitArgs[3].trim());
                } catch (Exception e2) {
                    throw new TaskListException("Unable to parse date!");
                }
            }
            t = new Deadline(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), dateTime);
        } else {
            try {
                dateTime = LocalDateTime.parse(splitArgs[1].trim(), formatter);
            } catch (DateTimeException e) {
                try {
                    dateTime = LocalDateTime.parse(splitArgs[1].trim());
                } catch (Exception e2) {
                    throw new TaskListException("Unable to parse date!");
                }
            }
            t = new Deadline(splitArgs[0], dateTime);
        }
        AddTask(t);
    }

    public void createEvent(String args) throws TaskListException {
        String[] splitArgs = args.split("/from|\\/to|\\|");
        if(splitArgs.length < 3) throw new TaskListException("event requires more arguments! Please provide them.");
        Task t;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        LocalDateTime from;
        LocalDateTime to;
        if(splitArgs.length > 3) {
            try {
                from = LocalDateTime.parse(splitArgs[3].trim(), formatter);
                to = LocalDateTime.parse(splitArgs[4].trim(), formatter);
            } catch (DateTimeException e) {
                try {
                    from = LocalDateTime.parse(splitArgs[3].trim());
                    to = LocalDateTime.parse(splitArgs[4].trim());
                } catch (Exception e2) {
                    throw new TaskListException("Unable to parse date! Please enter a date and time in this format: 2/12/2019 1800");
                }
            }
            t = new Event(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), from, to);
        } else {
            try {
                from = LocalDateTime.parse(splitArgs[1].trim(), formatter);
                to = LocalDateTime.parse(splitArgs[2].trim(), formatter);
            } catch (DateTimeException e) {
                try {
                    from = LocalDateTime.parse(splitArgs[1].trim());
                    to = LocalDateTime.parse(splitArgs[2].trim());
                } catch (Exception e2) {
                    throw new TaskListException("Unable to parse date! Please enter a date and time in this format: 2/12/2019 1800");
                }
            }
            t = new Event(splitArgs[0], from, to);
        }
        AddTask(t);
    }

    public String numOfTasks() {
        return TAB + "Now you have " + tasks.size() + " tasks in the list.";
    }

    public ArrayList<Task> getTaskList() {
        return this.tasks;
    }
}
