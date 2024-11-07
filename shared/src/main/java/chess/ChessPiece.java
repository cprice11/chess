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
        return switch (piece.getPieceType()) {
            case PAWN -> pawnMoves(board, position);
            case ROOK -> slidingPieceMoves(board, position, true, false);
            case KNIGHT -> knightMoves(board, position);
            case BISHOP -> slidingPieceMoves(board, position, false, true);
            case QUEEN -> slidingPieceMoves(board, position, true, true);
            case KING -> kingMoves(board, position);
            default -> new HashSet<>();
        };
    }

    /**
     * Calculates all the positions a chess piece can capture
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of positions threatened by the piece
     */
    public Collection<ChessPosition> pieceThreats(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        return switch (piece.getPieceType()) {
            case PAWN -> pawnThreats(board, position);
            case ROOK -> slidingPieceThreats(board, position, true, false);
            case KNIGHT -> knightThreats(board, position);
            case BISHOP -> slidingPieceThreats(board, position, false, true);
            case QUEEN -> slidingPieceThreats(board, position, true, true);
            case KING -> kingThreats(board, position);
            default -> new HashSet<>();
        };
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove.MoveBuilder> visibleSquares = new HashSet<>();
        int rank = position.getRank();
        int file = position.getFile();
        int direction = color == ChessGame.TeamColor.WHITE ? 1 : -1;
        int promotionRank = color == ChessGame.TeamColor.WHITE ? 8 : 1;
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
        if (attackedLeft != null && isEnemy(attackedLeft))
            visibleSquares.add(new ChessMove.MoveBuilder(position, attackLeft).capture(attackedLeft));
        if (attackedRight != null && isEnemy(attackedRight))
            visibleSquares.add(new ChessMove.MoveBuilder(position, attackRight).capture(attackedRight));
        HashSet<ChessMove> availableMoves = new HashSet<>();
        // add promotion options
        for (ChessMove.MoveBuilder move : visibleSquares) {
            move.piece(this);
            int endRank = move.end.getRank();
            if (endRank == promotionRank) {
                availableMoves.add(move.copy().promotion(PieceType.ROOK).build());
                availableMoves.add(move.copy().promotion(PieceType.KNIGHT).build());
                availableMoves.add(move.copy().promotion(PieceType.BISHOP).build());
                availableMoves.add(move.copy().promotion(PieceType.QUEEN).build());
            } else availableMoves.add(move.build());
        }
        return availableMoves;
    }

    private Collection<ChessPosition> pawnThreats(ChessBoard board, ChessPosition position) {
        HashSet<ChessPosition> threatenedSquares = new HashSet<>();
        pawnMoves(board, position).stream().filter(ChessMove::isCapture).forEach(move -> threatenedSquares.add(move.getEndPosition()));
        return threatenedSquares;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition startingPosition) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        int rank = startingPosition.getRank();
        int file = startingPosition.getFile();
        visibleSquares.add(new ChessPosition(rank - 1, file - 2));
        visibleSquares.add(new ChessPosition(rank + 1, file - 2));
        visibleSquares.add(new ChessPosition(rank + 2, file - 1));
        visibleSquares.add(new ChessPosition(rank + 2, file + 1));
        visibleSquares.add(new ChessPosition(rank - 1, file + 2));
        visibleSquares.add(new ChessPosition(rank + 1, file + 2));
        visibleSquares.add(new ChessPosition(rank - 2, file - 1));
        visibleSquares.add(new ChessPosition(rank - 2, file + 1));
        HashSet<ChessMove> availableMoves = new HashSet<>();
        for (ChessPosition square : visibleSquares) {
            if (square.isOnBoard()) {
                ChessMove.MoveBuilder move = new ChessMove.MoveBuilder(startingPosition, square).piece(this);
                ChessMove.MoveBuilder handledMove = handleCaptures(move, board);
                if (handledMove != null) availableMoves.add(handledMove.build());
            }
        }
        return availableMoves;
    }

    private Collection<ChessPosition> knightThreats(ChessBoard board, ChessPosition startingPosition) {
        HashSet<ChessPosition> threatenedSquares = new HashSet<>();
        knightMoves(board, startingPosition).forEach(move -> threatenedSquares.add(move.getEndPosition()));
        return threatenedSquares;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition startingPosition) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        int rank = startingPosition.getRank();
        int file = startingPosition.getFile();
        // Standard moves in 8 directions
        visibleSquares.add(new ChessPosition(rank - 1, file - 1));
        visibleSquares.add(new ChessPosition(rank, file - 1));
        visibleSquares.add(new ChessPosition(rank + 1, file - 1));
        visibleSquares.add(new ChessPosition(rank + 1, file));
        visibleSquares.add(new ChessPosition(rank + 1, file + 1));
        visibleSquares.add(new ChessPosition(rank, file + 1));
        visibleSquares.add(new ChessPosition(rank - 1, file + 1));
        visibleSquares.add(new ChessPosition(rank - 1, file));
        HashSet<ChessMove> availableMoves = new HashSet<>();
        for (ChessPosition square : visibleSquares) {
            if (square.isOnBoard()) {
                ChessMove.MoveBuilder move = new ChessMove.MoveBuilder(startingPosition, square).piece(this);
                ChessMove.MoveBuilder handledMove = handleCaptures(move, board);
                if (handledMove != null) availableMoves.add(handledMove.build());
            }
        }
        // Castling
        int homeRank = (color == ChessGame.TeamColor.WHITE) ? 1 : 8;
        // If both pieces haven't moved
        if (board.canCastleShort(color)) {
            Collection<ChessPosition> threatenedPositions = board.positionsThreatenedBy(otherTeam());
            ChessPosition eFile = new ChessPosition(homeRank, 5);
            ChessPosition fFile = new ChessPosition(homeRank, 6);
            ChessPosition gFile = new ChessPosition(homeRank, 7);
            ChessPosition hFile = new ChessPosition(homeRank, 8);
            // And the rook exists (for unit tests, and in case I missed it)
            if (Objects.equals(board.getPiece(hFile), new ChessPiece(color, PieceType.ROOK))) {
                // And these aren't threatened
                if (!threatenedPositions.contains(eFile) &&
                        !threatenedPositions.contains(fFile) &&
                        !threatenedPositions.contains(gFile)
                ) {
                    // And these are empty
                    if (board.getPiece(fFile) == null &&
                            board.getPiece(gFile) == null
                    ) {
                        // add ShortCastle
                        availableMoves.add(
                                new ChessMove.MoveBuilder(startingPosition, gFile)
                                        .piece(this)
                                        .castlesShortWith(hFile, fFile)
                                        .build()
                        );
                    }
                }
            }
        }
        // If both pieces haven't moved
        if (board.canCastleLong(color)) {
            Collection<ChessPosition> threatenedPositions = board.positionsThreatenedBy(otherTeam());
            ChessPosition aFile = new ChessPosition(homeRank, 1);
            ChessPosition bFile = new ChessPosition(homeRank, 2);
            ChessPosition cFile = new ChessPosition(homeRank, 3);
            ChessPosition dFile = new ChessPosition(homeRank, 4);
            ChessPosition eFile = new ChessPosition(homeRank, 5);
            // And the rook exists (for unit tests, and in case I missed it)
            if (Objects.equals(board.getPiece(aFile), new ChessPiece(color, PieceType.ROOK))) {
                // And these aren't threatened
                if (!threatenedPositions.contains(cFile) &&
                        !threatenedPositions.contains(dFile) &&
                        !threatenedPositions.contains(eFile)
                ) {
                    // And these are empty
                    if (board.getPiece(bFile) == null &&
                            board.getPiece(cFile) == null &&
                            board.getPiece(dFile) == null
                    ) {
                        // add LongCastle
                        availableMoves.add(
                                new ChessMove.MoveBuilder(startingPosition, cFile)
                                        .piece(this)
                                        .castlesLongWith(aFile, dFile)
                                        .build()
                        );
                    }
                }
            }
        }
        return availableMoves;
    }

    private Collection<ChessPosition> kingThreats(ChessBoard board, ChessPosition startingPosition) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        int rank = startingPosition.getRank();
        int file = startingPosition.getFile();
        // Standard moves in 8 directions
        visibleSquares.add(new ChessPosition(rank - 1, file - 1));
        visibleSquares.add(new ChessPosition(rank, file - 1));
        visibleSquares.add(new ChessPosition(rank + 1, file - 1));
        visibleSquares.add(new ChessPosition(rank + 1, file));
        visibleSquares.add(new ChessPosition(rank + 1, file + 1));
        visibleSquares.add(new ChessPosition(rank, file + 1));
        visibleSquares.add(new ChessPosition(rank - 1, file + 1));
        visibleSquares.add(new ChessPosition(rank - 1, file));
        visibleSquares.removeIf(square -> !square.isOnBoard());
        visibleSquares.removeIf(square -> board.getPiece(square) != null && board.getPiece(square).getTeamColor() == color);
        return visibleSquares;
    }

    private Collection<ChessMove> slidingPieceMoves(ChessBoard board, ChessPosition startingPosition, boolean orthogonal, boolean diagonal) {
        HashSet<ChessPosition> visibleSquares = new HashSet<>();
        if (orthogonal) {
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
            ChessMove.MoveBuilder move = new ChessMove.MoveBuilder(startingPosition, square).piece(this);
            ChessMove.MoveBuilder handledMove = handleCaptures(move, board);
            if (handledMove != null) availableMoves.add(handledMove.build());
        }
        return availableMoves;
    }

    private Collection<ChessPosition> slidingPieceThreats(ChessBoard board, ChessPosition startingPosition, boolean orthogonal, boolean diagonal) {
        HashSet<ChessPosition> threatenedSquares = new HashSet<>();
        slidingPieceMoves(board, startingPosition, orthogonal, diagonal).forEach(move -> threatenedSquares.add(move.getEndPosition()));
        return threatenedSquares;
    }

    private boolean isFriendly(ChessPiece piece) {
        return piece.getTeamColor() == color;
    }

    private boolean isEnemy(ChessPiece piece) {
        return !isFriendly(piece);
    }

    private ChessMove.MoveBuilder handleCaptures(ChessMove.MoveBuilder move, ChessBoard board) {
        ChessPiece attackedPiece = board.getPiece(move.end);
        if (attackedPiece == null) return move;
        else if (isEnemy(attackedPiece)) return move.capture(attackedPiece);
        return null;
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

    public ChessGame.TeamColor otherTeam() {
        return (color == ChessGame.TeamColor.WHITE) ?
                ChessGame.TeamColor.BLACK :
                ChessGame.TeamColor.WHITE;
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
            case EN_PASSANT -> " ";
        };
    }

    public char getChar() {
        String letter = toString();
        letter = (color == ChessGame.TeamColor.WHITE) ? letter.toUpperCase() : letter.toLowerCase();
        return letter.charAt(0);
    }
}
