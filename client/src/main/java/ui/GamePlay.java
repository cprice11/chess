package ui;

import chess.ChessColor;
import chess.ChessGame;
import serverfacade.ServerFacade;

import java.util.Arrays;

public class GamePlay extends Client {
    ChessGame game = new ChessGame();
    ChessGame.TeamColor teamColor;
    ChessColor color = new ChessColor();

    public GamePlay(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
        help = "I'm the gameplay help message.";
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
                printHelp();
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
}
