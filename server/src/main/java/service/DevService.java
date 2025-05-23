package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class DevService extends Service{
    AuthDAO auth;
    UserDAO user;
    GameDAO game;
    public DevService(AuthDAO auth, GameDAO game, UserDAO user) {
        this.auth = auth;
        this.game = game;
        this.user = user;
    }
    public void clear() {
        this.auth.clearAll();
        this.game.clearAll();
        this.user.clearAll();
        System.out.println("Clearing");
    }
}
