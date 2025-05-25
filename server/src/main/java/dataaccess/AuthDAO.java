package dataaccess;

import datamodels.AuthData;

public interface AuthDAO {
    void addAuth(AuthData auth) throws DataAccessException;
    AuthData getAuthByUsername(String username);
    AuthData getAuthByAuthToken(String authToken);
//    void updateAuth(AuthData auth, String authToken) throws DataAccessException;
//    void deleteAuth(AuthData authData);
    void deleteAuthByAuthToken(String authToken);
//    void deleteAuthByUsername(String username);
    void clearAll();
}
