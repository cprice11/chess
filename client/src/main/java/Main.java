import chess.ChessColor;
import chess.ChessGame;
import chess.ChessPiece;
import ui.ConsolePrinter;
import ui.PieceCharacters;
import ui.Repl;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessGame game = new ChessGame();
        ConsolePrinter printer = new ConsolePrinter(game);
        ChessColor color = new ChessColor();
        ChessColor.ColorPalette palette = new ChessColor.ColorPalette(Color.decode("#FFFFFF"), Color.decode("#000000"),
                Color.decode("#302E2B"), Color.decode("#F9F9F9"), Color.decode("#343534"),
                Color.decode("#d8daa4"), Color.decode("#739552"), Color.decode("#F5F580"),
                Color.decode("#B9CA42"), Color.decode("#82CBBA"), Color.decode("#46A07C"),
                Color.decode("#4EB7B4"), Color.decode("#302D29"), Color.decode("#F73E2C"),
                Color.decode("#F73E2C"));
        printer.setTheme(palette);
        printer.setPieceCharacters(PieceCharacters.LETTER_SYMBOLS);
        System.out.println("Starting ♕ 240 Chess Client " + piece.prettyString() + "...");
        var serverUrl = "http://localhost:8765";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new Repl(serverUrl).run();
    }
}