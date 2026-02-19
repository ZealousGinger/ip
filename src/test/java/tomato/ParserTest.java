package tomato;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import tomato.commands.Command;
import tomato.data.TaskList;
import tomato.parser.Parser;
import tomato.storage.Storage;
import tomato.task.Task;
import tomato.ui.UserInterface;

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
    public void parse_correctArgumentsInput_success() {
        Parser parser = new Parser();

        assertDoesNotThrow(() -> {
            parser.parse("deadline return books /by 2/2/2025 1945");
        });

        assertDoesNotThrow(() -> {
            parser.parse("deadline buy books /by 4/2/2026 0930");
        });

        assertDoesNotThrow(() -> {
            parser.parse("update 1 /description abcdefgh");
        });

        assertDoesNotThrow(() -> {
            parser.parse("update 1 /by 2/2/2024 1900");
        });

        assertDoesNotThrow(() -> {
            parser.parse("update 1 /time 2/2/2024 1900 /to 3/3/2024 1900");
        });
    }

    @Test
    public void parse_wrongArgumentsInput_exceptionThrown(){
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

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("update 1 /description");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("update 1 /by 2/2/2024 19dfd00");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("update 1 /time 2/2/20dfd24 1900 /to 3/3/2024 1900");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("update 1 /time 2/2/2024 1900");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("update 1 /time 2/2/2024 1900 /to");
        });
    }

    @Test
    public void parse_wrongDateFormatInput_exceptionThrown(){
        Parser parser = new Parser();

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("deadline return books /by 1945 2/2/2025");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            parser.parse("deadline buy books /by June 9th");
        });
    }

    @Test
    public void parse_invalidInputCommand_exceptionThrown() {
        Storage storage = new StorageStub("test");
        TaskList tasks = new TaskList();
        UserInterface ui = new UserInterface();
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
    public void parse_validInputCommand_success() {
        Parser parser = new Parser();

        assertDoesNotThrow(() -> {
            parser.parse("LIST");
        });

        assertDoesNotThrow(() -> {
            parser.parse("BYE");
        });
    }
}
