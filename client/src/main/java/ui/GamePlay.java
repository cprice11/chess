package ui;

import chess.ChessColor;
import chess.ChessGame;
import com.google.gson.Gson;
import serverfacade.ServerFacade;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.Arrays;

public class GamePlay extends Client {
    ChessGame game = new ChessGame();
    ChessGame.TeamColor teamColor;
    ChessColor color = new ChessColor();

    public GamePlay(ServerFacade server, Repl repl) {
        this.server = server;
        this.repl = repl;
        help = color.secondaryText() + "The following commands are available:\n" +
                commandString("help, h", "Prints this message\n") +
                commandString("switch, s", "Switch the orientation of the board\n") +
                commandString("redraw, r", "Redraw the chess board\n") +
                commandString("leave, l", "Removes the user from the game (whether they are playing or observing the game). The client transitions back to the Post-Login UI.\n") +
                commandString("move, m", "Allow the user to input what move they want to make. The board is updated to reflect the result of the move, and the board automatically updates on all clients involved in the game.\n") +
                commandString("resign", "Prompts the user to confirm they want to resign. If they do, the user forfeits the game and the game is over. Does not cause the user to leave the game.\n") +
                commandString("highlight", "Allows the user to input the piece for which they want to highlight legal moves. The selected piece’s current square and all squares it can legally move to are highlighted. This is a local operation and has no effect on remote users’ screens.\n") +
                commandString("quit, q", "Quit the program", color.errorText().toString());
        startMessage = color.secondaryText() +
                "Welcome to the C S 240 Chess client\n" +
                color.lightText() + "\tPlease enter a command";
    }

    public String eval(String input) {
        repl.printer.showLabels();
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch (cmd) {
            case "s", "switch" -> switchView();
            case "r", "redraw" -> redraw();
            case "l", "leave" -> {
                return "postLogin";
            }
            case "m", "move" -> makeMove(params);
            case "resign" -> resign();
            case "highlight" -> highlight(params);
            case "q", "quit" -> {
                return "quit";
            }
            default -> printHelp();
        }
        return "";
    }

    public void handleServerMessage(String messageString) {
        ServerMessage message = new Gson().fromJson(messageString, ServerMessage.class);
        System.out.println("Message received in GamePlay: " + message.getServerMessageType());
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> handleLoadGame(new Gson().fromJson(messageString, LoadGameMessage.class));
            case ERROR -> {
                error(message.toString());
            }
            case NOTIFICATION -> {
                warning(message.toString());
            }
        }
    }

    private void handleLoadGame(LoadGameMessage message) {
        repl.printer = new ConsolePrinter(new ChessGame(message.getGame().game()));
        redraw();
    }

    public void switchView() {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            teamColor = ChessGame.TeamColor.BLACK;
            repl.printer.fromBlack();
        } else {
            teamColor = ChessGame.TeamColor.WHITE;
            repl.printer.fromWhite();
        }
        redraw();
    }

    private void redraw() {
        repl.printer.print();
    }

    private void makeMove(String[] params) {
        throw new RuntimeException("Not implimented yet");
    }

    private void resign() {
        throw new RuntimeException("Not implimented yet");
    }

    private void highlight(String[] params) {
        throw new RuntimeException("Not implimented yet");
    }
}
