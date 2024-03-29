package chess;
import java.util.Arrays;


/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private static final int BOARD_SIZE = 8;
    private static final String RESET_HIGHLIGHT = "\u001B[0m";
    private static final String PRIMARY_START = "\u001B[44m";
    private static final String SECONDARY_START = "\u001B[42m";
    private static final String TERNARY_START = "\u001B[43m";
    private static final String NEGATIVE_HIGHLIGHT = "\u001B[41m";

    private ChessPiece[][] positions;
    private Highlight[][] highlightedPositions;

    public enum Highlight {
        PRIMARY,
        SECONDARY,
        TERNARY,
        NEGATIVE,
        NONE
    }


    public ChessBoard() {
        highlightedPositions = new Highlight[8][8];
        for (Highlight[] row : highlightedPositions) {
            Arrays.fill(row, Highlight.NONE);
        }
        positions = new ChessPiece[8][8];
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(positions);
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        positions[position.getRank() - 1][position.getFile() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position  The position to get the piece from
     * @return          Either the piece at the position, or null if no piece is at that position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return positions[position.getRank() - 1][position.getFile() - 1];
    }

    /**
     * Gets the color of a piece on the chessboard
     *
     * @param position  The position to get the piece from
     * @return          Either the color of the piece at the position, or null if no piece is at that position
     */
    public ChessGame.TeamColor getPieceColor(ChessPosition position) {
        if (getPiece(position) == null) return null;
        return positions[position.getRank() - 1][position.getFile() - 1].getTeamColor();
    }

    /**
     * Gets the color highlighting a position on the chessboard
     *
     * @param position  The position to get return the color from
     * @return          The Highlight object specifying the color
     */
    private Highlight getHighlight(ChessPosition position) {
        return highlightedPositions[position.getRank() - 1][position.getFile() - 1];
    }

    /**
     * Highlights the desired square on the board when printed.
     *
     * @param position      The position on the board to highlight
     * @param highlightType The color to highlight the square
     */
    public void highlightPosition(ChessPosition position, Highlight highlightType) {
        highlightedPositions[position.getRank() - 1][position.getFile() - 1] = highlightType;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        positions[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        positions[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        positions[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        positions[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        positions[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        positions[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        positions[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        positions[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        Arrays.fill(positions[6], new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        positions[5] = new ChessPiece[8];
        positions[4] = new ChessPiece[8];
        positions[3] = new ChessPiece[8];
        positions[2] = new ChessPiece[8];

        Arrays.fill(positions[1], new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        positions[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        positions[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        positions[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        positions[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        positions[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        positions[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        positions[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        positions[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        // Reset highlights as well
        resetHighlight();
    }

    /**
     * Removes all highlight marks from board
     */
    public void resetHighlight() {
        for (Highlight[] highlightedPosition : highlightedPositions) {
            Arrays.fill(highlightedPosition, Highlight.NONE);
        }
    }

    /**
     * Prints board to console with white pieces in capital letters
     */
    public void printBoard() {
        boolean currentlyHighlighting = false;
        for (int i = 0; i < positions.length; i++) {
            System.out.print(8 - i);
            System.out.print(' ');
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < positions[i].length; j++) {
                ChessPosition position = new ChessPosition(8 - i, j + 1);
                ChessPiece piece = getPiece(position);
                Highlight highlightColor = getHighlight(position);
                if (currentlyHighlighting) row.append(RESET_HIGHLIGHT);
                row.append('|');

                // set color according to highlights
                if (highlightColor != Highlight.NONE) {
                    String nextHighlightChar = switch (highlightColor) {
                        case PRIMARY   -> PRIMARY_START;
                        case SECONDARY -> SECONDARY_START;
                        case TERNARY   -> TERNARY_START;
                        case NEGATIVE  -> NEGATIVE_HIGHLIGHT;
                        default -> "\0";
                    };
                    currentlyHighlighting = true;
                    row.append(nextHighlightChar);
                }

                char nextChar = (piece == null)? ' ' : piece.getCode();
                row.append(nextChar);
            }
            if (currentlyHighlighting) row.append(RESET_HIGHLIGHT);
            row.append('|');
            System.out.println(row);
        }
        System.out.println("   A B C D E F G H\n");
    }
}

