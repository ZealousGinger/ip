package tomato;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class StorageStub extends Storage {
    /**
     * Instantiates the storage class with a specified file path for storage file.
     *
     * @param filePath string of file path to store the task file.
     */
    public StorageStub(String filePath) {
        super(filePath);
    }

    @Override
    public void saveToDisk(ArrayList<Task> tasks) {
        System.out.println("Stub save to disk.");
    }
}

public class ParserTest {
    @Test
    public void parseAndExecute_correctArgumentsInput_success() {
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        Parser parser = new Parser(tasks, storage);

        assertDoesNotThrow(() -> {
            parser.parseAndExecute("deadline return books /by 2/2/2025 1945");
        });

        assertDoesNotThrow(() -> {
            parser.parseAndExecute("deadline buy books /by 4/2/2026 0930");
        });
    }

    @Test
    public void parseAndExecute_wrongArgumentsInput_exceptionThrown(){
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        Parser parser = new Parser(tasks, storage);

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parseAndExecute("deadline return books /by 1945 2/2/2025");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parseAndExecute("deadline buy books /by June 9th");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parseAndExecute("deadline buy books by 4/2/2026 0930");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parseAndExecute("deadline buy books /from 4/2/2026 0930");
        });
    }

    @Test
    public void parseAndExecute_wrongDateFormatInput_exceptionThrown(){
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        Parser parser = new Parser(tasks, storage);

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parseAndExecute("deadline return books /by 1945 2/2/2025");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parseAndExecute("deadline buy books /by June 9th");
        });
    }

    @Test
    public void parseAndExecute_invalidInputCommand_exceptionThrown() {
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        Parser parser = new Parser(tasks, storage);

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parseAndExecute("jdhfjhdf");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parseAndExecute("BYEE");
        });
    }

    @Test
    public void parseAndExecute_validInputCommand_success() {
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        Parser parser = new Parser(tasks, storage);

        assertDoesNotThrow(() -> {
            parser.parseAndExecute("LIST");
        });

        assertDoesNotThrow(() -> {
            parser.parseAndExecute("BYE");
        });
    }
}
