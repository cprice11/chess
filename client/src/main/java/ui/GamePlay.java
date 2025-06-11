package ui;

import chess.ChessColor;
import chess.ChessGame;
import serverfacade.ServerFacade;

import java.util.Arrays;

public class GamePlay implements Client {
    private static final String HELP = "I'm the gameplay help message.";
    ChessGame game = new ChessGame();
    ChessGame.TeamColor teamColor;
    ChessColor color = new ChessColor();
    private final ServerFacade server;
    private final Repl repl;

    public GamePlay(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    public String eval(String input) {
        repl.printer.showLabels();
        repl.printer.print();
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        String returnVal = "";
        switch (cmd) {
            case "s", "switch":
                switchView();
            case "q", "quit":
                returnVal = "postLogin";
            case "hide":
                hide();
            default:
                System.out.println(help());
        }
        return returnVal;
    }

    public void switchView() {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            teamColor = ChessGame.TeamColor.BLACK;
            repl.printer.fromBlack();
        } else {
            teamColor = ChessGame.TeamColor.WHITE;
            repl.printer.fromWhite();
        }
        repl.printer.print();
    }

    private void hide() {
        repl.printer.hideLabels();
    }

    public String help() {
        color.secondaryText();
        return color + HELP + color.getResetString();
    }
}
