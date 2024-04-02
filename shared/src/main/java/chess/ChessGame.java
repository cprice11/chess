package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    GameState state;


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }


    public ChessGame() {
        state = new GameState();
    }

    public ChessGame(GameState state) {
        this.state = state;
        state.prettyPrint();
    }



    public GameState state(){
        return state;
    }

    public void state(GameState state){
        this.state = state;
    }



//    public void loadFen(String fenString) {
//        state.board().loadFenPosition(fenString);
//    }

    // moves
    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        if (state.board().getPiece(startPosition) == null) return null;
        moves = state.getValidMoves(startPosition);
        state.prettyPrint();
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        state.makeMove(move);
        state.prettyPrint();
    }

    public void makeMove(String move) throws InvalidMoveException {
        state.makeMove(move);
    }

    // getter setters
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return state.turn();
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        state.turn(team);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return state.board();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        state.board(board);
        state.resetFlags();
        state.prettyPrint();
    }

    // boolean flags
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return state.check(teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return state.checkmate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return state.stalemate(teamColor);
    }

}
