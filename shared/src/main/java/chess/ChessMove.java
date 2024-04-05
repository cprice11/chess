package chess;

import java.util.Objects;


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
    public final ChessPosition enPassant;
    public final boolean shortCastle;
    public final boolean longCastle;
    public final boolean offerDraw;
    public final boolean castle;

    /**
     *
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        piece = null;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
        isCheck = isMate = shortCastle = longCastle = offerDraw = isCapture = castle = false;
        this.enPassant = null;
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
        this.castle = builder.castle;
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
        return Objects.equals(this.startPosition, that.startPosition) && Objects.equals(this.endPosition, that.endPosition) && Objects.equals(this.promotionPiece, that.promotionPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    public String toString() {
        if (promotionPiece == null) return startPosition + "-" + endPosition;
        return startPosition + "-" + endPosition + "/" + promotionPiece;
    }

    public static class MoveBuilder {
        // required
        public final ChessPosition startPosition;
        public final ChessPosition endPosition;

        // optional
        public ChessPiece piece;
        public ChessPiece.PieceType promotionPiece;
        public boolean isCapture;
        public boolean isCheck;
        public boolean isMate;
        public ChessPosition enPassant;
        public boolean shortCastle;
        public boolean longCastle;
        public boolean offerDraw;
        public boolean castle;

        public MoveBuilder(ChessPosition startPosition, ChessPosition endPosition) {
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }

        public MoveBuilder(ChessMove move) {
            this.piece = move.piece;
            this.startPosition = move.startPosition;
            this.endPosition = move.endPosition;
            this.promotionPiece = move.promotionPiece;
            this.isCapture = move.isCapture;
            this.isCheck = move.isCheck;
            this.isMate = move.isMate;
            this.enPassant = move.enPassant;
            this.shortCastle = move.shortCastle;
            this.longCastle = move.longCastle;
            this.offerDraw = move.offerDraw;
            this.castle = move.castle;
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

        public MoveBuilder isCapture(boolean b) {
            this.isCapture = b;
            return this;
        }

        public MoveBuilder isCheck() {
            this.isCheck = true;
            return this;
        }

        public MoveBuilder isCheck(boolean b) {
            this.isCheck = b;
            return this;
        }

        public MoveBuilder isMate() {
            this.isMate = true;
            return this;
        }

        public MoveBuilder isMate(boolean b) {
            this.isMate = b;
            return this;
        }

        public MoveBuilder enPassant(ChessPosition p) {
            this.enPassant = p;
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

        public MoveBuilder shortCastle(boolean shortCastle) {
            this.shortCastle = shortCastle;
            this.castle = shortCastle || castle;
            return this;
        }

        public MoveBuilder longCastle(boolean longCastle) {
            this.longCastle = longCastle;
            this.castle = longCastle || castle;
            return this;
        }

        public MoveBuilder offerDraw() {
            this.offerDraw = true;
            return this;
        }

        public ChessMove build() {
            return new ChessMove(this);
        }
    }

}
