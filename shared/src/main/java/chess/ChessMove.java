package chess;

import java.util.Objects;

// Converted into record since moves shouldn't change.

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public final class ChessMove {
    public final ChessPiece piece;
    public final ChessPosition startPosition;
    public final ChessPosition endPosition;
    public final ChessPiece.PieceType promotionPiece;
    public final boolean isCapture;
    public final boolean isCheck;
    public final boolean isMate;
    public final boolean enPassant;
    public final boolean shortCastle;
    public final boolean longCastle;
    public final boolean offerDraw;

    /**
     *
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        piece = null;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
        isCheck = isMate = enPassant = shortCastle = longCastle = offerDraw = isCapture = false;
    }

    public ChessMove(ChessPiece piece, ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece, boolean isCapture, boolean isCheck, boolean isMate,
                     boolean enPassant, boolean shortCastle, boolean longCastle, boolean offerDraw) {
        this.piece = piece;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
        this.isCapture = isCapture;
        this.isCheck = isCheck;
        this.isMate = isMate;
        this.enPassant = enPassant;
        this.shortCastle = shortCastle;
        this.longCastle = longCastle;
        this.offerDraw = offerDraw;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition startPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition endPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType promotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChessMove) obj;
        return Objects.equals(this.startPosition, that.startPosition) &&
                Objects.equals(this.endPosition, that.endPosition) &&
                Objects.equals(this.promotionPiece, that.promotionPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    @Override
    public String toString() {
        return "ChessMove[" +
                "startPosition=" + startPosition + ", " +
                "endPosition=" + endPosition + ", " +
                "promotionPiece=" + promotionPiece + ']';
    }

}
