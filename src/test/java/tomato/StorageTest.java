package tomato;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class StorageTest {
    private static final String FILE_DIR = "src/test/data/";
    private static final String VALID_FILE = FILE_DIR + "TaskListValid.txt";
    private static final String VALID_FILE_SAVE = FILE_DIR + "TaskListValidSave.txt";
    private static final String INVALID_DATETIME = FILE_DIR + "TaskListInvalidDatetime.txt";
    private static final String INVALID_SEPARATOR = FILE_DIR + "TaskListInvalidSeparator.txt";
    private static final String MISSING_FILE = FILE_DIR + "doesNotExists.txt";
    private static final String DUMMY_FILE = FILE_DIR + "TaskListDummy.txt";

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
        Storage storage = new Storage(VALID_FILE);
        assertDoesNotThrow(() -> {
            storage.load();
        });
    }

    @Test
    public void load_invalidDatetimeFormat_exceptionThrown(){
        Storage storage = new Storage(INVALID_DATETIME);
        assertThrowsExactly(TomatoException.class, () -> {
            storage.load();
        });
    }

    @Test
    public void load_invalidSeparatorFormat_exceptionThrown(){
        Storage storage = new Storage(INVALID_SEPARATOR);
        assertThrowsExactly(TomatoException.class, () -> {
            storage.load();
        });
    }

    @Test
    public void load_missingFile_exceptionThrown(){
        Storage storage = new Storage(MISSING_FILE);
        assertThrowsExactly(TomatoException.class, () -> {
            storage.load();
        });

        try {
            Files.deleteIfExists(Path.of(MISSING_FILE));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void saveToDisk_validTaskList(){
        Storage storage = new Storage(VALID_FILE_SAVE);
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        try {
            storage.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertDoesNotThrow(() -> {
            storage.saveToDisk(tasks.getTaskList());
        });
    }

    @Test
    public void saveToDisk_validEmptyTaskList(){
        Storage storage = new Storage(VALID_FILE_SAVE);
        TaskList tasks = new TaskList();

        try {
            storage.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertDoesNotThrow(() -> {
            storage.saveToDisk(tasks.getTaskList());
        });
    }

    @Test
    public void saveToDisk_nullTaskList_exceptionThrown(){
        Storage storage = new Storage(DUMMY_FILE);

        try {
            storage.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertThrowsExactly(NullPointerException.class, () -> {
            storage.saveToDisk(null);
        });
    }

}
