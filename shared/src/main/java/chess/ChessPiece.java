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
        Collection<ChessPosition> squares = switch (piece.getPieceType()) {
            case PAWN -> pawnSquares(board, position, piece);
            case ROOK -> rookSquares(board, position);
            case KNIGHT -> knightSquares(position);
            case BISHOP -> bishopSquares(board, position);
            case QUEEN -> queenSquares(board, position);
            case KING -> kingSquares(position);
            case EN_PASSANT -> new HashSet<ChessPosition>();
        };
        squares.removeIf(square -> !square.isOnBoard());
        removeFriendlyCaptures(board, squares, piece);
        board.paintSquare(position, ChessBoard.PAINT_COLOR.PRIMARY);
        board.paintSquares(squares, ChessBoard.PAINT_COLOR.TERNARY);
        board.printBoard();
        return positionsToMoves(position, squares, piece);
    }

    private Collection<ChessPosition> pawnSquares(ChessBoard board, ChessPosition position, ChessPiece piece) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        int rank = position.getRank();
        int file = position.getFile();
        int direction = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        ChessPosition forward = new ChessPosition(rank + direction, file);
        if (board.getPiece(forward) == null) {
            visibleSquares.add(forward);
            ChessPosition leap = new ChessPosition(rank + 2 * direction, file);
            if (((rank == 2 && direction == 1) || (rank == 7 && direction == -1)) && board.getPiece(leap) == null) {
                visibleSquares.add(leap);
            }
        }
        ChessPosition attackLeft = new ChessPosition(rank + direction, file - 1);
        ChessPosition attackRight = new ChessPosition(rank + direction, file + 1);
        if (board.getPiece(attackLeft) != null) visibleSquares.add(attackLeft);
        if (board.getPiece(attackRight) != null) visibleSquares.add(attackRight);
        return visibleSquares;
    }

    // TODO Add castling squares
    private Collection<ChessPosition> rookSquares(ChessBoard board, ChessPosition position) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        visibleSquares.addAll(expandInDirection(board, position, 0, 1));
        visibleSquares.addAll(expandInDirection(board, position, 0, -1));
        visibleSquares.addAll(expandInDirection(board, position, 1, 0));
        visibleSquares.addAll(expandInDirection(board, position, -1, 0));
        return visibleSquares;
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

    private Collection<ChessPosition> bishopSquares(ChessBoard board, ChessPosition position) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        visibleSquares.addAll(expandInDirection(board, position, 1, 1));
        visibleSquares.addAll(expandInDirection(board, position, 1, -1));
        visibleSquares.addAll(expandInDirection(board, position, -1, 1));
        visibleSquares.addAll(expandInDirection(board, position, -1, -1));
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

    private Collection<ChessPosition> queenSquares(ChessBoard board, ChessPosition position) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        visibleSquares.addAll(expandInDirection(board, position, 0, 1));
        visibleSquares.addAll(expandInDirection(board, position, 0, -1));
        visibleSquares.addAll(expandInDirection(board, position, 1, 0));
        visibleSquares.addAll(expandInDirection(board, position, -1, 0));
        visibleSquares.addAll(expandInDirection(board, position, 1, 1));
        visibleSquares.addAll(expandInDirection(board, position, 1, -1));
        visibleSquares.addAll(expandInDirection(board, position, -1, 1));
        visibleSquares.addAll(expandInDirection(board, position, -1, -1));
        return visibleSquares;
    }

    private void removeFriendlyCaptures(ChessBoard board, Collection<ChessPosition> squares, ChessPiece piece) {
        squares.removeIf(square ->
                board.getPiece(square) != null &&
                        board.getPiece(square).getTeamColor() == piece.getTeamColor()
        );
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

    private Collection<ChessMove> positionsToMoves(ChessPosition start, Collection<ChessPosition> positions, ChessPiece piece) {
        HashSet<ChessMove> moves = new HashSet<>();
        if (piece.getPieceType() == PieceType.PAWN) {
            for (ChessPosition end : positions) {
                if (end.getRank() == 8 || end.getRank() == 1) {
                    moves.add(new ChessMove(start, end, PieceType.ROOK));
                    moves.add(new ChessMove(start, end, PieceType.BISHOP));
                    moves.add(new ChessMove(start, end, PieceType.KNIGHT));
                    moves.add(new ChessMove(start, end, PieceType.QUEEN));
                } else {
                    moves.add(new ChessMove(start, end, null));
                }
            }
        } else {
            for (ChessPosition end : positions) {
                moves.add(new ChessMove(start, end, null));
            }
        }
        return moves;
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
