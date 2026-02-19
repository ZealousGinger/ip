package tomato;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import tomato.data.TaskList;
import tomato.storage.Storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class StorageTest {
    private static final String TASK_FILE_DIR = "src/test/data/";
    private static final String TASK_FILE_VALID = TASK_FILE_DIR + "TaskListValid.txt";
    private static final String TASK_FILE_VALID_SAVE = TASK_FILE_DIR + "TaskListValidSave.txt";
    private static final String TASK_FILE_INVALID_DATETIME = TASK_FILE_DIR + "TaskListInvalidDatetime.txt";
    private static final String TASK_FILE_INVALID_SEPARATOR = TASK_FILE_DIR + "TaskListInvalidSeparator.txt";
    private static final String TASK_FILE_MISSING = TASK_FILE_DIR + "doesNotExists.txt";
    private static final String TASK_FILE_DUMMY = TASK_FILE_DIR + "TaskListDummy.txt";

    @Test
    public void constructor_nullFilePath_exceptionThrown(){
        assertThrowsExactly(NullPointerException.class, () -> {
            Storage storage = new Storage(null);
        });
    }

    @Test
    public void constructor_blankFilePath_assertionThrown(){
        assertThrowsExactly(AssertionError.class, () -> {
            Storage storage = new Storage("");
        });

        assertThrowsExactly(AssertionError.class, () -> {
            Storage storage = new Storage("   ");
        });
    }

    @Test
    public void load_validFile_success(){
        Storage storage = new Storage(TASK_FILE_VALID);
        assertDoesNotThrow(() -> {
            storage.load();
        });
    }

    @Test
    public void load_invalidDatetimeFormat_exceptionThrown(){
        Storage storage = new Storage(TASK_FILE_INVALID_DATETIME);
        assertThrowsExactly(TomatoException.class, () -> {
            storage.load();
        });
    }

    @Test
    public void load_invalidSeparatorFormat_exceptionThrown(){
        Storage storage = new Storage(TASK_FILE_INVALID_SEPARATOR);
        assertThrowsExactly(TomatoException.class, () -> {
            storage.load();
        });
    }

    @Test
    public void load_missingFile_exceptionThrown(){
        Storage storage = new Storage(TASK_FILE_MISSING);
        assertThrowsExactly(TomatoException.class, () -> {
            storage.load();
        });

        try {
            Files.deleteIfExists(Path.of(TASK_FILE_MISSING));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void saveToDisk_validTaskList(){
        Storage storage = new Storage(TASK_FILE_VALID_SAVE);
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        try {
            storage.load();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        assertDoesNotThrow(() -> {
            storage.saveToDisk(tasks.getTaskList());
        });
    }

    @Test
    public void saveToDisk_validEmptyTaskList(){
        Storage storage = new Storage(TASK_FILE_VALID_SAVE);
        TaskList tasks = new TaskList();

        try {
            storage.load();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        assertDoesNotThrow(() -> {
            storage.saveToDisk(tasks.getTaskList());
        });
    }

    @Test
    public void saveToDisk_nullTaskList_exceptionThrown(){
        Storage storage = new Storage(TASK_FILE_DUMMY);

        try {
            storage.load();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        assertThrowsExactly(NullPointerException.class, () -> {
            storage.saveToDisk(null);
        });
    }

}
