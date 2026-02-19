package tomato;

/**
 * Represents an exception thrown by Tomato operations.
 */
public class TomatoException extends Exception {
    private String errorWord;

    public TomatoException(String message) {
        super(message);
    }

    public TomatoException(String message, String errorWord) {
        super(message);
        this.errorWord = errorWord;
    }

    public String getErrorWord() {
        return errorWord;
    }
}
