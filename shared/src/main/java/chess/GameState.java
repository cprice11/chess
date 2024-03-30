package chess;

import java.util.Vector;

public class GameState {
    ChessGame.TeamColor turn;
    ChessBoard board;
    boolean whiteIsInCheck;
    boolean blackIsInCheck;
    boolean isCheckmate;
    boolean isStalemate;
    Vector<ChessMove> history;

    public GameState() {
        turn = ChessGame.TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    public GameState(ChessBoard board, ChessGame.TeamColor turn) {
        turn = ChessGame.TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }


    public void makeMove(ChessMove move) {
        throw new RuntimeException("Not implemented");
    }

    public ChessBoard getBoard() {
        return board;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public ChessGame.TeamColor getTurn() {
        return turn;
    }

    public void setTurn(ChessGame.TeamColor turn) {
        this.turn = turn;
    }

    public boolean isInCheck(ChessGame.TeamColor team) {
        boolean inCheck = (team == ChessGame.TeamColor.WHITE)? whiteIsInCheck : blackIsInCheck;
        return inCheck;
    }

    public void setInCheck(ChessGame.TeamColor color, boolean inCheck) {
        if (color == ChessGame.TeamColor.WHITE) whiteIsInCheck = inCheck;
        else blackIsInCheck = inCheck;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public void setCheckmate(boolean checkmate) {
        isCheckmate = checkmate;
    }

    public boolean isStalemate() {
        return isStalemate;
    }
    public void setStalemate(boolean stalemate) {
        isStalemate = stalemate;
    }

    public boolean isInStalemate(ChessGame.TeamColor team) {
        throw new RuntimeException("Not implemented");
    }

    public boolean isInCheckmate(ChessGame.TeamColor team) {
        throw new RuntimeException("Not implemented");
    }


}