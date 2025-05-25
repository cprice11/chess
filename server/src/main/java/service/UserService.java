package service;

import dataModels.AuthData;
import dataModels.UserData;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

import java.util.Objects;

public class UserService extends Service{
    AuthDAO authDAO;
    UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthData registerUser(UserData user) throws AlreadyTakenException, DataAccessException{
        UserData inDataBase = userDAO.getUser(user.username());
        if (inDataBase != null) {
            throw new AlreadyTakenException();
        }

        AuthData auth = new AuthData(user.username(), generateToken());
        authDAO.addAuth(auth);
        userDAO.addUser(user);
        return auth;
    }

    public AuthData loginUser(String username, String password) throws UnauthorizedException, DataAccessException {
        UserData inDataBase = userDAO.getUser(username);
        if (inDataBase == null) {
            throw new UnauthorizedException();
        }
        if (!Objects.equals(inDataBase.password(), password)) {
            throw new UnauthorizedException();
        }
        AuthData auth = authDAO.getAuthByUsername(username);
        if (auth == null) {
            auth = new AuthData(username, generateToken());
            authDAO.addAuth(auth);
        }
        return auth;
    }

    public void logoutUser(String authToken) throws UnauthorizedException {
        AuthData auth = authDAO.getAuthByAuthToken(authToken);
        if (auth == null) throw new UnauthorizedException();
        authDAO.deleteAuthByAuthToken(authToken);
    }
}
