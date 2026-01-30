package tomato;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private File taskFile;
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void loadTaskFile() throws FileNotFoundException {
        String root = System.getProperty("user.dir");
        java.nio.file.Path path = java.nio.file.Paths.get(root, this.filePath);
        boolean fileExists = java.nio.file.Files.exists(path);
        if(fileExists) {
            this.taskFile = path.toFile();
        } else {
            throw new FileNotFoundException();
        }
    }

    public void createTaskFile() throws IOException {
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

    public ArrayList<Task> decodeTasks() throws FileNotFoundException, TaskListException {
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

    public Task decodeTodo(String args) {
        String[] splitArgs = args.split("\\|");
        Task t;
        t = new Todo(splitArgs[2], (Integer.parseInt(splitArgs[1])==1));
        return t;
    }

    public Task decodeDeadline(String args) throws TaskListException {
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

    public Task decodeEvent(String args) throws TaskListException {
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
