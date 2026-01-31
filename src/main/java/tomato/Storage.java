package tomato;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the storage class.
 */
public class Storage {
    private File taskFile;
    private final String filePath;

    /**
     * Instantiates the storage class with a specified file path for storage file.
     * @param filePath string of file path to store the task file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Checks the previously specified file path, and loads it if exists.
     * @throws FileNotFoundException If previously specified file path does not exist.
     */
    private void loadTaskFile() throws FileNotFoundException {
        String root = System.getProperty("user.dir");
        java.nio.file.Path path = java.nio.file.Paths.get(root, this.filePath);
        boolean fileExists = java.nio.file.Files.exists(path);
        if(fileExists) {
            this.taskFile = path.toFile();
        } else {
            throw new FileNotFoundException();
        }
    }

    /**
     * Creates the relevant directories and file according the previously specified file path.
     * @throws IOException If an input output error has occurred while trying to create the file.
     */
    private void createTaskFile() throws IOException {
        String root = System.getProperty("user.dir");
        java.nio.file.Path taskListPath = java.nio.file.Paths.get(root, this.filePath);
        java.nio.file.Path dataDir = taskListPath.getParent();

        if (!java.nio.file.Files.exists(dataDir)) {
            java.nio.file.Files.createDirectories(dataDir);
        }

        if (!java.nio.file.Files.exists(taskListPath)) {
            java.nio.file.Files.createFile(taskListPath);
            System.out.println("Created file: " + taskListPath.toAbsolutePath() + " true");
        }
        this.taskFile = taskListPath.toFile();
    }

    /**
     * Returns an arraylist of tasks decoded from the stored task file.
     * @return
     * @throws FileNotFoundException If file does not exist.
     * @throws TaskListException If an error occurred from parsing the task file or if unable to create task object.
     * @throws TomatoException If unable to create task file or load tasks from file.
     */
    private ArrayList<Task> decodeTasks() throws FileNotFoundException, TaskListException {
        ArrayList<Task> tasks = new ArrayList<>();
        System.out.println(Ui.TAB + "Loading tasks from storage......................");
        Scanner fileScanner = new Scanner(this.taskFile);
        while (fileScanner.hasNextLine()) {
            String data = fileScanner.nextLine();
            String[] args = data.split("|", 2);
            if (args[0].equals("T")) {
                tasks.add(this.decodeTodo(args[1]));
            } else if (args[0].equals("D")) {
                tasks.add(this.decodeDeadline(args[1]));
            } else if (args[0].equals("E")) {
                tasks.add(this.decodeEvent(args[1]));
            }
        }
        return tasks;
    }

    /**
     * Returns an arraylist of tasks loaded from the stored task file if it exists.
     * If task file does not exist or cannot be read, creates a new task file and throws an exception.
     * @return arraylist of tasks.
     * @throws FileNotFoundException If file does not exist.
     * @throws TaskListException If an error occurred from parsing the task file or if unable to create task object.
     * @throws TomatoException If unable to create task file or load tasks from file.
     */
    public ArrayList<Task> load() throws FileNotFoundException, TaskListException, TomatoException {
        try {
            this.loadTaskFile();
            return this.decodeTasks();
        } catch (Exception e) {
            try {
                this.createTaskFile();
            } catch (IOException e2) {
                throw new TomatoException("Error, Unable to create new task file!");
            }
        }
        throw new TomatoException("Error unable to load from file!");
    }

    /**
     * Saves given array list of tasks into the task file.
     * @param tasks
     */
    public void saveToDisk(ArrayList<Task> tasks) {
        try {
            FileWriter taskWriter = new FileWriter(this.taskFile);
            for (Task task : tasks) {
                taskWriter.write(task.toSave() + "\n");
            }
            taskWriter.close();
            taskWriter.close();
        } catch (IOException e) {
            System.out.println("unable to save to file");
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a Todo instance from the stored todo string.
     * @param args String arguments e.g. "T|1|read book".
     * @return Todo Task object.
     */
    private Task decodeTodo(String args) {
        String[] splitArgs = args.split("\\|");
        Task t;
        t = new Todo(splitArgs[2], (Integer.parseInt(splitArgs[1])==1));
        return t;
    }

    /**
     * Returns a Deadline instance from the stored deadline string.
     * @param args String arguments e.g. "D|1|return books |2025-02-02T19:00".
     * @return Deadline Task object.
     */
    private Task decodeDeadline(String args) throws TaskListException {
        String[] splitArgs = args.split("/by|\\|");
        if(splitArgs.length < 2) throw new TaskListException("deadline requires more arguments! Please provide them.");
        Task t;
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(splitArgs[3].trim());
        } catch (Exception e2) {
            throw new TaskListException("Unable to parse date!");
        }

        t = new Deadline(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), dateTime);

        return t;
    }

    /**
     * Returns a Event instance from the stored event string.
     * @param args String arguments e.g. "E|0|book shopping |2025-03-03T16:00|2025-03-03T18:00".
     * @return Event Task object.
     */
    private Task decodeEvent(String args) throws TaskListException {
        String[] splitArgs = args.split("/from|\\/to|\\|");
        if(splitArgs.length < 3) throw new TaskListException("event requires more arguments! Please provide them.");
        Task t;
        LocalDateTime from;
        LocalDateTime to;
        try {
            from = LocalDateTime.parse(splitArgs[3].trim());
            to = LocalDateTime.parse(splitArgs[4].trim());
        } catch (Exception e2) {
            throw new TaskListException("Unable to parse date! Please enter a date and time in this format: 2/12/2019 1800");
        }

        t = new Event(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), from, to);
        return t;
    }
}
