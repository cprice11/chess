package chess;

/**
 * Indicates an invalid move was made in a game
 */
public class InvalidPositionException extends Exception {

    public InvalidPositionException() {}

    public InvalidPositionException(String message) {
        super(message);
    }
}
