package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final PieceType type;
    private final ChessGame.TeamColor color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece otherPiece = (ChessPiece) o;
        return type == otherPiece.type && color == otherPiece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    @Override
    public String toString() {
        String letter = switch (type) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
            default -> null;
        };
        letter = color == ChessGame.TeamColor.BLACK ? letter.toLowerCase() : letter;
        return letter;
    }

    public ChessPiece(ChessGame.TeamColor color, ChessPiece.PieceType type) {
        this.type = type;
        this.color = color;
    }

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

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return Which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        return switch (piece.getPieceType()) {
            case ROOK -> rookMoves(board, myPosition);
            default -> new HashSet<ChessMove>();
        };
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        moves.addAll(slideMoves(board, myPosition, piece, 1, 0));
        moves.addAll(slideMoves(board, myPosition, piece, -1, 0));
        moves.addAll(slideMoves(board, myPosition, piece,0, 1));
        moves.addAll(slideMoves(board, myPosition, piece,0, -1));
        return moves;
    }

    private Collection<ChessMove> slideMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int rankIteration, int fileIteration) {
        int startRank = myPosition.getRank();
        int startFile = myPosition.getFile();
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        for (int i = 1; i < 8; i++) {
            ChessPosition nextPosition = new ChessPosition(startRank + i * rankIteration, startFile + i * fileIteration);
            if (!nextPosition.isOnBoard()) break;
            ChessPiece nextPiece = board.getPiece(nextPosition);
            ChessMove nextMove =  new ChessMove(myPosition, nextPosition, null);
            if (nextPiece != null) {
                if (nextPiece.getPieceType() != piece.getPieceType()) {
                    moves.add(nextMove);
                }
                break;
            }
            moves.add(nextMove);
        }
        return moves;
    }
}
