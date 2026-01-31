package tomato;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TaskListTest {
    @Test
    public void createDeadline_correctArgumentsInput_success() {
        TaskList tasks = new TaskList();
        assertDoesNotThrow(() -> {
            tasks.createDeadline("deadline return books /by 2/2/2025 1945");
        });

        assertDoesNotThrow(() -> {
            tasks.createDeadline("deadline buy books /by 4/2/2026 0930");
        });
    }

    @Test
    public void createDeadline_wrongArgumentsInput_exceptionThrown(){
        TaskList tasks = new TaskList();
        assertThrowsExactly(TaskListException.class, () -> {
            tasks.createDeadline("deadline return books /by 1945 2/2/2025");
        });

        assertThrowsExactly(TaskListException.class, () -> {
            tasks.createDeadline("deadline buy books /by June 9th");
        });

        assertThrowsExactly(TaskListException.class, () -> {
            tasks.createDeadline("deadline buy books by 4/2/2026 0930");
        });

        assertThrowsExactly(TaskListException.class, () -> {
            tasks.createDeadline("deadline buy books /from 4/2/2026 0930");
        });
    }

    @Test
    public void createDeadline_wrongDateFormatInput_exceptionThrown(){
        TaskList tasks = new TaskList();
        assertThrowsExactly(TaskListException.class, () -> {
            tasks.createDeadline("deadline return books /by 1945 2/2/2025");
        });

        assertThrowsExactly(TaskListException.class, () -> {
            tasks.createDeadline("deadline buy books /by June 9th");
        });
    }

    @Test
    public void deleteTask_validTaskId_success(){
        TaskList tasks = new TaskList();
        tasks.createDeadline("deadline buy books /by 4/2/2026 0930");
        tasks.createDeadline("deadline return books /by 2/2/2025 1945");

        assertDoesNotThrow(() -> {
            tasks.deleteTask("2");
        });

        assertDoesNotThrow(() -> {
            tasks.deleteTask("1");
        });
    }

    @Test
    public void deleteTask_invalidTaskId_exceptionThrown(){
        TaskList tasks = new TaskList();
        tasks.createDeadline("deadline buy books /by 4/2/2026 0930");
        tasks.createDeadline("deadline return books /by 2/2/2025 1945");

        assertThrowsExactly(TaskListException.class, () -> {
            tasks.deleteTask("3");
        });

        assertThrowsExactly(TaskListException.class, () -> {
            tasks.deleteTask("4");
        });
    }
}
