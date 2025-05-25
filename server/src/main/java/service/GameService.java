package service;

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
}
