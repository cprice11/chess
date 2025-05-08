package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int rank;
    private final int file;
    private final char fileCharacter;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return getRank() == that.getRank() && getFile() == that.getFile();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRank(), getFile());
    }

    public ChessPosition(int rank, int file) {
        this.rank = rank;
        this.file = file;
        this.fileCharacter = switch (file) {
            case 1 -> 'A';
            case 2 -> 'B';
            case 3 -> 'C';
            case 4 -> 'D';
            case 5 -> 'E';
            case 6 -> 'F';
            case 7 -> 'G';
            case 8 -> 'H';
            default -> '?';
        };
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row or first rank
     */
    public int getRow() {
        return getRank();
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row or A file
     */
    public int getColumn() {
        return getFile();
    }

    /**
     * @return which rank this position is in
     * 1 codes for the first rank
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * @return which file this position is in
     * 1 codes for the A file
     */
    public int getFile() {
        return this.file;
    }

    /**
     * @return the letter of the position's file A-H
     */
    public char getFileCharacter() {
        return this.fileCharacter;
    }

    @Override
    public String toString() {
        return fileCharacter + String.valueOf(rank);
    }
}
