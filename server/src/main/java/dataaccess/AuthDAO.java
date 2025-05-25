package dataaccess;

import datamodels.AuthData;

public interface AuthDAO {
    void addAuth(AuthData auth) throws DataAccessException;
    AuthData getAuthByUsername(String username);
    AuthData getAuthByAuthToken(String authToken);
    void deleteAuthByAuthToken(String authToken);
    void clearAll();
}
