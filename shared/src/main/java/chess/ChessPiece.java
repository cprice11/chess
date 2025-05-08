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
//            default -> null;
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
            case ROOK -> rookMoves(board, myPosition, piece);
            case BISHOP -> bishopMoves(board, myPosition, piece);
            case QUEEN -> queenMoves(board, myPosition, piece);
            case KNIGHT -> knightMoves(board, myPosition, piece);
            case KING -> kingMoves(board, myPosition, piece);
            case PAWN -> pawnMoves(board, myPosition, piece);
//            default -> new HashSet<ChessMove>();
        };
    }

    /**
     * Calculates all the positions a rook can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new HashSet<>();
        moves.addAll(slideMoves(board, myPosition, piece, 1, 0));
        moves.addAll(slideMoves(board, myPosition, piece, -1, 0));
        moves.addAll(slideMoves(board, myPosition, piece,0, 1));
        moves.addAll(slideMoves(board, myPosition, piece,0, -1));
        return moves;
    }

    /**
     * Calculates all the positions a bishop can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new HashSet<>();
        moves.addAll(slideMoves(board, myPosition, piece, 1, -1));
        moves.addAll(slideMoves(board, myPosition, piece, 1, 1));
        moves.addAll(slideMoves(board, myPosition, piece,-1, -1));
        moves.addAll(slideMoves(board, myPosition, piece,-1, 1));
        return moves;
    }

    /**
     * Calculates all the positions a queen can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new HashSet<>();
        moves.addAll(rookMoves(board, myPosition, piece));
        moves.addAll(bishopMoves(board, myPosition, piece));
        return moves;
    }

    /**
     * Calculates all the positions a knight can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        int currentRank = myPosition.getRank();
        int currentFile = myPosition.getFile();
        HashSet<ChessPosition> hops = new HashSet<>();
        hops.add(new ChessPosition(currentRank + 2, currentFile - 1));
        hops.add(new ChessPosition(currentRank + 2, currentFile + 1));
        hops.add(new ChessPosition(currentRank - 2, currentFile - 1));
        hops.add(new ChessPosition(currentRank - 2, currentFile + 1));
        hops.add(new ChessPosition(currentRank + 1, currentFile - 2));
        hops.add(new ChessPosition(currentRank - 1, currentFile - 2));
        hops.add(new ChessPosition(currentRank + 1, currentFile + 2));
        hops.add(new ChessPosition(currentRank - 1, currentFile + 2));
        return hopMoves(board, myPosition, piece, hops);
    }

    /**
     * Calculates all the positions a king can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        int currentRank = myPosition.getRank();
        int currentFile = myPosition.getFile();
        HashSet<ChessPosition> hops = new HashSet<>();
        hops.add(new ChessPosition(currentRank + 1, currentFile - 1));
        hops.add(new ChessPosition(currentRank + 1, currentFile));
        hops.add(new ChessPosition(currentRank + 1, currentFile + 1));
        hops.add(new ChessPosition(currentRank, currentFile - 1));
        hops.add(new ChessPosition(currentRank, currentFile + 1));
        hops.add(new ChessPosition(currentRank - 1, currentFile - 1));
        hops.add(new ChessPosition(currentRank - 1, currentFile));
        hops.add(new ChessPosition(currentRank - 1, currentFile + 1));
        return hopMoves(board, myPosition, piece, hops);
    }

    /**
     * Calculates all the positions a pawn can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new HashSet<>();
        int currentRank = myPosition.getRank();
        int currentFile = myPosition.getFile();
        int advancementValue = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int promotionRank = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 8 : 1;
        boolean onStart = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? currentRank == 2 : currentRank == 7;
        ChessPosition leftCapture = new ChessPosition(currentRank + advancementValue, currentFile - 1);
        ChessPosition rightCapture = new ChessPosition(currentRank + advancementValue, currentFile + 1);
        ChessPiece leftCapturePiece = board.getPiece(leftCapture);
        ChessPiece rightCapturePiece = board.getPiece(rightCapture);
        if (leftCapturePiece != null && leftCapturePiece.getTeamColor() != piece.getTeamColor()) {
            moves.add(new ChessMove(myPosition, leftCapture, null));
        }
        if (rightCapturePiece != null && rightCapturePiece.getTeamColor() != piece.getTeamColor()) {
            moves.add(new ChessMove(myPosition, rightCapture, null));
        }
        ChessPosition oneForward = new ChessPosition(currentRank + advancementValue, currentFile);
        if (board.getPiece(oneForward) == null) {
            moves.add(new ChessMove(myPosition, oneForward, null));
            ChessPosition twoForward = new ChessPosition(currentRank + 2 * advancementValue, currentFile);
            if (onStart && board.getPiece(twoForward) == null) {
                moves.add(new ChessMove(myPosition, twoForward, null));
            }
        }
        Collection<ChessMove> movesWithPromotion = new HashSet<>();
        moves.forEach(move -> {
            if (move.getEndPosition().getRank() == promotionRank) {
                ChessPosition start = move.getStartPosition();
                ChessPosition end = move.getEndPosition();
                movesWithPromotion.add(new ChessMove(start, end, PieceType.ROOK));
                movesWithPromotion.add(new ChessMove(start, end, PieceType.KNIGHT));
                movesWithPromotion.add(new ChessMove(start, end, PieceType.BISHOP));
                movesWithPromotion.add(new ChessMove(start, end, PieceType.QUEEN));
            }
            else {
                movesWithPromotion.add(move);
            }
        });
        return movesWithPromotion;
    }

    /**
     * Calculates all the positions a chess piece can slide to unobstructed and possibly capture
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @param rankIteration how much the rank should change each step. -1, 0, or 1
     * @param fileIteration how much the file should change each step. -1, 0, or 1
     * @return Collection of valid moves
     */
    private Collection<ChessMove> slideMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int rankIteration, int fileIteration) {
        int startRank = myPosition.getRank();
        int startFile = myPosition.getFile();
        Collection<ChessMove> moves = new HashSet<>();
        for (int i = 1; i < 8; i++) {
            ChessPosition nextPosition = new ChessPosition(startRank + i * rankIteration, startFile + i * fileIteration);
            if (!nextPosition.isOnBoard()) break;
            ChessPiece nextPiece = board.getPiece(nextPosition);
            ChessMove nextMove =  new ChessMove(myPosition, nextPosition, null);
            if (nextPiece != null) {
                if (nextPiece.getTeamColor() != piece.getTeamColor()) {
                    moves.add(nextMove);
                }
                break;
            }
            moves.add(nextMove);
        }
        return moves;
    }

    /**
     * Calculates moves from a collection of end positions that are to empty spaces or enemy captures
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> hopMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece, Collection<ChessPosition> hops) {
        Collection<ChessMove> moves = new HashSet<>();
        hops.removeIf(hop -> {
            if (!hop.isOnBoard()) return true;
            ChessPiece nextPiece = board.getPiece(hop);
            if (nextPiece == null) return false;
            return nextPiece.getTeamColor() == piece.getTeamColor();
        });
        hops.forEach(hop -> moves.add(new ChessMove(myPosition, hop, null)));
        return moves;
    }
}
