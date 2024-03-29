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
    int row;
    int file;
    int col;
    char fileLetter;


    public ChessPosition(int rank, int file) {
        this.row = this.rank = rank;
        this.col = this.file = file;
        switch (this.file) {
            case 1:
                fileLetter = 'A';
                break;
            case 2:
                fileLetter = 'B';
                break;
            case 3:
                fileLetter = 'C';
                break;
            case 4:
                fileLetter = 'D';
                break;
            case 5:
                fileLetter = 'E';
                break;
            case 6:
                fileLetter = 'F';
                break;
            case 7:
                fileLetter = 'G';
                break;
            case 8:
                fileLetter = 'I';
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
        return file == position.file && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, col);
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
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




}
