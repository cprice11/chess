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
    private final ChessPiece.PieceType promotionPiece;

    // King specific
    private final boolean isCastle;
    private final ChessPosition castlingRookPosition;
    private final ChessPosition newCastlingRookPosition;

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
        this.promotionPiece = builder.promotionPiece;
        // King specific
        this.isCastle = builder.isCastle;
        this.castlingRookPosition = builder.castlingRookPosition;
        this.newCastlingRookPosition = builder.newCastlingRookPosition;
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
        private ChessPiece.PieceType promotionPiece;
        // King specific
        private boolean isCastle = false;
        private ChessPosition castlingRookPosition = null;
        private ChessPosition newCastlingRookPosition = null;

        public MoveBuilder(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionPiece) {
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
                    .castlesShortWith(this.castlingRookPosition, this.newCastlingRookPosition)
                    .castlesLongWith(this.castlingRookPosition, this.newCastlingRookPosition)
                    .leap(this.positionSkippedByLeap)
                    .capture(this.capturedPiece);
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
            return this;
        }

        public MoveBuilder castlesShortWith(ChessPosition castlingRook, ChessPosition newRookPosition) {
            if (castlingRook == null || newRookPosition == null) return this;
            this.isCastle = true;
            this.castlingRookPosition = castlingRook;
            this.newCastlingRookPosition = newRookPosition;
            return this;
        }

        public MoveBuilder castlesLongWith(ChessPosition castlingRook, ChessPosition newRookPosition) {
            if (castlingRook == null || newRookPosition == null) return this;
            this.isCastle = true;
            this.castlingRookPosition = castlingRook;
            this.newCastlingRookPosition = newRookPosition;
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

    public ChessPosition getCastlingRookPosition() {
        return castlingRookPosition;
    }

    public ChessPosition getNewCastlingRookPosition() {
        return newCastlingRookPosition;
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
