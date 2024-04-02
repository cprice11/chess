package chess;

import java.util.Dictionary;
import java.util.Hashtable;

public class ChessMain {
    // public static ChessGame game = new ChessGame();
    public static void main (String[] args) {
        String fen = "8/8/8/8/8/8/8/K1q5 w KQkq - 0 0";
        Dictionary<ChessPosition, ChessPiece> pieces = new Hashtable<>();
        ChessPiece p = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece q = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        pieces.put(new ChessPosition(1, 3), p);
        pieces.put(new ChessPosition(2, 3), p);

        FENParser parser = new FENParser();
        GameState state = parser.parseFEN(fen);
        ChessGame game = new ChessGame(state);
        game.state.prettyPrint();
        System.out.println("WHITE IN CHECK? " + game.state().isInCheck(ChessGame.TeamColor.WHITE));
        System.out.println("BLACK IN CHECK? " + game.state().isInCheck(ChessGame.TeamColor.BLACK));
    }
}