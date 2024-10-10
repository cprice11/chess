package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType type;
    private final ChessGame.TeamColor color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Sets the piece to a new piece type. Useful for promotion.
     *
     * @param newType The new piece type
     */
    public void changeType(PieceType newType) {
        this.type = newType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        board.printBoard();
        Collection<ChessMove> moves = switch (piece.getPieceType()) {
            case PAWN -> pawnMoves(board, position, piece);
            case ROOK -> rookMoves(board, position, piece);
            case KNIGHT -> knightMoves(board, position, piece);
            case BISHOP -> bishopMoves(board, position, piece);
            case QUEEN -> queenMoves(board, position, piece);
            case KING -> kingMoves(board, position, piece);
        };
        removeFriendlyCaptures(board, moves, piece);
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int currRank = position.getRank();
        int currFile = position.getFile();
        for (int i = currRank + 1; i < 8; i++) {
            ChessPosition newPosition = new ChessPosition(i, currFile);
            ChessPiece existingPiece = board.getPiece(newPosition);
            if (existingPiece != null) {
                moves.add(new ChessMove(position, newPosition, piece.getPieceType()));
                break;
            }
            moves.add(new ChessMove(position, newPosition, piece.getPieceType()));
        }
        for (int i = currRank - 1; i > 0; i--) {
            ChessPosition newPosition = new ChessPosition(i, currFile);
            ChessPiece existingPiece = board.getPiece(newPosition);
            if (existingPiece != null) {
                moves.add(new ChessMove(position, newPosition, piece.getPieceType()));
                break;
            }
            moves.add(new ChessMove(position, newPosition, piece.getPieceType()));
        }
        for (int i = currFile + 1; i < 8; i++) {
            ChessPosition newPosition = new ChessPosition(currRank, i);
            ChessPiece existingPiece = board.getPiece(newPosition);
            if (existingPiece != null) {
                moves.add(new ChessMove(position, newPosition, piece.getPieceType()));
                break;
            }
            moves.add(new ChessMove(position, newPosition, piece.getPieceType()));
        }
        for (int i = currFile - 1; i > 0; i--) {
            ChessPosition newPosition = new ChessPosition(currRank, i);
            ChessPiece existingPiece = board.getPiece(newPosition);
            if (existingPiece != null) {
                moves.add(new ChessMove(position, newPosition, piece.getPieceType()));
                break;
            }
            moves.add(new ChessMove(position, newPosition, piece.getPieceType()));
        }
        return moves;
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }
    private void removeFriendlyCaptures(ChessBoard board, Collection<ChessMove> moves, ChessPiece piece) {
        moves.removeIf(move ->
                board.getPiece(move.getEndPosition()) != null &&
                board.getPiece(move.getEndPosition()).getTeamColor() == piece.getTeamColor()
        );
    }

    private Collection<ChessPosition> expandInDirection(ChessBoard board, ChessPosition position, int x, int y) {
        if ((x != 1 && x != -1) || (y != 1 && y != -1)) throw new RuntimeException("Invalid direction values");
        HashSet<ChessPosition> spaces = new HashSet<>();
        int nextRank = position.getRank() + y;
        int nextFile = position.getFile() + x;
        ChessPosition nextPosition = new ChessPosition(nextRank, nextFile);
        while (nextPosition.isOnBoard()) {
            spaces.add(nextPosition);
            if (board.getPiece(nextPosition) != null) break;
            nextRank += y;
            nextRank += x;
            nextPosition = new ChessPosition(nextRank, nextFile);
        }
        return spaces;
    }

    private Collection<ChessMove> positionsToMoves(ChessPosition start, Collection<ChessPosition> positions) {
        HashSet<ChessMove> moves = new HashSet<>();
        for (ChessPosition end : positions) {
            moves.add(new ChessMove(start, end, null));
        }
        return moves;
    }



    @Override
    public String toString() {
        return switch (type){
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
    }

    public String symbol() {
        return switch (type){
            case KING -> "♚";
            case QUEEN -> "♛";
            case BISHOP -> "♝";
            case KNIGHT -> "♞";
            case ROOK -> "♜";
            case PAWN -> "♟";
        };
    }
}
