package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GameState {
    private RuleSet rules;

    private ChessBoard board;

    private ChessGame.TeamColor turn;

    private int movesSinceCapture = 0;
    private boolean whiteIsInCheck;
    private boolean blackIsInCheck;
    private boolean whiteIsInCheckmate;
    private boolean blackIsInCheckmate;
    private boolean whiteIsInStalemate;
    private boolean blackIsInStalemate;
    private boolean whiteKingHasMoved;
    private boolean blackKingHasMoved;

    private boolean whiteCanCastleShort;
    private boolean whiteCanCastleLong;
    private boolean blackCanCastleShort;
    private boolean blackCanCastleLong;


    private ChessPosition enPassant;

    private int halfmoveClock;
    private int fullmoveClock;

    private Vector<ChessMove> history;
    private HashMap<ChessBoard, Integer> positionHashMap;
    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public void setHalfmoveClock(int halfmoveClock) {
        this.halfmoveClock = halfmoveClock;
    }

    public int getFullmoveClock() {
        return fullmoveClock;
    }

    public void setFullmoveClock(int fullmoveClock) {
        this.fullmoveClock = fullmoveClock;
    }

    public void incrementFullmoveClock() {
        this.fullmoveClock += 1;
    }

    public void incrementHalfmoveClock() {
        this.halfmoveClock += 1;
    }


    public boolean whiteCanCastleShort() {
        return whiteCanCastleShort;
    }
    public void whiteCanCastleShort(boolean whiteCanCastleShort) {
        this.whiteCanCastleShort = whiteCanCastleShort;
    }
    public boolean whiteCanCastleLong() {
        return whiteCanCastleLong;
    }
    public void whiteCanCastleLong(boolean whiteCanCastleLong) {
        this.whiteCanCastleLong = whiteCanCastleLong;
    }
    public boolean blackCanCastleShort() {
        return blackCanCastleShort;
    }
    public void blackCanCastleShort(boolean blackCanCastleShort) {
        this.blackCanCastleShort = blackCanCastleShort;
    }
    public boolean blackCanCastleLong() {
        return blackCanCastleLong;
    }
    public void blackCanCastleLong(boolean blackCanCastleLong) {
        this.blackCanCastleLong = blackCanCastleLong;
    }



    public ChessPosition getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(ChessPosition enPassant) {
        this.enPassant = enPassant;
    }






    public GameState() {
        turn = ChessGame.TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
        history = new Vector<ChessMove>();
        positionHashMap = new HashMap<ChessBoard, Integer>();
        rules = new StandardRules();
        whiteIsInCheck = blackIsInCheck = whiteIsInCheckmate = blackIsInCheckmate = whiteIsInStalemate =
                blackIsInStalemate = whiteKingHasMoved = blackKingHasMoved = false;
    }

    public GameState(ChessBoard board, ChessGame.TeamColor turn) {
        this.board = board;
        board.resetBoard();
    }

    public Collection<ChessMove> getValidMoves(ChessPosition position) {
        throw new RuntimeException("not implimented error");
    }


    public void makeMove(ChessMove move) {
        addHistory(move);
        ChessPiece piece = removePiece(move.startPosition);
        piece = (move.promotionPiece == null)? piece: new ChessPiece(piece.getTeamColor(), move.promotionPiece);
        board.addPiece(move.endPosition, piece);
    }

    public void makeMove(String move) {
        throw new RuntimeException("NOT IMPLEMENTED");
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



    public String toString() {
        FENParser fenParser = new FENParser();
        return fenParser.getFen(this);
    }

    public String prettyToString() {
        String turn = (turn() == ChessGame.TeamColor.WHITE)? "White to move": "Black to Movee";
        String out = board.prettyToString() +
                    "   " + turn +
                    "\nFEN: " + toString();
        return out;
    }

    public void prettyPrint() {
        System.out.println(prettyToString());
    }
}