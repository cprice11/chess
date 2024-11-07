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
    private final ChessPiece piece;
    private final ChessGame.TeamColor team;
    private final boolean isCapture;
    private final ChessPiece capturedPiece;
    // Pawn specific
    private final boolean isLeap;
    private final ChessPosition positionSkippedByLeap;
    private final ChessPosition positionBeingCapturedByEnPassant;
    private final boolean isPromotion;
    private final ChessPiece.PieceType promotionPiece;

    // King specific
    private final boolean isCastle;
    private final boolean isCastleShort;
    private final boolean isCastleLong;
    private final ChessPosition castlingRook;

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

    private ChessMove(MoveBuilder builder) {
        this.start = builder.start;
        this.end = builder.end;
        this.piece = builder.piece;
        this.team = builder.team;
        this.isCapture = builder.isCapture;
        this.capturedPiece = builder.capturedPiece;
        // Pawn specific
        this.isLeap = builder.isLeap;
        this.positionSkippedByLeap = builder.positionSkippedByLeap;
        this.positionBeingCapturedByEnPassant = builder.postitionBeingCapturedByEnPassant;
        this.isPromotion = builder.isPromotion;
        this.promotionPiece = builder.promotionPiece;
        // King specific
        this.isCastle = builder.isCastle;
        this.isCastleShort = builder.isCastleShort;
        this.isCastleLong = builder.isCastleLong;
        this.castlingRook = builder.castlingRook;
    }

    public ChessMove(ChessPosition start, ChessPosition end, ChessPiece.PieceType pieceType) {
        this(new MoveBuilder(start, end, pieceType));
    }

    public ChessMove(ChessPosition start, ChessPosition end) {
        this(new MoveBuilder(start, end, null));
    }

    public static class MoveBuilder {
        public final ChessPosition start;
        public final ChessPosition end;
        public ChessPiece piece;
        public ChessGame.TeamColor team;
        private boolean isCapture = false;
        private ChessPiece capturedPiece = null;
        // Pawn specific
        private boolean isLeap = false;
        private ChessPosition positionSkippedByLeap = null;
        private ChessPosition postitionBeingCapturedByEnPassant = null;
        private boolean isPromotion = false;
        private ChessPiece.PieceType promotionPiece;
        // King specific
        private boolean isCastle = false;
        private boolean isCastleShort = false;
        private boolean isCastleLong = false;
        private ChessPosition castlingRook = null;

        public MoveBuilder(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionPiece){
            this.start = start;
            this.end = end;
            promotion(promotionPiece);
        }
        public MoveBuilder(ChessPosition start, ChessPosition end) {
            this(start, end, null);
        }

        public MoveBuilder copy() {
            MoveBuilder copiedBuilder = new MoveBuilder(this.start, this.end, this.promotionPiece);
             return copiedBuilder
                    .piece(this.piece)
                    .castlesShortWith(this.castlingRook)
                    .castlesLongWith(this.castlingRook)
                    .leap(this.positionSkippedByLeap)
                    .capture(this.capturedPiece)
                    .pieceCapturedByEnPassant(this.postitionBeingCapturedByEnPassant);
        }

        public MoveBuilder piece(ChessPiece piece) {
            if (piece == null) return this;
            this.piece = piece;
            this.team = piece.getTeamColor();
            return this;
        }
        public MoveBuilder promotion(ChessPiece.PieceType piece) {
            if (piece == null) return this;
            this.promotionPiece = piece;
            this.isPromotion = true;
            return this;
        }
        public MoveBuilder castlesShortWith(ChessPosition castlingRook) {
            if (castlingRook == null) return this;
            this.isCastleShort = true;
            this.isCastle = true;
            this.castlingRook = castlingRook;
            return this;
        }
        public MoveBuilder castlesLongWith(ChessPosition castlingRook) {
            if (castlingRook == null) return this;
            this.isCastleLong = true;
            this.isCastle = true;
            this.castlingRook  = castlingRook;
            return this;
        }
        public MoveBuilder leap(ChessPosition leapedSquare) {
            if (leapedSquare == null) return this;
            this.positionSkippedByLeap = leapedSquare;
            this.isLeap = true;
            return this;
        }
        public MoveBuilder capture(ChessPiece capture) {
            if (capture == null) return this;
            this.isCapture = true;
            this.capturedPiece = capture;
            return this;
        }
        public MoveBuilder pieceCapturedByEnPassant(ChessPosition position) {
            if (position == null) return this;
            this.postitionBeingCapturedByEnPassant = position;
            return this;
        }

        public ChessMove build() {
            return new ChessMove(this);
        }
    }

    // Getters
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
    public boolean isCastle() {
        return isCastle;
    }
    public boolean isCastleShort() {
        return isCastleShort;
    }
    public boolean isCastleLong() {
        return isCastleLong;
    }
    public ChessPosition getCastlingRook() {
        return castlingRook;
    }
    public boolean isPromotion() {
        return isPromotion;
    }
    public ChessPosition getPositionBeingCapturedByEnPassant() {
        return positionBeingCapturedByEnPassant;
    }
    public ChessPosition getPositionSkippedByLeap() {
        return positionSkippedByLeap;
    }
    public boolean isLeap() {
        return isLeap;
    }
    public ChessPiece getCapturedPiece() {
        return capturedPiece;
    }
    public ChessPiece getPiece() {
        return piece;
    }
    public ChessGame.TeamColor getTeam() {
        return team;
    }
    public boolean isCapture() {
        return isCapture;
    }

    @Override
    public String toString() {
        return start + "-" + end;
    }
}
