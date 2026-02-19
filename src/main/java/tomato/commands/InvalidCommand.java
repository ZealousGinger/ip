package tomato.commands;

import tomato.TomatoException;
import tomato.data.TaskList;
import tomato.storage.Storage;
import tomato.ui.UserInterface;

/**
 * Represents a command that reports invalid user input.
 */
public class InvalidCommand extends Command {
    private static final String ERROR_MESSAGE = "Invalid command given: ";
    private static final String LIST_COMMANDS_MESSAGE = "\nPlease try a given command: " +
            "[bye, list, mark, todo, deadline, event, delete, find, " +
            "update]. ";

    private final String invalidCmd;

    /**
     * Creates an invalid-command response command.
     *
     * @param invalidCmd Invalid command entered by the user.
     */
    public InvalidCommand(String invalidCmd) {
        this.invalidCmd = invalidCmd;
    }

    @Override
    public void execute(TaskList tasks, UserInterface ui, Storage storage) throws TomatoException {
        throw new TomatoException(ERROR_MESSAGE + invalidCmd + LIST_COMMANDS_MESSAGE, invalidCmd);
    }
}
