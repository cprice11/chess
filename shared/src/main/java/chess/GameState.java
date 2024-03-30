package chess;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GameState {
    private ChessGame.TeamColor turn;
    private ChessBoard board;
    private Vector<ChessMove> history;
    private HashMap<ChessBoard, Integer> positionHashMap;

    private int movesSinceCapture = 0;
    private boolean whiteIsInCheck;
    private boolean blackIsInCheck;
    private boolean whiteIsInCheckmate;
    private boolean blackIsInCheckmate;
    private boolean whiteIsInStalemate;
    private boolean blackIsInStalemate;
    private boolean whiteKingHasMoved;
    private boolean blackKingHasMoved;




    public GameState() {
        turn = ChessGame.TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
        history = new Vector<ChessMove>();
        positionHashMap = new HashMap<ChessBoard, Integer>();
        whiteIsInCheck = blackIsInCheck = whiteIsInCheckmate = blackIsInCheckmate = whiteIsInStalemate =
                blackIsInStalemate = whiteKingHasMoved = blackKingHasMoved = false;
    }

    public GameState(ChessBoard board, ChessGame.TeamColor turn) {
        this.board = board;
        board.resetBoard();
    }


    public void makeMove(ChessMove move) {
        addHistory(move);
    }

    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.addPiece(position, piece);
    }
    public ChessPiece removePiece(ChessPosition position) {
        return board.removePiece(position);
    }
    public ChessPiece getPiece(ChessPosition position) {
        return board.getPiece(position);
    }

    public Vector<ChessMove> history() {
        return history;
    }
    public void addHistory(ChessMove move) {
        history.add(move);
    }
    public ChessMove getLastMove() {
        return history.lastElement();
    }
    public int getMaxPositionRepeats() {
        int repeats = 0;
        for (int entry : positionHashMap.values()) {
            repeats = Math.max(repeats, entry);
        }
        return repeats;
    }

    public ChessBoard board() {
        return board;
    }
    public void board(ChessBoard board) {
        this.board = board;
    }

    public ChessGame.TeamColor turn() {
        return turn;
    }
    public void turn(ChessGame.TeamColor turn) {
        this.turn = turn;
    }

    public boolean check() {
        return whiteIsInCheck || blackIsInCheck;
    }
    public boolean check(ChessGame.TeamColor team) {
        return (team == ChessGame.TeamColor.WHITE)? whiteIsInCheck : blackIsInCheck;
    }
    public void check(ChessGame.TeamColor color, boolean check) {
        if (color == ChessGame.TeamColor.WHITE) whiteIsInCheck = check;
        else blackIsInCheck = check;
    }
    
    public boolean checkmate() {
        return whiteIsInCheckmate || blackIsInCheckmate;
    }
    public boolean checkmate(ChessGame.TeamColor color) {
        return  (color == ChessGame.TeamColor.WHITE)? whiteIsInCheckmate : blackIsInCheckmate;
    }
    public void checkmate(ChessGame.TeamColor color, boolean checkmate) {
        if (color == ChessGame.TeamColor.WHITE) whiteIsInCheckmate = checkmate;
        else blackIsInCheckmate = checkmate;
    }

    public boolean stalemate() {
        return whiteIsInStalemate || blackIsInStalemate;
    }
    public boolean stalemate(ChessGame.TeamColor color) {
        return  (color == ChessGame.TeamColor.WHITE)? whiteIsInStalemate : blackIsInStalemate;
    }
    public void stalemate(ChessGame.TeamColor color, boolean stalemate) {
        if (color == ChessGame.TeamColor.WHITE) whiteIsInStalemate = stalemate;
        else blackIsInStalemate = stalemate;
    }

    public boolean kingMove() {
        return whiteKingHasMoved || blackKingHasMoved;
    }
    public boolean kingMove(ChessGame.TeamColor color) {
        return  (color == ChessGame.TeamColor.WHITE)? whiteKingHasMoved : blackKingHasMoved;
    }
    public void kingMove(ChessGame.TeamColor color, boolean kingMove) {
        if (color == ChessGame.TeamColor.WHITE) whiteKingHasMoved = kingMove;
        else blackKingHasMoved = kingMove;
    }

    public int movesSinceCapture() {
        return movesSinceCapture;
    }
    public void movesSinceCapture(int movesSinceCapture) {
        this.movesSinceCapture = movesSinceCapture;
    }
}