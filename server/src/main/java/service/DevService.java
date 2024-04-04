package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class DevService extends Service {
    private final AuthDAO auth;
    private final GameDAO games;
    private final UserDAO users;

    public DevService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        auth = authDAO;
        games = gameDAO;
        users = userDAO;
    }

    public void clearDatabase() {
        auth.deleteAll();
        games.deleteAll();
        users.deleteAll();
    }
}
