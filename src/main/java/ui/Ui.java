package ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import tomato.TomatoException;

/**
 * Represents the User Interface class.
 */
public class Ui {
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;


    // happy user image from https://unsplash.com/photos/smiley-ball-zDDdoYqQ64U
    private final Image happyUserImage = new Image(this.getClass().getResourceAsStream("/images/happy_user.jpg"));

    // image from https://unsplash.com/photos/red-tomato-on-gray-concrete-surface-OlXUUQedQyM
    private final Image tomatoImage = new Image(this.getClass().getResourceAsStream("/images/tomato.jpg"));

    // image from https://pixabay.com/photos/tomatoes-ketchup-sad-food-veggie-1448267/
    private final Image sadTomatoImage = new Image(this.getClass().getResourceAsStream("/images/sad_tomato.png"));


    /**
     * Instantiates the User interface class.
     */
    public Ui() {
    }

    /**
     * Instantiates the User interface class with JavaFX GUI components
     */
    public Ui(VBox dialogContainer, TextField userInput) {
        this.dialogContainer = dialogContainer;
        this.userInput = userInput;
    }

    /**
     * Gets the start message.
     */
    public static String getStartMessage() {
        return "\n" + "Hello! I'm Tomato !\n" + "What can I do for you?\n";
    }

    /**
     * Gets the exit message.
     */
    public static String getExitMessage() {
        return "Bye. Hope to see you again soon!\n";
    }

    private void clearInputField() {
        userInput.clear();
    }

    public void showUserDialog(String s) {
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(s, happyUserImage));
        clearInputField();
    }

    public void showErrorDialog(Exception e) {
        if (e instanceof TomatoException && ((TomatoException) e).getErrorWord() != null) {
            showErrorHighlightedDialog(e);
        } else {
            dialogContainer.getChildren().addAll(DialogBox.getErrorDialog("ERROR!\n" + e.getMessage(), sadTomatoImage));
        }
    }

    public void showErrorHighlightedDialog(Exception e) {
        TomatoException te = ((TomatoException) e);
        dialogContainer.getChildren().addAll(DialogBox.getErrorHighlightedDialog("ERROR!\n" + e.getMessage(), te.getErrorWord(), sadTomatoImage));
    }

    public void showTomatoDialog(String s) {
        dialogContainer.getChildren().addAll(DialogBox.getTomatoDialog(s, tomatoImage));
    }

    public void showStartDialog() {
        dialogContainer.getChildren().addAll(DialogBox.getTomatoDialog(getStartMessage(), tomatoImage));
    }

    public void showExitDialog() {
        dialogContainer.getChildren().addAll(
                DialogBox.getTomatoDialog(Ui.getExitMessage(), tomatoImage)
        );
    }

    // Solution below adapted from https://stackoverflow.com/questions/12153622/how-to-close-a-javafx-application-on-window-close
    // and https://stackoverflow.com/questions/76696287/how-do-i-pause-a-javafx-program-without-causing-the-program-to-run-in-a-laggy-fa
    // to exit JavaFX Gui programmatically.
    private void exitGui() {
        PauseTransition pause = new PauseTransition(Duration.seconds(.5));
        pause.setOnFinished(event -> Platform.exit());
        pause.play();
    }

    public void exit() {
        showExitDialog();
        exitGui();
    }
}
