package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import model.GameSummary;
import server.Authorization;
import server.request.CreateGameRequest;
import server.request.JoinGameRequest;
import server.request.ListGamesRequest;
import server.result.CreateGameResult;

import java.util.Collection;

public class GameService extends Service {
    private final GameDAO dao;
    private final AuthService authService;
    private final UserService userService;

    public GameService(GameDAO dao, AuthService auth, UserService users) {
        this.dao = dao;
        this.authService = auth;
        this.userService = users;
    }
    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException{
        authService.verify(request.authorization());
        return new CreateGameResult(dao.createGame(request.gameName()));
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException {
        authService.verify(request.authorization());

    }

    public Collection<GameSummary> getGames(ListGamesRequest request) {
        return dao.getGameSummaries();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return dao.getGame(gameID);
    }

    public Collection<GameData> getGamesByPlayer(String username) {
        return getGamesByPlayer(username);
    }

    public GameData getGamesByName(String gameName) {
        return getGamesByName(gameName);
    }
}
