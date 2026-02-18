package ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Represents the User Interface class.
 */
public class Ui {
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.jpg"));
    private final Image tomatoImage = new Image(this.getClass().getResourceAsStream("/images/tomato.jpg"));

    private static final String SPACER = "   ____________________________________________________________";
    private static final String TAB = "    ";


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
        return SPACER + "\n" + TAB + "Hello! I'm Tomato \uD83C\uDF45 !\n" + TAB + "What can I do for you?\n" + SPACER;
    }

    /**
     * Gets the exit message.
     */
    public static String getExitMessage() {
        return TAB + "Bye. Hope to see you again soon!\n" + SPACER;
    }

    private void clearInputField() {
        userInput.clear();
    }

    public void showUserDialog(String s) {
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(s, userImage));
        clearInputField();
    }

    public void showErrorDialog(Exception e) {
        dialogContainer.getChildren().addAll(DialogBox.getTomatoDialog(e.getMessage(), tomatoImage));
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
