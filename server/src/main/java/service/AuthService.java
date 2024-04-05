package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;

import java.util.Collection;

public class AuthService {
    private final AuthDAO dao;

    public AuthService(AuthDAO dao) {
        this.dao = dao;
    }

    public AuthData createAuth(String username) {
        return dao.createAuth(username);
    }

    public void delete(AuthData target) {
        dao.delete(target);
    }

    public AuthData verify(String authToken) throws UnauthorizedException {
        try {
            return dao.verify(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Auth token not verified");
        }
    }

    public Collection<AuthData> getAuthByUsername(String username) throws DataAccessException {
        return dao.getAuthFromUser(username);
    }

    public AuthData getAuthByAuthToken(String authToken) throws DataAccessException {
        return dao.verify(authToken);
    }
}
