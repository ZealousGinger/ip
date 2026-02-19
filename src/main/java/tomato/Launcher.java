package tomato;

import javafx.application.Application;

/**
 * Launches Tomato with a classpath-safe entry point.
 */
public class Launcher {
    /**
     * Starts the JavaFX launcher entry point.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
