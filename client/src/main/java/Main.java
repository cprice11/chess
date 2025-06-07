import chess.ChessGame;
import chess.ChessPiece;
import ui.ConsolePrinter;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ChessGame game = new ChessGame();
        ConsolePrinter printer = new ConsolePrinter(game);
        printer.fromBlack();
        printer.print();
        Repl repl = new Repl("localhost:3606");
        repl.run();
    }
}