package service;

import model.GameData;
import model.GameSummary;
import server.Authorization;
import server.CreateGameRequest;
import server.CreateGameResult;
import server.JoinGameRequest;

import java.util.Collection;

public class GameService extends Service {
    public void joinGame(JoinGameRequest request) {
        throw new RuntimeException("Not implemented yet");
    }

    public CreateGameResult createGame(CreateGameRequest request) {
        throw new RuntimeException("Not implemented yet");
    }

    public Collection<GameSummary> getGames(Authorization authorization) {
        throw new RuntimeException("Not implemented yet");
        // verify()
        // getAllGameSummaries()
    }

    public GameData getGame(String gameID) {
        throw new RuntimeException("Not implemented yet");
    }

    public Collection<GameData> getGamesByPlayer(String username) {
        throw new RuntimeException("Not implemented yet");
    }

    public GameData getGamesByName(String gameName) {
        throw new RuntimeException("Not implemented yet");
    }
}
