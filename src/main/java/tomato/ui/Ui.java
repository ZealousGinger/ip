package tomato.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import tomato.TomatoException;

/**
 * Handles UI rendering and dialog display for Tomato.
 */
public class Ui {
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;


    // Happy user image source: https://unsplash.com/photos/smiley-ball-zDDdoYqQ64U
    private final Image happyUserImage = new Image(
            this.getClass().getResourceAsStream("/images/happy_user.jpg"));

    // Tomato image source: https://unsplash.com/photos/red-tomato-on-gray-concrete-surface-OlXUUQedQyM
    private final Image tomatoImage = new Image(this.getClass().getResourceAsStream("/images/tomato.jpg"));

    // Sad tomato image source: https://pixabay.com/photos/tomatoes-ketchup-sad-food-veggie-1448267/
    private final Image sadTomatoImage = new Image(
            this.getClass().getResourceAsStream("/images/sad_tomato.png"));


    /**
     * Creates a UI instance without JavaFX bindings.
     */
    public Ui() {
    }

    /**
     * Creates a UI instance bound to JavaFX components.
     *
     * @param dialogContainer Container for dialog nodes.
     * @param userInput User input text field.
     */
    public Ui(VBox dialogContainer, TextField userInput) {
        this.dialogContainer = dialogContainer;
        this.userInput = userInput;
    }

    /**
     * Returns the start message.
     */
    public static String getStartMessage() {
        return "\n" + "Hello! I'm Tomato !\n" + "What can I do for you?\n";
    }

    /**
     * Returns the exit message.
     */
    public static String getExitMessage() {
        return "Bye. Hope to see you again soon!\n";
    }

    private void clearInputField() {
        userInput.clear();
    }

    /**
     * Shows the user's message in the dialog container.
     *
     * @param s User input text.
     */
    public void showUserDialog(String s) {
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(s, happyUserImage));
        clearInputField();
    }

    /**
     * Shows an error dialog for the provided exception.
     *
     * @param e Exception to display.
     */
    public void showErrorDialog(Exception e) {
        if (e instanceof TomatoException && ((TomatoException) e).getErrorWord() != null) {
            showErrorHighlightedDialog(e);
        } else {
            dialogContainer.getChildren().addAll(
                    DialogBox.getErrorDialog("ERROR!\n" + e.getMessage(), sadTomatoImage));
        }
    }

    /**
     * Shows an error dialog with highlighted error text.
     *
     * @param e Exception containing the highlight token.
     */
    public void showErrorHighlightedDialog(Exception e) {
        TomatoException te = ((TomatoException) e);
        dialogContainer.getChildren().addAll(
                DialogBox.getErrorHighlightedDialog("ERROR!\n" + e.getMessage(), te.getErrorWord(),
                        sadTomatoImage));
    }

    /**
     * Shows a Tomato response dialog.
     *
     * @param s Tomato response text.
     */
    public void showTomatoDialog(String s) {
        dialogContainer.getChildren().addAll(DialogBox.getTomatoDialog(s, tomatoImage));
    }

    /**
     * Shows the startup dialog.
     */
    public void showStartDialog() {
        dialogContainer.getChildren().addAll(DialogBox.getTomatoDialog(getStartMessage(), tomatoImage));
    }

    /**
     * Shows the exit dialog.
     */
    public void showExitDialog() {
        dialogContainer.getChildren().addAll(
                DialogBox.getTomatoDialog(Ui.getExitMessage(), tomatoImage)
        );
    }

    // Solution below adapted from:
    // https://stackoverflow.com/questions/12153622/how-to-close-a-javafx-application-on-window-close
    // https://stackoverflow.com/questions/76696287/how-do-i-pause-a-javafx-program-without-causing-the-program-to-run-in-a-laggy-fa
    // to exit JavaFX Gui programmatically.
    /**
     * Closes the JavaFX application after a short delay.
     */
    private void exitGui() {
        PauseTransition pause = new PauseTransition(Duration.seconds(.5));
        pause.setOnFinished(event -> Platform.exit());
        pause.play();
    }

    /**
     * Shows exit messages and closes the GUI.
     */
    public void exit() {
        showExitDialog();
        exitGui();
    }
}
