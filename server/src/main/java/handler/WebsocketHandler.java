package handler;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class WebsocketHandler {
    public ServerMessage handle(UserGameCommand command) {
        System.out.println("received websocket command " + command.getCommandType());
        ChessGame testGame = new ChessGame();
        try {
            testGame.makeMove(new ChessMove(new ChessPosition("a2"), new ChessPosition("a3"), null));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        return new LoadGameMessage(testGame);
    }
}
