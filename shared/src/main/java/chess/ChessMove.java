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
    private boolean cannotCapture = false;
    private boolean createsEnPassant = false;
    private boolean capturesByEnPassant = false;
    private ChessPosition passedPosition = null;
    private boolean isCastle = false;
    private boolean isShortCastle = false;
    private boolean isLongCastle = false;
    private ChessPosition castlingRookStart = null;
    private ChessPosition castlingRookEnd = null;

    private boolean decorated = false;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(start, chessMove.start)
                && Objects.equals(end, chessMove.end)
                && getPromotionPiece() == chessMove.getPromotionPiece();
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, getPromotionPiece());
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
                    case EN_PASSANT -> "?";
                };
        String separator = isCapture ? "x" : "-";
        return start.toString() + separator + end.toString() + promotionString;
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

    // Decorator methods
    public void decorate(ChessBoard board) {
        ChessPiece target = board.getPiece(end);
        if (target != null) isCapture(true);
        ChessPiece piece = board.getPiece(start);
        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();
        if (type == ChessPiece.PieceType.PAWN) {
            if (start.getFile() == end.getFile()) {
                cannotCapture(true);
            }
            if (Math.abs(end.getRank() - start.getRank()) == 2) {
                int passedRank = color == ChessGame.TeamColor.WHITE ? 3 : 6;
                createsEnPassant(new ChessPosition(passedRank ,start.getFile()));
            }
            if (target != null && target.getPieceType() == ChessPiece.PieceType.EN_PASSANT) {
                capturesByEnPassant(true);
            }
        } else if (type == ChessPiece.PieceType.KING) {
            int homeRank = color == ChessGame.TeamColor.WHITE ? 1 : 8;
            ChessPosition homePosition = new ChessPosition(homeRank, 5);
            if (start.equals(homePosition)) {
                int endFile = end.getFile();
                if (endFile == 3) {
                    isCastle(new ChessPosition(homeRank, 1), new ChessPosition(homeRank, 4), false);
                }
                if (endFile == 7) {
                    isCastle(new ChessPosition(homeRank, 8), new ChessPosition(homeRank, 6), true);
                }
            }
        }
        decorated = true;
    }

    public ChessMove isCapture(boolean isCapture) {
        if (cannotCapture) {
            throw new RuntimeException("Move cannot be capture while cannotCapture is true");
        }
        this.isCapture = isCapture;
        this.decorated = true;
        return this;
    }

    public ChessMove capturesByEnPassant(boolean capturesByEnPassant) {
        isCapture(true);
        this.capturesByEnPassant = capturesByEnPassant;
        decorated = true;
        return this;
    }

    public ChessMove createsEnPassant(ChessPosition passedPosition) {
        createsEnPassant = true;
        this.passedPosition = passedPosition;
        decorated = true;
        return this;
    }

    public ChessMove isCastle(ChessPosition castlingRookStart, ChessPosition castlingRookEnd, boolean isShortCastle) {
        this.isCastle = true;
        this.castlingRookStart = castlingRookStart;
        this.castlingRookEnd = castlingRookEnd;
        if (isShortCastle) this.isShortCastle = true;
        else this.isLongCastle = true;
        decorated = true;
        return this;
    }

    public ChessMove cannotCapture(boolean cannotCapture) {
        if (isCapture) {
            throw new RuntimeException("The cannotCapture flag cannot be set while isCapture is true");
        }
        this.cannotCapture = cannotCapture;
        decorated = true;
        return this;
    }

    // getters
    public boolean decorated() {
        return decorated;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public boolean cannotCapture() {
        return cannotCapture;
    }

    public boolean isCastle() {
        return isCastle;
    }

    public boolean isShortCastle() {
        return isShortCastle;
    }

    public boolean isLongCastle() {
        return isLongCastle;
    }

    public boolean capturesByEnPassant() {
        return capturesByEnPassant;
    }

    public boolean createsEnPassant() {
        return createsEnPassant;
    }

    public ChessPosition passedPosition() {
        return this.passedPosition;
    }

    public ChessPosition getCastlingRookStart() {
        return castlingRookStart;
    }

    public ChessPosition getCastlingRookEnd() {
        return castlingRookEnd;
    }

}
