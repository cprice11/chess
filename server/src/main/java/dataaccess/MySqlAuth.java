package dataaccess;

import datamodels.AuthData;

public class MySqlAuth extends MySqlDataAccess implements AuthDAO {
    @Override
    public void addAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public AuthData getAuthByUsername(String username) {
        return null;
    }

    @Override
    public AuthData getAuthByAuthToken(String authToken) {
        return null;
    }

    @Override
    public void deleteAuthByAuthToken(String authToken) {

    }

    @Override
    public void clearAll() {

    }
}
