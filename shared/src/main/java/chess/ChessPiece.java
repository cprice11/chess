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
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN, EN_PASSANT
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
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        Collection<ChessMove> moves = switch (piece.getPieceType()) {
            case PAWN -> pawnMoves(board, position, piece);
            case ROOK -> slidingPieceMoves(board, position, piece, true, false);
//            case KNIGHT -> knightSquares(position);
            case BISHOP -> slidingPieceMoves(board, position, piece, false, true);
            case QUEEN -> slidingPieceMoves(board, position, piece, true,true);
//            case KING -> kingSquares(position);
            case EN_PASSANT -> new HashSet<ChessMove>();
            default -> new HashSet<ChessMove>();
        };
//        moves.removeIf(move -> !move.getEndPosition().isOnBoard()); // is this only for the knight?
//        removeFriendlyCaptures(board, moves, piece);
        board.paintMoves(moves);
        board.printBoard();
        board.clearPaint();
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        HashSet<ChessMove.MoveBuilder> visibleSquares = new HashSet<>();
        int rank = position.getRank();
        int file = position.getFile();
        int direction = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int promotionRank = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 8 : 1;
        // forward
        ChessPosition forward = new ChessPosition(rank + direction, file);
        if (board.getPiece(forward) == null) {
            visibleSquares.add(new ChessMove.MoveBuilder(position, forward, null));
            ChessPosition leap = new ChessPosition(rank + 2 * direction, file);
            if (((rank == 2 && direction == 1) || (rank == 7 && direction == -1)) && board.getPiece(leap) == null) {
                visibleSquares.add(new ChessMove.MoveBuilder(position, leap, null).leap(forward));
            }
        }
        // diagonals
        ChessPosition attackLeft = new ChessPosition(rank + direction, file - 1);
        ChessPosition attackRight = new ChessPosition(rank + direction, file + 1);
        ChessPiece attackedLeft = board.getPiece(attackLeft);
        ChessPiece attackedRight = board.getPiece(attackRight);
        if (attackedLeft != null && isEnemy(attackedLeft)) visibleSquares.add(new ChessMove.MoveBuilder(position, attackLeft).capture(attackedLeft));
        if (attackedRight != null && isEnemy(attackedRight)) visibleSquares.add(new ChessMove.MoveBuilder(position, attackRight).capture(attackedRight));
        HashSet<ChessMove> availableMoves = new HashSet<>();
        // add promotion options
        for (ChessMove.MoveBuilder move : visibleSquares) {
            int endRank = move.end.getRank();
            if (endRank == promotionRank) {
                availableMoves.add(move.copy().promotion(PieceType.ROOK).build());
                availableMoves.add(move.copy().promotion(PieceType.KNIGHT).build());
                availableMoves.add(move.copy().promotion(PieceType.BISHOP).build());
                availableMoves.add(move.copy().promotion(PieceType.QUEEN).build());
            }
            else availableMoves.add(move.build());
        }
        return availableMoves;
    }

    private Collection<ChessPosition> knightSquares(ChessPosition position) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        int rank = position.getRank();
        int file = position.getFile();
        visibleSquares.add(new ChessPosition(rank - 1, file - 2));
        visibleSquares.add(new ChessPosition(rank + 1, file - 2));
        visibleSquares.add(new ChessPosition(rank + 2, file - 1));
        visibleSquares.add(new ChessPosition(rank + 2, file + 1));
        visibleSquares.add(new ChessPosition(rank - 1, file + 2));
        visibleSquares.add(new ChessPosition(rank + 1, file + 2));
        visibleSquares.add(new ChessPosition(rank - 2, file - 1));
        visibleSquares.add(new ChessPosition(rank - 2, file + 1));
        return visibleSquares;
    }

    private Collection<ChessPosition> kingSquares(ChessPosition position) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        int rank = position.getRank();
        int file = position.getFile();
        visibleSquares.add(new ChessPosition(rank - 1, file - 1));
        visibleSquares.add(new ChessPosition(rank, file - 1));
        visibleSquares.add(new ChessPosition(rank + 1, file - 1));
        visibleSquares.add(new ChessPosition(rank + 1, file));
        visibleSquares.add(new ChessPosition(rank + 1, file + 1));
        visibleSquares.add(new ChessPosition(rank, file + 1));
        visibleSquares.add(new ChessPosition(rank - 1, file + 1));
        visibleSquares.add(new ChessPosition(rank - 1, file));
        return visibleSquares;
    }

    private Collection<ChessMove> slidingPieceMoves(ChessBoard board, ChessPosition startingPosition, ChessPiece piece, boolean orthoganal, boolean diagonal){
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        if (orthoganal) {
            visibleSquares.addAll(expandInDirection(board, startingPosition, 0, 1));
            visibleSquares.addAll(expandInDirection(board, startingPosition, 0, -1));
            visibleSquares.addAll(expandInDirection(board, startingPosition, 1, 0));
            visibleSquares.addAll(expandInDirection(board, startingPosition, -1, 0));
        }
        if (diagonal) {
            visibleSquares.addAll(expandInDirection(board, startingPosition, 1, 1));
            visibleSquares.addAll(expandInDirection(board, startingPosition, 1, -1));
            visibleSquares.addAll(expandInDirection(board, startingPosition, -1, 1));
            visibleSquares.addAll(expandInDirection(board, startingPosition, -1, -1));
        }
        HashSet<ChessMove> availableMoves = new HashSet<>();
        for (ChessPosition square : visibleSquares) {
            ChessMove.MoveBuilder move  = new ChessMove.MoveBuilder(startingPosition, square).piece(piece);
            ChessPiece attackedPiece = board.getPiece(square);
            if (attackedPiece == null) availableMoves.add(move.build());
            else if (isEnemy(attackedPiece)) availableMoves.add(move.capture(attackedPiece).build());
        }
        return availableMoves;
    }

    private boolean isFriendly(ChessPiece piece) {
        return piece.getTeamColor() == color;
    }

    private boolean isEnemy(ChessPiece piece) {
        return !isFriendly(piece);
    }

    private Collection<ChessMove> removeFriendlyCaptures(Collection<ChessMove> moves) {
        moves.removeIf(move ->
                move.isCapture() && move.getCapturedPiece().getTeamColor() == move.getTeam()
        );
        return moves;
    }

    private Collection<ChessPosition> expandInDirection(ChessBoard board, ChessPosition position, int x, int y) {
        if (x * x != 1 && y * y != 1) throw new RuntimeException("Invalid direction values");
        if (x * x > 1 || y * y > 1) throw new RuntimeException("Invalid direction values");
        HashSet<ChessPosition> spaces = new HashSet<>();
        int nextRank = position.getRank() + y;
        int nextFile = position.getFile() + x;
        ChessPosition nextPosition = new ChessPosition(nextRank, nextFile);
        while (nextPosition.isOnBoard()) {
            spaces.add(nextPosition);
            if (board.getPiece(nextPosition) != null) break;
            nextRank += y;
            nextFile += x;
            nextPosition = new ChessPosition(nextRank, nextFile);
        }
        return spaces;
    }

    @Override
    public String toString() {
        return switch (type) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
            case EN_PASSANT -> null;
        };
    }

    public String symbol() {
        return switch (type) {
            case KING -> "♚";
            case QUEEN -> "♛";
            case BISHOP -> "♝";
            case KNIGHT -> "♞";
            case ROOK -> "♜";
            case PAWN -> "♟";
            case EN_PASSANT -> null;
        };
    }

    public char getChar() {
        String letter = toString();
        letter = (color == ChessGame.TeamColor.WHITE) ? letter.toUpperCase() : letter.toLowerCase();
        return letter.charAt(0);
    }
}
