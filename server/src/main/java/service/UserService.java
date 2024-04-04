package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;
import server.request.LoginRequest;
import server.request.LogoutRequest;
import server.request.RegisterRequest;
import server.result.LoginResult;
import server.result.RegisterResult;

public class UserService extends Service {
    private final UserDAO dao;

    public UserService(UserDAO dao) {
        this.dao = dao;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        dao.getUser(request.username())
    }

    public LoginResult login(LoginRequest request) {
        throw new RuntimeException("Not implemented yet");
    }

    public void logout(LogoutRequest request) {
        throw new RuntimeException("Not implemented yet");
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
