package service;

import dataModels.AuthData;
import dataModels.UserData;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

public class UserService extends Service{
    AuthDAO authDAO;
    UserDAO userDAO;
    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public AuthData registerUser(UserData user) throws DataAccessException {
        AuthData auth = new AuthData(user.username(), generateToken());
        authDAO.addAuth(auth);
        userDAO.addUser(user);
        return auth;
    }
    public AuthData loginUser(String username, String password) throws DataAccessException {
        UserData inDataBase = userDAO.getUser(username);
        if (inDataBase == null) {
            return null;
        }
        if (inDataBase.password().equals(password)) {
            return null;
        }
        AuthData auth = new AuthData(username, generateToken());
        authDAO.addAuth(auth);
        return auth;
    }
}
