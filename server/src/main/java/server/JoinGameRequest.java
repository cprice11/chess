package server;

import chess.ChessGame;

public record JoinGameRequest(
        String authorization,
        ChessGame.TeamColor playerColor,
        int gameID
) {
}
