package tomato;

import org.junit.jupiter.api.Test;
import tomato.data.TaskList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListTest {

    @Test
    public void deleteTask_validTaskId(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        assertEquals(2, tasks.getTaskList().size());
        assertDoesNotThrow(() -> {
            tasks.deleteTask(1);
        });

        assertDoesNotThrow(() -> {
            tasks.deleteTask(0);
        });
        assertEquals(0, tasks.getTaskList().size());
    }

    @Test
    public void deleteTask_invalidTaskId_exceptionThrown(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        // test case 1: out of bound index
        assertEquals(2, tasks.getTaskList().size());
        assertThrowsExactly(TomatoException.class, () -> {
            tasks.deleteTask(3);
        });
        assertEquals(2, tasks.getTaskList().size());

        // test case 2: negative index
        assertThrowsExactly(TomatoException.class, () -> {
            tasks.deleteTask(-1);
        });
        assertEquals(2, tasks.getTaskList().size());
    }

    @Test
    public void markTask_validTaskId(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        assertEquals(" ", tasks.getTaskList().get(0).getStatusIcon());
        assertEquals(" ", tasks.getTaskList().get(1).getStatusIcon());

        assertDoesNotThrow(() -> {
            tasks.markTask(1);
        });

        assertEquals(" ", tasks.getTaskList().get(0).getStatusIcon());
        assertEquals("X", tasks.getTaskList().get(1).getStatusIcon());
    }

    @Test
    public void markTask_invalidTaskId_exceptionThrown(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        // test cases 1: out of bound index
        assertThrowsExactly(TomatoException.class, () -> {
            tasks.markTask(3);
        });

        assertThrowsExactly(TomatoException.class, () -> {
            tasks.markTask(2);
        });

        // test case 2: negative index
        assertThrowsExactly(TomatoException.class, () -> {
            tasks.markTask(-1);
        });
    }

    @Test
    public void unmarkTask_validTaskId(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");
        try {
            tasks.markTask(0);
            tasks.markTask(1);
        } catch (TomatoException e) {
            System.out.println(e.getMessage());
        }

        assertEquals("X", tasks.getTaskList().get(0).getStatusIcon());
        assertEquals("X", tasks.getTaskList().get(1).getStatusIcon());

        assertDoesNotThrow(() -> {
            tasks.unmarkTask(1);
        });

        assertEquals("X", tasks.getTaskList().get(0).getStatusIcon());
        assertEquals(" ", tasks.getTaskList().get(1).getStatusIcon());
    }

    @Test
    public void unmarkTask_invalidTaskId_exceptionThrown(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        // test case 1: out of bound index
        assertThrowsExactly(TomatoException.class, () -> {
            tasks.unmarkTask(3);
        });

        // test case 2: negative index
        assertThrowsExactly(TomatoException.class, () -> {
            tasks.unmarkTask(-1);
        });
    }

    @Test
    public void updateDescription_valid(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        assertTrue(tasks.getTaskList().get(0).toString().contains("buy books"));

        assertDoesNotThrow(() -> {
            tasks.updateDescription(0, "borrow books from library");
        });

        assertTrue(tasks.getTaskList().get(0).toString().contains("borrow books from library"));
    }

    @Test
    public void updateDescription_emptyDescription_exceptionThrown(){
        TaskList tasks = new TaskList();
        tasks.createTodo("buy books");
        tasks.createTodo("return books to library");

        assertTrue(tasks.getTaskList().get(0).toString().contains("buy books"));

        assertThrowsExactly(TomatoException.class, () -> {
            tasks.updateDescription(0, "");
        });

        assertThrowsExactly(TomatoException.class, () -> {
            tasks.updateDescription(0, "  ");
        });

        assertTrue(tasks.getTaskList().get(0).toString().contains("buy books"));
    }


}
