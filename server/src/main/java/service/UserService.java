package service;

import dataAccess.DataAccessException;
import dataAccess.UserDao;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import server.request.LoginRequest;
import server.request.LogoutRequest;
import server.request.RegisterRequest;
import server.result.LoginResult;
import server.result.RegisterResult;

public class UserService {
    private final UserDao dao;
    private final AuthService auth;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserDao dao, AuthService auth) {
        this.dao = dao;
        this.auth = auth;
    }

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException {
        String username = request.username();
        try {
            dao.getUser(username);
            throw new AlreadyTakenException("Username already in use; registration failed.");
        } catch (DataAccessException e) {
            dao.add(new UserData(username, encoder.encode(request.password()), request.email()));
            return new RegisterResult(auth.createAuth(username).authToken(), username);
        }
    }

    public LoginResult login(LoginRequest request) throws UnauthorizedException {
        try {
            String username = request.username();
            UserData existingUser = dao.getUser(username);
            if (encoder.matches(request.password(), existingUser.password())) {
                return new LoginResult(username, auth.createAuth(username).authToken());
            }
            throw new UnauthorizedException("Credentials do not match; login failed.");
        } catch (DataAccessException e) {
            throw new UnauthorizedException("User with username does not exist");
        }
    }

    public void logout(LogoutRequest request) throws UnauthorizedException {
        AuthData authData = auth.verify(request.authorization());
        auth.delete(authData);
    }
}
