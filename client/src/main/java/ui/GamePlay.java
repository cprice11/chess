package ui;

import chess.ChessColor;
import chess.ChessGame;
import server.ServerFacade;

import java.util.Arrays;

public class GamePlay implements Client {
    private static final String help = "I'm the gameplay help message.";
    ChessGame game = new ChessGame();
    ChessGame.TeamColor teamColor;
    ConsolePrinter printer = new ConsolePrinter(game);
    ChessColor color = new ChessColor();
    private final ServerFacade server;
    private final Repl repl;

    public GamePlay(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    public String eval(String input) {
        printer.showLabels();
        printer.print();
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        String returnVal = "";
        switch (cmd) {
            case "s", "switch":
                switchView();
            case "q", "quit":
                returnVal = "l";
            default:
                System.out.println(help());
        }
        return returnVal;
    }

    public void switchView() {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            teamColor = ChessGame.TeamColor.BLACK;
            printer.fromBlack();
        } else {
            teamColor = ChessGame.TeamColor.WHITE;
            printer.fromWhite();
        }
        printer.print();
    }

    public String help() {
        color.secondaryText();
        return color + help + color.getResetString();
    }
}
