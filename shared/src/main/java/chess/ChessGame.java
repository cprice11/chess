package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private Vector<ChessMove> moveHistory = new Vector<>();
    private TeamColor turn = TeamColor.WHITE;
    private boolean whiteCanShortCastle = true;
    private boolean whiteCanLongCastle = true;
    private boolean blackCanShortCastle = true;
    private boolean blackCanLongCastle = true;
    private ChessPosition enPassant = null;
    private ChessPosition positionVulnerableToEnPassant = null;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(getBoard(), chessGame.getBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, getBoard());
    }

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        moves.removeIf(move -> {
            try {
                validateMove(move);
                return false;
            } catch (InvalidMoveException e) {
                return true;
            }
        });
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        validateMove(move);
        TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
        if (color != getTeamTurn()) {
            throw new InvalidMoveException("It is not this piece's turn.");
        }
        updateFlags(move);
        // use special case methods if applicable
        if (move.capturesByEnPassant()) {
            makeEnPassantCaptureMove(move);
            enPassant = null;
            positionVulnerableToEnPassant = null;
            return;
        }
        if (enPassant != null) {
            board.removePiece(enPassant);
            enPassant = null;
            positionVulnerableToEnPassant = null;
        }
        if (move.createsEnPassant()) {
            makeEnPassantMove(move);
            return;
        }
        if (move.isCastle()) {
            makeCastleMove(move);
            return;
        }
        makeStandardMove(move);
    }

    private ChessMove validateMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        ChessPiece piece = board.getPiece(start);
        // Fail fast if move isn't valid
        if (piece == null) {
            throw new InvalidMoveException("There is no piece at this move's start.");
        }
        TeamColor color = piece.getTeamColor();
        if (!piece.pieceMoves(board, start).contains(move)) {
            throw new InvalidMoveException("This piece can't move to that position.");
        }
        move.decorate(board);
        if (move.isCastle()) {
            move = validateCastleMove(move, color);
        }
        return move;
    }

    private ChessMove validateCastleMove(ChessMove move, TeamColor color) throws InvalidMoveException {
        if (isInCheck(color)) {
            throw new InvalidMoveException("Cannot castle while in check");
        }
        if (color == TeamColor.WHITE) { // Throw if king or relevant rook has moved.
            if (move.isLongCastle() && !whiteCanLongCastle) {
                throw new InvalidMoveException("White cannot long castle");
            } else if (move.isShortCastle() && !whiteCanShortCastle) {
                throw new InvalidMoveException("White cannot short castle");
            }
        } else {
            if (move.isLongCastle() && !blackCanLongCastle) {
                throw new InvalidMoveException("Black cannot long castle");
            } else if (move.isShortCastle() && !blackCanShortCastle) {
                throw new InvalidMoveException("Black cannot short castle");
            }
        }
        int homeRank = color == TeamColor.WHITE ? 1 : 8;

        Collection<ChessPosition> inBetweenPositions = new ArrayList<>(); // These must be clear in order to castle
        if (move.isShortCastle()) {
            inBetweenPositions.add(new ChessPosition(homeRank, 6));
            inBetweenPositions.add(new ChessPosition(homeRank, 7));
        } else {
            inBetweenPositions.add(new ChessPosition(homeRank, 2));
            inBetweenPositions.add(new ChessPosition(homeRank, 3));
            inBetweenPositions.add(new ChessPosition(homeRank, 4));
        }
        for (ChessPosition position : inBetweenPositions) {
            ChessPiece piece = board.getPiece(position);
            if (piece != null) {
                throw new InvalidMoveException("The piece at " + position + " blocks the castle");
            }
        }

        Collection<ChessPosition> threatenedPositions = board.getPositionsThreatenedByColor(getOtherTeam(color));
        ChessPosition safePosition = move.isShortCastle() ? new ChessPosition(homeRank, 6) : new ChessPosition(homeRank, 4);
        if (threatenedPositions.contains(safePosition)) {
            throw new InvalidMoveException("Cannot castle through check");
        }
        return move;
    }

    private void makeStandardMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        ChessPiece piece = board.getPiece(start);
        TeamColor color = piece.getTeamColor();
        board.removePiece(start);
        board.addPiece(end, promotionPiece == null ? piece : new ChessPiece(color, promotionPiece));
        board.printBoard();
    }

    private void makeCastleMove(ChessMove move) {
        board.printBoard();
        ChessPiece king = board.removePiece(move.getStartPosition());
        ChessPiece rook = board.removePiece(move.getCastlingRookStart());
        board.addPiece(move.getEndPosition(), king);
        board.addPiece(move.getCastlingRookEnd(), rook);
        board.printBoard();
    }

    private void makeEnPassantMove(ChessMove move) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        TeamColor color = piece.getTeamColor();
        ChessPosition end = move.getEndPosition();
        enPassant = move.passedPosition();
        positionVulnerableToEnPassant = end;
        board.removePiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(enPassant, new ChessPiece(color, ChessPiece.PieceType.EN_PASSANT));
    }

    private void makeEnPassantCaptureMove(ChessMove move) {
        ChessPiece piece = board.removePiece(move.getStartPosition());
        board.removePiece(positionVulnerableToEnPassant);
        board.addPiece(move.getEndPosition(), piece);
    }

    private void updateFlags(ChessMove move) {
        moveHistory.add(move);
        ChessPiece piece = board.getPiece(move.getStartPosition());
        ChessPiece.PieceType type = piece.getPieceType();
        TeamColor color = piece.getTeamColor();

        // Castling flags
        if (type == ChessPiece.PieceType.KING) {
            if (color == TeamColor.WHITE) {
                whiteCanLongCastle = whiteCanShortCastle = false;
            } else {
                blackCanLongCastle = blackCanShortCastle = false;
            }
        } else if (type == ChessPiece.PieceType.ROOK) {
            ChessPosition start = move.getStartPosition();
            if (start.equals(new ChessPosition(1, 1))) {
                whiteCanLongCastle = false;
            }
            if (start.equals(new ChessPosition(1, 8))) {
                whiteCanShortCastle = false;
            }
            if (start.equals(new ChessPosition(8, 1))) {
                blackCanLongCastle = false;
            }
            if (start.equals(new ChessPosition(8, 8))) {
                blackCanShortCastle = false;
            }
        } else if (type == ChessPiece.PieceType.PAWN) {
            halfMoveClock = 0; // resets on pawn advance
        }
        if (move.isCapture()) {
            halfMoveClock = 0; // or captures
        }
        fullMoveNumber += color == TeamColor.BLACK ? 1 : 0; // Increment clock on black turns

        turn = getOtherTeam(turn);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check, false otherwise.
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingSquare = board.getKingSquare(teamColor);
        Collection<ChessPosition> threatenedPositions = board.getPositionsThreatenedByColor(getOtherTeam(teamColor));
        return threatenedPositions.contains(kingSquare);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public TeamColor getOtherTeam(TeamColor color) {
        return color == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    public String fenString() {
        StringBuilder fenString = new StringBuilder(board.positionFenString());
        fenString.append(' ').append(turn == TeamColor.WHITE ? 'w' : 'b').append(' ');
        if (whiteCanShortCastle || whiteCanLongCastle || blackCanShortCastle || blackCanLongCastle) {
            fenString.append(whiteCanShortCastle ? 'K' : "");
            fenString.append(whiteCanLongCastle ? 'Q' : "");
            fenString.append(blackCanShortCastle ? 'k' : "");
            fenString.append(blackCanLongCastle ? 'q' : "");
        } else {
            fenString.append('-');
        }
        fenString.append(' ');
        if (enPassant == null) {
            fenString.append('-');
        } else {
            fenString.append(enPassant);
        }
        fenString.append(' ');
        fenString.append(halfMoveClock).append(' ');
        fenString.append(fullMoveNumber);
        return fenString.toString();
    }

    @Override
    public String toString() {
        return fenString();
    }
}
