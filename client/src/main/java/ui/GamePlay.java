package ui;

import chess.*;
import com.google.gson.Gson;
import serverfacade.ServerFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
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
                commandString("leave, l", "Removes the user from the game\n") +
                commandString("move, m", "Make a move in the game\n") +
                commandString("resign", "Forfeit the game\n") +
                commandString("highlight", "Show legal moves available to a piece\n") +
                commandString("quit, q", "Quit the program", color.errorText().toString());
        startMessage = color.secondaryText() +
                "Welcome to the C S 240 Chess client\n" +
                color.lightText() + "\tPlease enter a command";
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch (cmd) {
            case "s", "switch" -> switchView();
            case "r", "redraw" -> redraw();
            case "l", "leave" -> {
                if (leave()) {
                    return "postLogin";
                }
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
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> handleLoadGame(new Gson().fromJson(messageString, LoadGameMessage.class));
            case ERROR -> handleError(new Gson().fromJson(messageString, ErrorMessage.class).errorMessage);
            case NOTIFICATION ->
                    handleNotification(new Gson().fromJson(messageString, NotificationMessage.class).message);
        }
    }

    private void handleError(String message) {
        repl.printer.setMessageString(color.errorHighlight().lightText() + " " + message + " " + color.noSquare());
        redraw();
    }

    private void handleNotification(String message) {
        repl.printer.setMessageString(color.secondaryHighlight().darkSquare().lightText() + " " + message + " " + color.noSquare());
        redraw();
    }

    private void handleLoadGame(LoadGameMessage message) {
        repl.printer.setMessageString("");
        game = new ChessGame(message.getGame().game());
        repl.printer.setGame(game);
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
        ChessPosition start;
        ChessPosition end;
        ChessPiece.PieceType promotion = null;
        try {
            if (params.length == 2) {
                start = new ChessPosition(params[0]);
                end = new ChessPosition(params[1]);
            } else {
                start = new ChessPosition(getLine("Start Position"));
                end = new ChessPosition(getLine("End Position"));
                String promotionString = params.length > 2 ? params[2] : getLine("Promotion piece");
                promotion = switch (promotionString.toLowerCase()) {
                    case "q" -> ChessPiece.PieceType.QUEEN;
                    case "r" -> ChessPiece.PieceType.ROOK;
                    case "b" -> ChessPiece.PieceType.BISHOP;
                    case "n" -> ChessPiece.PieceType.KNIGHT;
                    default -> null;
                };
            }
        } catch (Exception e) {
            System.out.println("Couldn't parse position");
            return;
        }
        try {
            server.sendMakeMove(repl.getAuthToken(), repl.getCurrentGameID(), new ChessMove(start, end, promotion));
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private boolean leave() {
        try {
            server.sendLeave(repl.getAuthToken(), repl.getCurrentGameID());
            return true;
        } catch (IOException e) {
            error("Failed to leave: " + e.getMessage());
        }
        return false;
    }

    private void resign() {
        String resignString = getLine("Are you sure you want to resign (y/N)");
        if (resignString.equalsIgnoreCase("y")) {
            try {
                server.sendResign(repl.getAuthToken(), repl.getCurrentGameID());
            } catch (IOException e) {
                error("Failed to resign: " + e.getMessage());
            }
        }
    }

    private void highlight(String[] params) {
        String positionString;
        if (params.length == 1 && params[0].length() == 2) {
            positionString = params[0];
        } else {
            positionString = getLine("Start Position");
        }
        ChessPosition start = new ChessPosition(positionString);
        if (start.getFile() == 0 || start.getRank() > 8 || start.getRank() < 0) {
            warning("Cannot interpret position '" + positionString + "'");
        } else {
            game.validMoves(start);
            redraw();
        }
    }
}
