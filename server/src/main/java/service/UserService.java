package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import server.request.LoginRequest;
import server.request.LogoutRequest;
import server.request.RegisterRequest;
import server.result.LoginResult;
import server.result.RegisterResult;

public class UserService extends Service {
    private final UserDAO dao;
    private final AuthService auth;

    public UserService(UserDAO dao, AuthService auth) {
        this.dao = dao;
        this.auth = auth;
    }

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException {
        String username = request.username();
        try {
            dao.getUser(username);
            throw new AlreadyTakenException("Username already in use; registration failed.");
        } catch (DataAccessException e) {
            dao.add(new UserData(username, request.password(), request.email()));
            return new RegisterResult(auth.createAuth(username).authToken(), username);
        }
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        String username = request.username();
        UserData existingUser = dao.getUser(username);
        if (existingUser.password().equals(request.password())) {
            return new LoginResult(username, auth.createAuth(username).authToken());
        }
        throw new DataAccessException("Credentials do not match; login failed.");
    }

    public void logout(LogoutRequest request) throws DataAccessException {
        AuthData authData = auth.verify(request.authorization());
        auth.delete(authData);
//        Collection<AuthData> allSessions = auth.getAuthByUsername(authData.username());
//        for (AuthData session : allSessions) {
//            auth.delete(session);
//        }
    }

    // FIXME: I assume that these will be useful later.
//    public UserData getUserByUsername(String username) {
//        throw new RuntimeException("Not implemented yet");
//    }
//
//    public UserData getUserByEmail(String username) {
//        throw new RuntimeException("Not implemented yet");
//    }
}
