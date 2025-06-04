package dataaccess;

import datamodels.AuthData;

public interface AuthDAO {
    void addAuth(AuthData auth) throws DataAccessException;

    AuthData getAuthByUsername(String username);

    AuthData getAuthByAuthToken(String authToken) throws DataAccessException;

    void deleteAuthByAuthToken(String authToken);

    void clearAll();
}
