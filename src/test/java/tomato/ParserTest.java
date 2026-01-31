package tomato;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class ParserTest {
    @Test
    public void parseAndExecute_invalidInputCommand_exceptionThrown() {
        Storage storage = new Storage("data/TaskList.txt");
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
        Storage storage = new Storage("data/TaskList.txt");
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
