package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final HashMap<ChessPosition, ChessPiece> pieces = new HashMap<>();
    public final int BOARD_SIZE = 8;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pieces);
    }

    public ChessBoard() {
        
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
    public void resetBoard() {
        pieces.clear();
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        for (int i = 1; i <= BOARD_SIZE; i++) {
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }

    public String getFEN() {
        // Turn
        StringBuilder FEN = new StringBuilder(getFENBoard()).append(" ");
//        FEN.append((turn == ChessGame.TeamColor.WHITE) ? "w" : "b").append(" ");
        // Castling
        StringBuilder castling = new StringBuilder();
//        castling.append(whiteCanCastleShort ? "K" : "");
//        castling.append(whiteCanCastleLong ? "Q" : "");
//        castling.append(blackCanCastleShort ? "k" : "");
//        castling.append(blackCanCastleLong ? "q" : "");
        if (castling.isEmpty()) castling.append("-");
        FEN.append(castling).append(" ");
        // enPassant available
//        FEN.append((enPassantTarget == null) ? "-" : enPassantTarget).append(" ");
        // "Clock"
//        FEN.append(getHalfMoveClock()).append(" ");
//        FEN.append(getFullMoveNumber());
        return FEN.toString();
    }

    public String getFENBoard() {
        // Join rows together with slashes
        List<String> rows = new ArrayList<>(8);
        for (int i = BOARD_SIZE; i > 0; i--) {
            rows.add(getFENRow(i));
        }
        return String.join("/", rows);
    }

    public String getFENRow(int rank) {
        int numBlanks = 0;
        StringBuilder row = new StringBuilder();
        for (int i = 1; i <= BOARD_SIZE; i++) {
            ChessPiece piece = getPiece(new ChessPosition(rank, i));
            if (piece == null) numBlanks++;
            else {
                if (numBlanks != 0) row.append(numBlanks);
                numBlanks = 0;
                row.append(piece.toString());
            }
        }
        if (numBlanks != 0) row.append(numBlanks);
        return row.toString();
    }
}
