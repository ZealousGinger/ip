package tomato.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Represents a dialog box with speaker text and profile image.
 */
public class DialogBox extends HBox {
    @FXML
    private TextFlow dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        dialog.getChildren().add(new Text(text));
        displayPicture.setImage(img);

        // Solution to fix text flow vertical wrapping issues from ai
        HBox.setHgrow(dialog, Priority.NEVER);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> childNodes = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(childNodes);
        getChildren().setAll(childNodes);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Applies error styling to this dialog box.
     */
    private void setError() {
        // code generated with AI
        dialog.getStyleClass().add("error-label");
    }

    /**
     * Highlights the provided error word in the given text.
     *
     * @param fullText Full text to render.
     * @param errorWord Word to highlight.
     */
    private void highlightErrorWord(String fullText, String errorWord) {
        // code generated with AI
        dialog.getChildren().clear();
        String[] parts = fullText.split("(?=" + errorWord + ")|(?<=" + errorWord + ")");

        for (String part : parts) {
            Text t = new Text(part);
            if (part.equals(errorWord)) {
                t.setStyle("-fx-fill: white; -fx-font-weight: bold;");
            }
            dialog.getChildren().add(t);
        }
    }


    /**
     * Returns an error-styled dialog box.
     *
     * @param text Dialog text.
     * @param img Speaker image.
     * @return Error dialog box.
     */
    public static DialogBox getErrorDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        db.setError();
        return db;
    }

    /**
     * Returns an error-styled dialog box with a highlighted error word.
     *
     * @param text Dialog text.
     * @param errorWord Word to highlight.
     * @param img Speaker image.
     * @return Error dialog box with highlight.
     */
    public static DialogBox getErrorHighlightedDialog(String text, String errorWord, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        db.setError();
        db.highlightErrorWord(text, errorWord);
        return db;
    }

    /**
     * Returns a dialog box for user messages.
     *
     * @param text Dialog text.
     * @param img Speaker image.
     * @return User dialog box.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Returns a dialog box for Tomato messages.
     *
     * @param text Dialog text.
     * @param img Speaker image.
     * @return Tomato dialog box.
     */
    public static DialogBox getTomatoDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
}
