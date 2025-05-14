package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 */
public class ChessPosition {
    private final int rank;
    private final int file;

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
    }

    /**
     * @return which rank this position is in
     * 1 codes for the white back rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @return which file this position is in
     * 1 codes for the 'a' file
     */
    public int getFile() {
        return file;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return getRank();
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return getFile();
    }

    public char getFileChar() {
        return switch (file) {
            case 1 -> 'a';
            case 2 -> 'b';
            case 3 -> 'c';
            case 4 -> 'd';
            case 5 -> 'e';
            case 6 -> 'f';
            case 7 -> 'g';
            case 8 -> 'h';
            default -> '?';
        };
    }

    @Override
    public String toString() {
        return getFileChar() + String.valueOf(rank);
    }
}
