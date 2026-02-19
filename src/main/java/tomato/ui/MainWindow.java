package tomato.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import tomato.Tomato;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Tomato tomato;
    private Ui ui;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Sets the Tomato instance and initializes the UI adapter.
     *
     * @param t Tomato instance.
     */
    public void setTomatoGui(Tomato t) {
        tomato = t;
        ui = new Ui(dialogContainer, userInput);
        t.setGui(ui);
    }

    /**
     * Handles text entered by the user.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        tomato.handleResponse(input);
    }
}
