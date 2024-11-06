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
    public final boolean isCastle;
    public final boolean isCastleShort;
    public final boolean isCastleLong;
    public final boolean isLeap;
    public final boolean isPromotion;
    public final boolean isCapture;
    public ChessPosition enPassant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(start, chessMove.start) && Objects.equals(end, chessMove.end) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, promotionPiece);
    }

//    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
//                     ChessPiece.PieceType promotionPiece) {
//        start = startPosition;
//        end = endPosition;
//        this.promotionPiece = promotionPiece;
//    }

    private ChessMove(MoveBuilder builder) {
        this.start = builder.start;
        this.end = builder.end;
        this.promotionPiece = builder.promotionPiece;
        this.isCastleShort = builder.isCastleShort;
        this.isCastleLong = builder.isCastleLong;
        this.isCastle = builder.isCastle;
        this.isLeap = builder.isLeap;
        this.isPromotion = builder.isPromotion;
        this.isCapture = builder.isCapture;
        this.enPassant = builder.enPassant;
    }

    public static class MoveBuilder {
        private ChessPosition start;
        private ChessPosition end;
        private ChessPiece.PieceType promotionPiece;
        public boolean isCastleShort = false;
        public boolean isCastleLong = false;
        public boolean isCastle = false;
        public boolean isLeap = false;
        public boolean isPromotion = false;
        public boolean isCapture = false;
        public ChessPosition enPassant = null;

        public MoveBuilder start(ChessPosition position) {
            this.start = position;
            return this;
        }
        public MoveBuilder end(ChessPosition position) {
            this.end = position;
            return this;
        }
        public MoveBuilder promotion(ChessPiece.PieceType piece) {
            this.promotionPiece = piece;
            this.isPromotion = true;
            return this;
        }
        public MoveBuilder isCastleShort() {
            this.isCastleShort = true;
            this.isCastle = true;
            return this;
        }
        public MoveBuilder isCastleLong() {
            this.isCastleLong = true;
            this.isCastle = true;
            return this;
        }
        public MoveBuilder isLeap() {
            this.isLeap = true;
            return this;
        }
        public MoveBuilder isCapture() {
            this.isCapture = true;
            return this;
        }
        public MoveBuilder pieceCapturedByEnPassant(ChessPosition position) {
            this.enPassant = position;
            return this;
        }

        public ChessMove build() {
            return new ChessMove(this);
        }
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

    @Override
    public String toString() {
        return start + "-" + end;
    }

}
