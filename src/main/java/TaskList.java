import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.util.ArrayList;

public class TaskList {
    private static final String TAB = "    ";
    private ArrayList<Task> tasks = new ArrayList<>();
    private File taskFile;
    private boolean isLoadingTask;

    public TaskList() {
        try {
            this.taskFile = loadTaskFile();
            loadTasks();
        } catch (FileNotFoundException | TaskListException e) {
            if(e instanceof TaskListException) {
                System.out.println("Unable to load tasks due to corrupted/improper format.");
            }
            System.out.println("creating a new file: " + e);
            try {
                this.taskFile = createTaskFile();
            } catch (IOException e2) {
                System.out.println("Error creating file: " + e2);
            }
        }
    }

    private File loadTaskFile() throws FileNotFoundException {
        String root = System.getProperty("user.dir");
        java.nio.file.Path path = java.nio.file.Paths.get(root, "data", "TaskList.txt");
        boolean fileExists = java.nio.file.Files.exists(path);
        if(fileExists) {
            return path.toFile();
        } else {
            throw new FileNotFoundException();
        }
    }

    private File createTaskFile() throws IOException {
        String root = System.getProperty("user.dir");
        java.nio.file.Path dataDir = java.nio.file.Paths.get(root, "data");
        if (!java.nio.file.Files.exists(dataDir)) {
            java.nio.file.Files.createDirectories(dataDir);
        }
        java.nio.file.Path taskListPath = java.nio.file.Paths.get(root, "data", "TaskList.txt");
        if (!java.nio.file.Files.exists(taskListPath)) {
            java.nio.file.Files.createFile(taskListPath);
            System.out.println("Created file: " + taskListPath.toAbsolutePath() + " true");
        }

        return taskListPath.toFile();
    }

    private void loadTasks() throws FileNotFoundException, TaskListException {
        this.isLoadingTask = true;
        System.out.println(TAB + "Loading tasks from storage......................");
        Scanner fileScanner = new Scanner(this.taskFile);
        while (fileScanner.hasNextLine()) {
            String data = fileScanner.nextLine();
            String[] args = data.split("|", 2);
            if(args[0].equals("T")) {
                createTodo(args[1]);
            } else if (args[0].equals("D")) {
                createDeadline(args[1]);
            } else if (args[0].equals("E")) {
                createEvent(args[1]);
            }
        }
        this.isLoadingTask = false;
    }

    public void saveToDisk() {
        try {
            FileWriter taskWriter = new FileWriter(this.taskFile);
            for (Task task : this.tasks) {
                taskWriter.write(task.toSave() + "\n");
            }
            taskWriter.close();
            taskWriter.close();
        } catch (IOException e) {
            System.out.println("unable to save to file");
            throw new RuntimeException(e);
        }
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
        saveToDisk();
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
            saveToDisk();
        } else {
            throw new TaskListException("This task cannot be removed, it doesn't exist!");
        }
    }

    private void AddTask(Task t) {
        this.tasks.add(t);
        System.out.println(TAB + "Got it. I've added this task:\n" + TAB + t.toString());
        System.out.println(numOfTasks());
        if(!this.isLoadingTask) {
            saveToDisk();
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
}
