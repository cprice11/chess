package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    int rank;
    int file;
    char fileLetter;


    public ChessPosition(int rank, int file) {
        this.rank = rank;
        this.file = file;
        switch (this.file) {
            case 1:
                fileLetter = 'a';
                break;
            case 2:
                fileLetter = 'b';
                break;
            case 3:
                fileLetter = 'c';
                break;
            case 4:
                fileLetter = 'd';
                break;
            case 5:
                fileLetter = 'e';
                break;
            case 6:
                fileLetter = 'f';
                break;
            case 7:
                fileLetter = 'g';
                break;
            case 8:
                fileLetter = 'h';
                break;
            default:
                fileLetter = '-';
        }
    }


    public String toString() {
        return getFileLetter() + Integer.toString(getRank());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition position = (ChessPosition) o;
        return rank == position.rank && file == position.file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, file);
    }

    /**
     * @return rank of this position (1 - 8)
     * 1 codes for the bottom row
     */
    public int getRank() {
        return rank;
    }

    /**
     * @return file of this position (1 - 8)
     * 1 codes for the 'A' file
     */
    public int getFile() {
        return file;
    }

    /**
     * @return file of this position (A - G)
     * A codes for the left column
     */
    public char getFileLetter() {
        return fileLetter;
    }

    public boolean isOnBoard() {
        return !isOffBoard();
    }

    public boolean isOffBoard() {
        return (rank > 8 || rank < 1) || (file > 8 || file < 1);
    }


}
