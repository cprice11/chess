package webSocketMessages.userCommands;

import chess.ChessGame;

public class Resign extends UserGameCommand {
    int gameID;

    public Resign(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }
}
