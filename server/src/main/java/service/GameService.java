package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.GameSummary;
import server.request.CreateGameRequest;
import server.request.JoinGameRequest;
import server.request.ListGamesRequest;
import server.result.CreateGameResult;
import server.result.ListGamesResult;

import java.util.Collection;
import java.util.Random;

public class GameService extends Service {
    private final GameDAO dao;
    private final AuthService authService;

    public GameService(GameDAO dao, AuthService auth) {
        this.dao = dao;
        this.authService = auth;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {
        authService.verify(request.authorization());
        return new CreateGameResult(dao.createGame(request.gameName()));
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException, AlreadyTakenException {
        String newPlayer = authService.verify(request.authorization()).username();
        GameData game = getGame(request.gameID());
        String whitePlayer = game.whiteUsername();
        String blackPlayer = game.blackUsername();
        if (blackPlayer != null && whitePlayer != null) throw new AlreadyTakenException("Game is full");
        if (blackPlayer == null && whitePlayer == null) {
            if (request.playerColor() == null) {
                if (game.gameID() % 2 == 0) whitePlayer = newPlayer;
                else blackPlayer = newPlayer;
            }
            else if (request.playerColor().equals(ChessGame.TeamColor.WHITE)) whitePlayer = newPlayer;
            else blackPlayer = newPlayer;
        }
        else {
            if (whitePlayer == null) whitePlayer = newPlayer;
            else blackPlayer = newPlayer;
        }
        dao.update(game, new GameData(
                game.gameID(),
                whitePlayer,
                blackPlayer,
                game.gameName(),
                game.game())
        );
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        authService.verify(request.authentication());
        return new ListGamesResult(dao.getGameSummaries());
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return dao.getGame(gameID);
    }

    public Collection<GameSummary> getGamesByPlayer(String username) {
        return dao.getGamesByPlayer(username);
    }

    public Collection<GameSummary> getGamesByName(String gameName) {
        return dao.getGamesByName(gameName);
    }
}
