package commands;

import tomato.Storage;
import tomato.TaskList;
import tomato.TomatoException;
import ui.Ui;

public class InvalidCommand extends Command {
    private static final String errorMessage = "Invalid command given: ";
    private static final String listCommandsMessage = "\nPlease try a given command: " +
            "[bye, list, mark, todo, deadline, event, delete, find, " +
            "update_description, update_deadline, update_event_from, " +
            "update_event_to, update_event_time]. ";

    private final String invalidCmd;

    public InvalidCommand(String invalidCmd) {
        this.invalidCmd = invalidCmd;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TomatoException {
        throw new TomatoException(errorMessage + invalidCmd + listCommandsMessage);
    }
}