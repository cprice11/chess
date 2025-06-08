import chess.ChessGame;
import chess.ChessPiece;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("Starting ♕ 240 Chess Client " + piece.prettyString() + "...");
        var serverUrl = "http://localhost:8765";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new Repl(serverUrl).run();
    }
}