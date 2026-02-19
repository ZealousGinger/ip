package tomato.commands;

import tomato.storage.Storage;
import tomato.data.TaskList;
import tomato.TomatoException;
import tomato.ui.Ui;

public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";
    public static final String MESSAGE_USAGE = "find {keyword} e.g.(find book, find meeting)";
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        ui.showTomatoDialog(tasks.printMatchingTasks(keyword));
    }
}