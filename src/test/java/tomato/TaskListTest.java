package tomato;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TaskListTest {

    @Test
    public void deleteTask_validTaskId_success(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");


        assertDoesNotThrow(() -> {
            tasks.deleteTask(1);
        });

        assertDoesNotThrow(() -> {
            tasks.deleteTask(0);
        });
    }

    @Test
    public void deleteTask_invalidTaskId_exceptionThrown(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        assertThrowsExactly(TomatoException.class, () -> {
            tasks.deleteTask(3);
        });

        assertThrowsExactly(TomatoException.class, () -> {
            tasks.deleteTask(4);
        });

        assertThrowsExactly(TomatoException.class, () -> {
            tasks.deleteTask(-1);
        });
    }
}
