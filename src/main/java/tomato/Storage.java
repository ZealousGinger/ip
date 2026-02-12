package tomato;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the storage class.
 */
public class Storage {
    private File taskFile;
    private final String filePath;
    private static final String REGEX_DEFAULT = "\\|";
    private static final String REGEX_BY = "/by|";
    private static final String REGEX_FROM_TO = "/from|\\\\/to|";
    private static final String REGEX_EMPTY = "";
    private static final String DEADLINE = "DEADLINE";
    private static final String EVENT = "EVENT";

    /**
     * Instantiates the storage class with a specified file path for storage file.
     * @param filePath string of file path to store the task file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Checks the previously specified file path, and loads it if exists.
     */
    private boolean loadTaskFile() {
        String root = System.getProperty("user.dir");
        Path path = Paths.get(root, filePath);
        boolean fileExists = Files.exists(path);

        if(!fileExists) {
            return false;
        }
        taskFile = path.toFile();
        return true;
    }

    /**
     * Creates the relevant directories and file according the previously specified file path.
     * @throws IOException If an input output error has occurred while trying to create the file.
     */
    private void createTaskFile() throws IOException {
        String root = System.getProperty("user.dir");
        Path taskListPath = Paths.get(root, filePath);
        Path dataDir = taskListPath.getParent();

        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }

        if (!Files.exists(taskListPath)) {
            Files.createFile(taskListPath);
            System.out.println("Created file: " + taskListPath.toAbsolutePath() + " true");
        }
        taskFile = taskListPath.toFile();
    }

    private Task decodeTask(String[] args) throws TomatoException {
        if (args[0].equals("T")) {
            return decodeTodo(args[1]);
        } else if (args[0].equals("D")) {
            return decodeDeadline(args[1]);
        } else if (args[0].equals("E")) {
            return decodeEvent(args[1]);
        } else {
            assert false : "code should not reach here";
        }

        assert false : "code should not reach here";
        return null;
    }

    private ArrayList<Task> scanTasks(Scanner sc) throws TomatoException {
        ArrayList<Task> tasks = new ArrayList<>();
        while (sc.hasNextLine()) {
            String data = sc.nextLine();
            String[] args = data.split("|", 2);
            tasks.add(decodeTask(args));
        }
        return tasks;
    }

    /**
     * Returns an arraylist of tasks decoded from the stored task file.
     * @return Arraylist of tasks.
     * @throws FileNotFoundException If file does not exist.
     * @throws TomatoException If unable to create task file or load tasks from file.
     * or If an error occurred from parsing the task file or if unable to create task object.
     */
    private ArrayList<Task> loadTasks() throws FileNotFoundException, TomatoException {
        Scanner fileScanner = new Scanner(taskFile);
        return scanTasks(fileScanner);
    }

    /**
     * Returns an arraylist of tasks loaded from the stored task file if it exists.
     * If task file does not exist or cannot be read, creates a new task file and throws an exception.
     * @return arraylist of tasks.
     * @throws FileNotFoundException If file does not exist.
     * @throws TomatoException If unable to create task file or load tasks from file.
     * or If an error occurred from parsing the task file or if unable to create task object.
     */
    public ArrayList<Task> load() throws FileNotFoundException, TomatoException {
        boolean isLoaded = loadTaskFile();
        if (!isLoaded) {
            try {
                createTaskFile();
                throw new TomatoException("Unable to load from task file!");
            } catch (IOException e) {
                throw new TomatoException("Error, Unable to create new task file!");
            }
        }

        return loadTasks();
    }

    /**
     * Saves given array list of tasks into the task file.
     * @param tasks Array list of Task objects.
     */
    public void saveToDisk(ArrayList<Task> tasks) {
        try {
            FileWriter taskWriter = new FileWriter(taskFile);
            for (Task task : tasks) {
                taskWriter.write(task.toSave() + "\n");
            }
            taskWriter.close();
        } catch (IOException e) {
            System.out.println("unable to save to file");
            throw new RuntimeException(e);
        }
    }

    private String[] parseArgs(String arg, String keyword) {
        return arg.split(keyword + REGEX_DEFAULT);
    }

    private void checkArgLength(String[] args, int len, String commandName) throws TomatoException {
        if (args.length < len) {
            throw new TomatoException(commandName + " arguments is required! Please provide it.");
        }
    }

    private LocalDateTime parseDate(String arg) throws TomatoException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        String trimmedArg = arg.trim();

        try {
            return LocalDateTime.parse(trimmedArg, formatter);
        } catch (DateTimeException e) {
            // move on and try the second parser
        }

        try {
            return LocalDateTime.parse(trimmedArg);
        } catch (DateTimeException e) {
            throw new TomatoException("Unable to parse date!");
        }
    }

    /**
     * Returns a Todo instance from the stored todo string.
     * @param args String arguments e.g. "T|1|read book".
     * @return Todo Task object.
     */
    private Task decodeTodo(String args) {
        String[] splitArgs = parseArgs(args, REGEX_EMPTY);
        return new Todo(splitArgs[2], (Integer.parseInt(splitArgs[1])==1));
    }

    /**
     * Returns a Deadline instance from the stored deadline string.
     * @param args String arguments e.g. "D|1|return books |2025-02-02T19:00".
     * @return Deadline Task object.
     */
    private Task decodeDeadline(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_BY);
        checkArgLength(splitArgs, 2, DEADLINE);
        LocalDateTime dateTime = parseDate(splitArgs[3]);
        return new Deadline(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), dateTime);
    }

    /**
     * Returns a Event instance from the stored event string.
     * @param args String arguments e.g. "E|0|book shopping |2025-03-03T16:00|2025-03-03T18:00".
     * @return Event Task object.
     */
    private Task decodeEvent(String args) throws TomatoException {
        String[] splitArgs = parseArgs(args, REGEX_FROM_TO);
        checkArgLength(splitArgs, 3, EVENT);
        LocalDateTime from = parseDate(splitArgs[3]);
        LocalDateTime to = parseDate(splitArgs[4]);
        return new Event(splitArgs[2], (Integer.parseInt(splitArgs[1])==1), from, to);
    }
}
