package tomato.commands;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.ui.Ui;

/**
 * Represents a command that finds tasks by keyword.
 */
public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";
    public static final String MESSAGE_USAGE = "find {keyword} e.g.(find book, find meeting)";
    private final String keyword;

    /**
     * Creates a find command.
     *
     * @param keyword Keyword to search in tasks.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        ui.showTomatoDialog(tasks.printMatchingTasks(keyword));
    }
}
