package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import datamodels.AuthData;
import datamodels.GameData;
import datamodels.GameSummary;

import java.util.Collection;

public class GameService extends Service{
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private int gameIndex = 100000;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public int createGame(String authToken, String gameName) throws UnauthorizedException, DataAccessException {
        AuthData auth = authDAO.getAuthByAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedException();
        }
        gameIndex += 1;
        GameData newGame = new GameData(gameIndex, null, null, gameName, new ChessGame());
        gameDAO.addGame(newGame);
        return gameIndex;
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID)
            throws UnauthorizedException, DataAccessException, AlreadyTakenException {
        AuthData auth = authDAO.getAuthByAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedException();
        }
        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new DataAccessException("Game doesn't exist");
        }
        if (playerColor == ChessGame.TeamColor.BLACK && game.blackUsername() == null) {
            gameDAO.updateGame(gameID, new GameData(gameID, auth.username(), game.whiteUsername(), game.gameName(), game.game()));
            return;
        }
        if (playerColor == ChessGame.TeamColor.WHITE && game.whiteUsername() == null) {
            gameDAO.updateGame(gameID, new GameData(gameID, game.blackUsername(), auth.username(), game.gameName(), game.game()));
            return;
        }
        throw new AlreadyTakenException();
    }

    public Collection<GameSummary> listGames(String authToken) throws UnauthorizedException, DataAccessException {
        AuthData auth = authDAO.getAuthByAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedException();
        }
        return gameDAO.getGameSummaries();
    }
}
