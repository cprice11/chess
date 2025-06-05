package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 */
public class ChessPosition {
    private final int rank;
    private final int file;

    public ChessPosition(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    public ChessPosition(String code) {
        char fileChar = code.charAt(0);
        this.file = switch (fileChar) {
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> 0;
        };
        this.rank = code.charAt(1) - '0'; // Why does this work?
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

    /**
     * @return the letter of the position's file from a - h or '?'
     */
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

    @Override
    public String toString() {
        return getFileChar() + String.valueOf(rank);
    }
}
