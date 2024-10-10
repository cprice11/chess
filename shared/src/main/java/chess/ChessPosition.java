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
    private final char fileChar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return rank == that.rank && file == that.file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, file);
    }

    public ChessPosition(int rank, int file) {
        this.rank = rank;
        this.file = file;
        switch (file) {
            case 1:
                this.fileChar = 'A';
                break;
            case 2:
                this.fileChar = 'B';
                break;
            case 3:
                this.fileChar = 'C';
                break;
            case 4:
                this.fileChar = 'D';
                break;
            case 5:
                this.fileChar = 'E';
                break;
            case 6:
                this.fileChar = 'F';
                break;
            case 7:
                this.fileChar = 'G';
                break;
            case 8:
                this.fileChar = 'H';
                break;
            default:
                this.fileChar = '?';
        }
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.rank;
    }

    public int getRank() {
        return this.rank;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     * Identical to getFile()
     */
    public int getColumn() {
        return this.file;
    }

    /**
     * @return which file this position is on.
     * 1 codes for the A file
     */
    public int getFile() {
        return this.file;
    }

    /**
     * @return which file this position is on.
     * returns '?' in case of undefined file values like 0.
     */
    public char getFileChar() {
        return this.fileChar;
    }

    public boolean isOnBoard() {
        return (rank <= 8 && rank >= 1 && file <= 8 && file >= 1);
    }

    @Override
    public String toString() {
        return fileChar + String.valueOf(rank);
    }
}
