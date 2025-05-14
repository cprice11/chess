package chess;

import java.util.Hashtable;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final Hashtable<ChessPosition, ChessPiece> pieces = new Hashtable<>();

    public static final int BOARD_SIZE = 8;

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
        System.out.println(prettyBoard());
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece == null || position == null) return;
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

    public boolean isOnBoard(ChessPosition position) {
        int rank = position.getRank();
        int file = position.getFile();
        return rank >= 1 && rank <= BOARD_SIZE && file >= 1 && file <= BOARD_SIZE;
    }

    public String fenString() {
        StringBuilder board = new StringBuilder(fenRow(BOARD_SIZE));
        for (int rank = BOARD_SIZE - 1; rank > 0; rank--) {
            board.append('/');
            board.append(fenRow(rank));
        }
        return board.toString();
    }

    private String fenRow(int rank) {
        StringBuilder row = new StringBuilder();
        int numBlanks = 0;
        for (int file = 1; file <= BOARD_SIZE ; file++) {
            ChessPiece piece = pieces.get(new ChessPosition(rank, file));
            if (piece == null) {
                numBlanks += 1;
            } else {
                if (numBlanks > 0) {
                    row.append(numBlanks);
                    numBlanks = 0;
                }
                row.append(piece);
            }
        }
        return row.toString();
    }

    public String prettyBoard() {
        StringBuilder test = new StringBuilder();
        ChessColor color = new ChessColor(null).lightSquare().lightPiece().build();
        test.append(color);
        test.append(" Hello ");
        test.append(color.getResetString());
        return test.toString();
//        String fileLabels = "   a  b  c  d  e  f  g  h \n";
//        StringBuilder board = new StringBuilder(fileLabels);
//        for (int rank = BOARD_SIZE; rank > 0; rank--) {
//            board.append(rank + " ");
//            for (int file = 1; file <= BOARD_SIZE; file++) {
//
//            }
//
//        }

    }

    @Override
    public String toString() {
        return prettyBoard();
//        StringBuilder board = new StringBuilder();
//        for (int rank = BOARD_SIZE; rank > 0; rank--) {
//            for (int file = 1; file <= BOARD_SIZE; file++) {
//                ChessPiece piece = getPiece(new ChessPosition(rank, file));
//                board.append(piece == null ? "   " : " " + piece + " ");
//            }
//            board.append('\n');
//        }
//        return board.toString();
    }
}
