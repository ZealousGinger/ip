package tomato;

import task.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the storage class.
 */
public class Storage {
    private final Parser parser = new Parser();
    private File taskFile;
    private final String filePath;

    /**
     * Instantiates the storage class with a specified file path for storage file.
     * @param filePath string of file path to store the task file.
     */
    public Storage(String filePath) {
        assert !filePath.isBlank() : "file path string must not be blank";
        this.filePath = filePath;
    }

    /**
     * Checks the previously specified file path, and loads it if exists.
     */
    private boolean loadTaskFile() {
        String root = System.getProperty("user.dir");
        Path path = Paths.get(root, filePath);
        boolean isExists = Files.exists(path);

        if(!isExists) {
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

    /** Scans task file and returns the arraylist of tasks */
    private ArrayList<Task> scanFile(Scanner sc) throws TomatoException {
        ArrayList<Task> tasks = new ArrayList<>();
        while (sc.hasNextLine()) {
            String data = sc.nextLine();
            String[] args = data.split("|", 2);
            tasks.add(parser.decodeTask(args));
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
        return scanFile(fileScanner);
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
                throw new TomatoException("Missing task file!");
            } catch (IOException e) {
                throw new TomatoException("Error, Unable to create new task file!");
            }
        }
        assert taskFile != null : "task file should not be null here";
        return loadTasks();
    }

    /** Writes array of tasks to file */
    private void writeToFile(ArrayList<Task> tasks) throws IOException {
        FileWriter taskWriter = new FileWriter(taskFile);
        for (Task task : tasks) {
            taskWriter.write(task.toSave() + "\n");
        }
        taskWriter.close();
    }


    /**
     * Saves given array list of tasks into the task file.
     * @param tasks Array list of Task objects.
     */
    public void saveToDisk(ArrayList<Task> tasks) throws TomatoException {
        assert taskFile != null : "task file should not be null";
        try {
            writeToFile(tasks);
        } catch (IOException e) {
            throw new TomatoException("IO error, unable to write to file.");
        }
    }
}
