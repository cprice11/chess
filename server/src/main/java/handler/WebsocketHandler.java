package handler;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class WebsocketHandler {
    private static final Gson GSON = new Gson();

    public String handle(String command) {
        System.out.println("received websocket command " + command);
        UserGameCommand userGameCommand = GSON.fromJson(command, UserGameCommand.class);
        ChessGame testGame = new ChessGame();
        try {
            testGame.makeMove(new ChessMove(new ChessPosition("a2"), new ChessPosition("a3"), null));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        ServerMessage returnMessage = new LoadGameMessage(testGame);
        return GSON.toJson(returnMessage);
    }
}
