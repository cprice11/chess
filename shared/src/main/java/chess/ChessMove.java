package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition start;
    private final ChessPosition end;
    private final ChessPiece.PieceType promotionPiece;
    private boolean isCapture = false;
    private boolean canCapture = true;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return     Objects.equals(start, chessMove.start)
                && Objects.equals(end, chessMove.end)
                && getPromotionPiece() == chessMove.getPromotionPiece();
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, getPromotionPiece());
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.start = startPosition;
        this.end = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public boolean getIsCapture() {
        return isCapture;
    }

    public ChessMove isCapture(boolean isCapture) {
        this.isCapture = isCapture;
        return this;
    }

    public boolean getCanCapture() {
        return canCapture;
    }

    public ChessMove canCapture(boolean canCapture) {
        this.canCapture = canCapture;
        return this;
    }

    @Override
    public String toString() {
        String promotionString = promotionPiece == null ? "" :
                "!" + switch (promotionPiece) {
                    case KING -> "K";
                    case QUEEN -> "Q";
                    case BISHOP -> "B";
                    case KNIGHT -> "N";
                    case ROOK -> "R";
                    case PAWN -> "P";
                };
        String separator = isCapture ? "x" : "-";
        return start.toString() + separator + end.toString() + promotionString;
    }
}
