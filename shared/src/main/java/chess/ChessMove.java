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

    private ChessMove(MoveBuilder builder) {
        this.piece = builder.piece;
        this.startPosition = builder.startPosition;
        this.endPosition = builder.endPosition;
        this.promotionPiece = builder.promotionPiece;
        this.isCapture = builder.isCapture;
        this.isCheck = builder.isCheck;
        this.isMate = builder.isMate;
        this.enPassant = builder.enPassant;
        this.shortCastle = builder.shortCastle;
        this.longCastle = builder.longCastle;
        this.offerDraw = builder.offerDraw;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
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

    public static class MoveBuilder{
        // required
        public final ChessPosition startPosition;
        public final ChessPosition endPosition;

        // optional
        public ChessPiece piece;
        public ChessPiece.PieceType promotionPiece;
        public boolean isCapture;
        public boolean isCheck;
        public boolean isMate;
        public boolean enPassant;
        public boolean shortCastle;
        public boolean longCastle;
        public boolean offerDraw;

        public MoveBuilder(ChessPosition startPosition, ChessPosition endPosition) {
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }
        public MoveBuilder withPiece(ChessPiece piece) {
            this.piece = piece;
            return this;
        }

        public MoveBuilder withPromotion(ChessPiece.PieceType promotionPiece) {
            this.promotionPiece = promotionPiece;
            return this;
        }

        public MoveBuilder isCapture() {
            this.isCapture = true;
            return this;
        }
        public MoveBuilder isCheck() {
            this.isCheck = true;
            return this;
        }
        public MoveBuilder isMate() {
            this.isMate = true;
            return this;
        }
        public MoveBuilder enPassant() {
            this.enPassant = true;
            return this;
        }
        public MoveBuilder shortCastle() {
            this.shortCastle = true;
            return this;
        }
        public MoveBuilder longCastle() {
            this.longCastle = true;
            return this;
        }
        public MoveBuilder offerDraw() {
            this.offerDraw = true;
            return this;
        }

        public ChessMove build(){
            return new ChessMove(this);
        }
    }

}
