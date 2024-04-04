package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;

public class AuthService extends Service {
    private final AuthDAO dao;
    public AuthService(AuthDAO dao) {
        this.dao = dao;
    }
    public AuthData createAuth(String username) {
        return dao.createAuth(username);
    }

//    public LoginResult login(LoginRequest request) {
//        throw new RuntimeException("Not implemented yet");
//    }

//    public Result logout(LogoutRequest request) {
//        throw new RuntimeException("Not implemented yet");
//    }
    public void delete(AuthData target) {
        dao.delete(target);
    }

    public AuthData verify(String authToken) throws DataAccessException {
        return dao.verify(authToken);
    }

    public AuthData getAuthByUsername(String username) throws DataAccessException{
        return dao.verify(username);
    }

    public AuthData getAuthByAuthToken(String authToken) throws DataAccessException{
        return dao.verify(authToken);
    }
}
