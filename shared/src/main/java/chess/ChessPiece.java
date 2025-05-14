package chess;

import java.util.ArrayList;
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
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
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
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
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
            case KING -> kingMoves(board, myPosition, piece);
            case QUEEN -> queenMoves(board, myPosition, piece);
            case BISHOP -> bishopMoves(board, myPosition, piece);
            case KNIGHT -> knightMoves(board, myPosition, piece);
            case ROOK -> rookMoves(board, myPosition, piece);
            case PAWN -> pawnMoves(board, myPosition, piece);
        };
    }

    /**
     * Calculates all the positions a king piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition start, ChessPiece piece) {
        HashSet<ChessPosition> hops = new HashSet<>();
        int startRank = start.getRank();
        int startFile = start.getFile();
        hops.add(new ChessPosition(startRank + 1, startFile - 1));
        hops.add(new ChessPosition(startRank + 1, startFile));
        hops.add(new ChessPosition(startRank + 1, startFile + 1));
        hops.add(new ChessPosition(startRank, startFile - 1));
        hops.add(new ChessPosition(startRank, startFile + 1));
        hops.add(new ChessPosition(startRank - 1, startFile - 1));
        hops.add(new ChessPosition(startRank - 1, startFile));
        hops.add(new ChessPosition(startRank - 1, startFile + 1));
        return movesFromPositions(board, start, piece, hops);
    }

    /**
     * Calculates all the positions a queen piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition start, ChessPiece piece) {
        HashSet<ChessMove> moves = new HashSet<>();
        moves.addAll(rookMoves(board, start, piece));
        moves.addAll(bishopMoves(board, start, piece));
        return moves;
    }

    /**
     * Calculates all the positions a bishop piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition start, ChessPiece piece) {
        HashSet<ChessMove> moves = new HashSet<>();
        moves.addAll(slidingMoves(board, start, piece, 1, -1));
        moves.addAll(slidingMoves(board, start, piece, 1, 1));
        moves.addAll(slidingMoves(board, start, piece, -1, -1));
        moves.addAll(slidingMoves(board, start, piece, -1, 1));
        return moves;
    }

    /**
     * Calculates all the positions a knight piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition start, ChessPiece piece) {
        HashSet<ChessPosition> hops = new HashSet<>();
        int startRank = start.getRank();
        int startFile = start.getFile();
        hops.add(new ChessPosition(startRank + 2, startFile - 1));
        hops.add(new ChessPosition(startRank + 2, startFile + 1));
        hops.add(new ChessPosition(startRank + 1, startFile + 2));
        hops.add(new ChessPosition(startRank - 1, startFile + 2));
        hops.add(new ChessPosition(startRank - 2, startFile + 1));
        hops.add(new ChessPosition(startRank - 2, startFile - 1));
        hops.add(new ChessPosition(startRank - 1, startFile - 2));
        hops.add(new ChessPosition(startRank + 1, startFile - 2));
        return movesFromPositions(board, start, piece, hops);
    }

    /**
     * Calculates all the positions a rook piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition start, ChessPiece piece) {
        HashSet<ChessMove> moves = new HashSet<>();
        moves.addAll(slidingMoves(board, start, piece, 1, 0));
        moves.addAll(slidingMoves(board, start, piece, -1, 0));
        moves.addAll(slidingMoves(board, start, piece, 0, 1));
        moves.addAll(slidingMoves(board, start, piece, 0, -1));
        return moves;
    }

    /**
     * Calculates all the positions a pawn piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     * @return Collection of valid moves
     */
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition start, ChessPiece piece) {
        ChessGame.TeamColor color = piece.getTeamColor();
        int startRank = start.getRank();
        int startFile = start.getFile();
        int advancementValue = color == ChessGame.TeamColor.WHITE ? 1 : -1;
        int promotionRank = color == ChessGame.TeamColor.WHITE ? 8 : 1;
        boolean onStartSquare = color == ChessGame.TeamColor.WHITE ? startRank == 2 : startRank == 7;

        HashSet<ChessPosition> attacks = new HashSet<>();
        attacks.add(new ChessPosition(startRank + advancementValue, startFile - 1));
        attacks.add(new ChessPosition(startRank + advancementValue, startFile + 1));

        HashSet<ChessMove> moves = new HashSet<>(movesFromPositions(board, start, piece, attacks));
        moves.removeIf(move -> !move.isCapture());

        ChessPosition oneForward = new ChessPosition(startRank + advancementValue, startFile);
        ChessPiece oneForwardPiece = board.getPiece(oneForward);
        if (oneForwardPiece == null && board.isOnBoard(oneForward)) {
            moves.add(new ChessMove(start, oneForward, null));
            ChessPosition twoForward = new ChessPosition(startRank + 2 * advancementValue, startFile);
            if (onStartSquare && board.getPiece(twoForward) == null) {
                moves.add(new ChessMove(start, twoForward, null));
            }
        }
        HashSet<ChessMove> movesWithPromotion = new HashSet<>();
        moves.forEach(move -> {
            if (move.getEndPosition().getRank() == promotionRank) {
                movesWithPromotion.addAll(getPromotions(move));
            } else movesWithPromotion.add(move);
        });
        return movesWithPromotion;
    }

    /**
     * Returns a collection of all promotion variations of a ChessMove.
     * ROOK, KNIGHT, BISHOP, and QUEEN.
     * @param move the move that the promotions will take their start and end positions from
     * @return a collection of chess moves with all promotions
     */
    private Collection<ChessMove> getPromotions(ChessMove move) {
        Collection<ChessMove> promotions = new ArrayList<>();
        promotions.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), PieceType.ROOK));
        promotions.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), PieceType.KNIGHT));
        promotions.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), PieceType.BISHOP));
        promotions.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), PieceType.QUEEN));
        return promotions;
    }

    /**
     * Calculates all the positions a chess piece can slide to in a given direction
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition start, ChessPiece piece, int rankIncrement, int fileIncrement) {
        HashSet<ChessPosition> visiblePositions = new HashSet<>();
        int startRank = start.getRank();
        int startFile = start.getFile();

        for (int i = 1; i <= ChessBoard.BOARD_SIZE; i++) {
            ChessPosition nextSpace = new ChessPosition(startRank + i * rankIncrement, startFile + i * fileIncrement);
            if (!board.isOnBoard(nextSpace)) break;
            visiblePositions.add(nextSpace);
            ChessPiece nextPiece = board.getPiece(nextSpace);
            if (nextPiece != null) break;
        }
        return movesFromPositions(board, start, piece, visiblePositions);
    }

    /**
     * Takes a collection of positions and returns moves calculated from them while handling captures.
     * @param board the game board containing the pieces
     * @param start the starting position of the move
     * @param piece the piece on that position
     * @param positions the collection of ChessPositions the moves would end on
     * @return a collection of ChessMoves
     */
    private Collection<ChessMove> movesFromPositions(ChessBoard board, ChessPosition start, ChessPiece piece, Collection<ChessPosition> positions) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor color = piece.getTeamColor();
        positions.forEach(target -> {
            if (!board.isOnBoard(target)) return;
            ChessPiece targetPiece = board.getPiece(target);
            ChessMove hopMove = new ChessMove(start, target, null);
            if (targetPiece != null) {
                if (targetPiece.getTeamColor() == color) return;
                hopMove.setIsCapture(true);
            }
            moves.add(hopMove);
        });
        return moves;
    }

    @Override
    public String toString() {
        String pieceChar = switch (type) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
        return color == ChessGame.TeamColor.WHITE ? pieceChar : pieceChar.toLowerCase();
    }
}
