package commands;

import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        ui.showTomatoDialog(tasks.printMatchingTasks(keyword));
    }
}