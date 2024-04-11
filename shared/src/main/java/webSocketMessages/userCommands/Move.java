package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class Move extends UserGameCommand {
    int gameID;
    ChessMove move;

    public Move(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }
}
