package chess;

import java.util.HashMap;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private static final String DARK_SQUARE = "[48;2;{r};{g};{b}m";
    private static final String LIGHT_SQUARE = "[48;2;{r};{g};{b}m";
    private static final String DARK_PIECE = "[48;2;{r};{g};{b}m";
    private static final String LIGHT_PIECE = "[48;2;{r};{g};{b}m";
    private static final String RESET_CODE = "[48;2;{r};{g};{b}m";
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }

    private static final int BOARD_SIZE = 8; // no magic numbers, If adding alternate rule sets, refactor to be set.
    private final HashMap<ChessPosition, ChessPiece> pieces = new HashMap<ChessPosition, ChessPiece>();

    public ChessBoard() {
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieces.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return pieces.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() { // For alternate rules this might need to be overridden.
        pieces.clear();
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        for (int i = 1; i <= BOARD_SIZE; i++) {
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        printBoard();
    }

    public void printBoard() {
        StringBuilder boardString = new StringBuilder();
        ChessGame.TeamColor boardColor = ChessGame.TeamColor.WHITE;
        boardString.append("    A  B  C  D  E  F  G  H ");
        for (int rank = BOARD_SIZE; rank > 0; rank--) {
            boardString.append(' ').append(rank).append(' ');
            for (int file = 1; file < BOARD_SIZE; file++) {
                if ((rank + file) % 2 == 0) boardString.append(LIGHT_SQUARE);
                else boardString.append(DARK_SQUARE);
                ChessPiece piece = pieces.get(new ChessPosition(rank, file));
                String textColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? LIGHT_PIECE : DARK_PIECE;
                if (piece == null) boardString.append("   ");
                else boardString.append(" ").append(textColor).append(piece.symbol()).append(" ");
            }
            boardString.append(RESET_CODE).append(' ').append(rank).append('\n');
        }
        System.out.println(boardString);
    }
}
