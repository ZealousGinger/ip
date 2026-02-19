package tomato;

/**
 * Represents an exception thrown by Tomato operations.
 */
public class TomatoException extends Exception {
    private String errorWord;

    /**
     * Creates a Tomato exception with the specified message.
     *
     * @param message Error message.
     */
    public TomatoException(String message) {
        super(message);
    }

    /**
     * Creates a Tomato exception with a message and highlight token.
     *
     * @param message Error message.
     * @param errorWord Token to highlight in the UI.
     */
    public TomatoException(String message, String errorWord) {
        super(message);
        this.errorWord = errorWord;
    }

    public String getErrorWord() {
        return errorWord;
    }
}
