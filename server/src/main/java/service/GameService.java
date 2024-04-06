package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import model.GameSummary;
import server.request.CreateGameRequest;
import server.request.InvalidRequestException;
import server.request.JoinGameRequest;
import server.request.ListGamesRequest;
import server.result.CreateGameResult;
import server.result.ListGamesResult;

import java.util.Collection;

public class GameService {
    private final GameDAO dao;
    private final AuthService authService;

    public GameService(GameDAO dao, AuthService auth) {
        this.dao = dao;
        this.authService = auth;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws UnauthorizedException {
        authService.verify(request.authorization());
        return new CreateGameResult(dao.createGame(request.gameName()));
    }

    public void joinGame(JoinGameRequest request) throws InvalidRequestException, AlreadyTakenException, UnauthorizedException {
        String newPlayer = authService.verify(request.authorization()).username();
        try {
            GameData game = getGame(request.gameID());
            if (request.playerColor() == null) return;
            String whitePlayer = game.whiteUsername();
            String blackPlayer = game.blackUsername();
            if (blackPlayer != null && whitePlayer != null) throw new AlreadyTakenException("Game is full");
            if (request.playerColor().equals(ChessGame.TeamColor.WHITE) && whitePlayer == null) whitePlayer = newPlayer;
            else if (request.playerColor().equals(ChessGame.TeamColor.BLACK) && blackPlayer == null)
                blackPlayer = newPlayer;
            else throw new AlreadyTakenException("Color is already taken");
            dao.update(game, new GameData(
                    game.gameID(),
                    whitePlayer,
                    blackPlayer,
                    game.gameName(),
                    game.game())
            );
        } catch (DataAccessException e) {
            throw new InvalidRequestException("No game found with specified ID");
        }
    }

    public ListGamesResult listGames(ListGamesRequest request) throws UnauthorizedException {
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
