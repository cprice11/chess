package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

// moving logic out of here to make this class as straight forward as possible.

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private Collection<ChessMove> moves;
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;

//    public ChessPosition getPosition() {
//        return position;
//    }
//
//    public void setPosition(ChessPosition position) {
//        this.position = position;
//    }
//
//    private ChessPosition position;




    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType pieceType) {
        color = pieceColor;
        type = pieceType;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChessPiece that = (ChessPiece) obj;
        return (color == that.getTeamColor() && type == that.type());
    }

    public Collection<ChessMove> PieceMoves() {
        return moves;
    }

    public Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition position) {
        return moves;
    }

    public void setPieceMoves(Collection<ChessMove> moves) {
        this.moves = moves;
    }

    public void addMove(ChessMove move) {
        this.moves = moves;
    }

    // Basic access methods
    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType type() {
        return type;
    }

    /**
     * @return The one-character code to represent this piece
     */
    public char code() {
        return switch (type) {
            case PieceType.PAWN   -> (color == ChessGame.TeamColor.WHITE) ? 'P' : 'p';
            case PieceType.ROOK   -> (color == ChessGame.TeamColor.WHITE) ? 'R' : 'r';
            case PieceType.KNIGHT -> (color == ChessGame.TeamColor.WHITE) ? 'N' : 'n';
            case PieceType.BISHOP -> (color == ChessGame.TeamColor.WHITE) ? 'B' : 'b';
            case PieceType.QUEEN  -> (color == ChessGame.TeamColor.WHITE) ? 'Q' : 'q';
            case PieceType.KING   -> (color == ChessGame.TeamColor.WHITE) ? 'K' : 'k';
            default -> ' ';
        };
    }


}
