package service;

import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;

public class DevService {
    private final AuthDao auth;
    private final GameDao games;
    private final UserDao users;

    public DevService(AuthDao authDAO, GameDao gameDAO, UserDao userDAO) {
        auth = authDAO;
        games = gameDAO;
        users = userDAO;
    }

    public void clearDatabase() throws DataAccessException {
        auth.deleteAll();
        games.deleteAll();
        users.deleteAll();
    }
}
