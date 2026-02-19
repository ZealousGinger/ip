package ui;

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
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
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
        } catch (IOException e) {
            e.printStackTrace();
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
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Sets the dialog box to an error style (red).
     */
    private void setError() {
        // code generated with AI
        dialog.getStyleClass().add("error-label");
    }

    /**
     * Sets word of a text as highlighted for errors.
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


    public static DialogBox getErrorDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        db.setError();
        return db;
    }

    public static DialogBox getErrorHighlightedDialog(String text, String errorWord, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        db.setError();
        db.highlightErrorWord(text, errorWord);
        return db;
    }

    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    public static DialogBox getTomatoDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
}