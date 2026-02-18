package tomato;

import commands.Command;
import org.junit.jupiter.api.Test;
import task.Task;
import ui.Ui;

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
        Parser parser = new Parser();

        assertDoesNotThrow(() -> {
            parser.parse("deadline return books /by 2/2/2025 1945");
        });

        assertDoesNotThrow(() -> {
            parser.parse("deadline buy books /by 4/2/2026 0930");
        });
    }

    @Test
    public void parseAndExecute_wrongArgumentsInput_exceptionThrown(){
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        Parser parser = new Parser();

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("deadline return books /by 1945 2/2/2025");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("deadline buy books /by June 9th");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("deadline buy books by 4/2/2026 0930");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("deadline buy books /from 4/2/2026 0930");
        });
    }

    @Test
    public void parseAndExecute_wrongDateFormatInput_exceptionThrown(){
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        Parser parser = new Parser();

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("deadline return books /by 1945 2/2/2025");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("deadline buy books /by June 9th");
        });
    }

    @Test
    public void parseAndExecute_invalidInputCommand_exceptionThrown() {
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        assertThrowsExactly(TomatoException.class, () -> {
            Command cmd = parser.parse("jdhfjhdf");
            cmd.execute(tasks, ui, storage);
        });

        assertThrowsExactly(TomatoException.class, () -> {
            Command cmd = parser.parse("BYEE");
            cmd.execute(tasks, ui, storage);
        });
    }

    @Test
    public void parseAndExecute_validInputCommand_success() {
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        Parser parser = new Parser();

        assertDoesNotThrow(() -> {
            parser.parse("LIST");
        });

        assertDoesNotThrow(() -> {
            parser.parse("BYE");
        });
    }
}
