package dataaccess;

import datamodels.AuthData;

public interface AuthDAO {
    void addAuth(AuthData auth) throws DataAccessException;

    // This may be better off being removed.
    // I don't think it's ever being used outside of tests
    // Also, some tests implied that one user can have multiple auths at once
    AuthData getAuthByUsername(String username) throws DataAccessException;

    AuthData getAuthByAuthToken(String authToken) throws DataAccessException;

    void deleteAuthByAuthToken(String authToken) throws DataAccessException;

    void clearAll() throws DataAccessException;
}
