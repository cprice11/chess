package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board;
    private boolean whiteShortCastleAllowed;
    private boolean blackShortCastleAllowed;
    private boolean whiteLongCastleAllowed;
    private boolean blackLongCastleAllowed;
    private ChessPosition enPassant = null;
    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        whiteShortCastleAllowed = true;
        blackShortCastleAllowed = true;
        whiteLongCastleAllowed = true;
        blackLongCastleAllowed = true;
        turn = TeamColor.WHITE;
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // If game over throw
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        if (piece == null) throw new InvalidMoveException("No piece at location: " + move.getStartPosition());
        TeamColor teamColor = piece.getTeamColor();
        if (teamColor != turn) throw new InvalidMoveException("Not " + teamColor + "'s turn.");
        if (!piece.pieceMoves(board, start).contains(move)) throw new InvalidMoveException(move + " is an invalid move");
        Collection<ChessPosition> threatenedPositions = getSquaresThreatenedBy(piece.getTeamColor());
        int homeRank = (teamColor == TeamColor.WHITE) ? 1 : 8;
        if (move.isCastleLong || move.isCastleShort) {
            List<ChessPosition> kingsPositions = new ArrayList<>();
            kingsPositions.add(start);
            kingsPositions.add(end);
            if (move.isCastleLong) {
                kingsPositions.add(new ChessPosition(homeRank, 3));
                kingsPositions.add(new ChessPosition(homeRank, 4));
            } else {
                kingsPositions.add(new ChessPosition(homeRank, 6));
            }
            Collection<ChessPosition> overlap = new ArrayList<>();
            kingsPositions.forEach((ChessPosition position) -> {
                if (threatenedPositions.contains(position)) overlap.add(position);
            });
            if (overlap.isEmpty()) throw new InvalidMoveException("Cannot castle through check");
        }
        if (move.isLeap) {
            board.addPiece(move.enPassant, new ChessPiece(teamColor, ChessPiece.PieceType.EN_PASSANT));
        }
        // If pawn leap set enpassant square
        // IF in check
        //  -   check if move gets out
        // ELSE
        //  -
        board.removePiece(start);
        if (move.getPromotionPiece() != null) piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        board.addPiece(move.getEndPosition(), piece);
        turn = otherTeam(turn);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return getSquaresThreatenedBy(otherTeam(teamColor)).contains(getKingPosition(teamColor));
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

    private Collection<ChessPosition> getSquaresThreatenedBy(TeamColor color) {
        HashSet<ChessPosition> threatenedSquares = new HashSet<>();
        board.getPieces(color).forEach((position, piece) ->
                piece.pieceMoves(board, position).forEach(move ->
                        threatenedSquares.add(move.getEndPosition())
                )
        );
        return threatenedSquares;
    }

    private ChessPosition getKingPosition(TeamColor color) {
        HashMap<ChessPosition, ChessPiece> pieces = board.getPieces(color);
        for(Map.Entry<ChessPosition, ChessPiece> position : board.getPieces(color).entrySet()) {
            if (position.getValue().getPieceType() == ChessPiece.PieceType.KING) {
                return position.getKey();
            }
        }
        return null;
    }

    private TeamColor otherTeam(TeamColor color) {
        return color == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }
}
