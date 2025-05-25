package service;

import chess.ChessGame;
import dataModels.AuthData;
import dataModels.GameData;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;

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
        GameData newGame = new GameData(gameIndex, null, null, gameName, null);
        gameDAO.addGame(newGame);
        return gameIndex;
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws UnauthorizedException, DataAccessException, AlreadyTakenException, Exception{
        AuthData auth = authDAO.getAuthByAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedException();
        }
        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new Exception("Game doesn't exist");
        }
        if (playerColor == ChessGame.TeamColor.WHITE && game.whiteUsername() == null) {
            gameDAO.updateGame(gameID, new GameData(gameID, auth.username(), game.blackUsername(), game.gameName(), game.game()));
            return;
        }
        if (playerColor == ChessGame.TeamColor.BLACK && game.blackUsername() == null) {
            gameDAO.updateGame(gameID, new GameData(gameID, game.whiteUsername(), auth.username(), game.gameName(), game.game()));
            return;
        }
        throw new AlreadyTakenException();
    }
}
