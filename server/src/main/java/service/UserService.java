package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import datamodels.AuthData;
import datamodels.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        AuthData auth = new AuthData(user.username(), generateToken());
        authDAO.addAuth(auth);
        userDAO.addUser(new UserData(user.username(), hashedPassword, user.email()));
        return auth;
    }

    public AuthData loginUser(String username, String password) throws UnauthorizedException, DataAccessException {
        UserData inDataBase = userDAO.getUser(username);
        if (inDataBase == null) {
            throw new UnauthorizedException();
        }
        if (!BCrypt.checkpw(password, inDataBase.password())) {
            throw new UnauthorizedException();
        }
        AuthData auth = new AuthData(username, generateToken());
        authDAO.addAuth(auth);
        return auth;
    }

    public void logoutUser(String authToken) throws UnauthorizedException, DataAccessException {
        AuthData auth = authDAO.getAuthByAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedException();
        }
        authDAO.deleteAuthByAuthToken(authToken);
    }
}
