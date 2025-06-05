package dataaccess;

import datamodels.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class MySqlAuth extends MySqlDataAccess implements AuthDAO {
    public void addAuth(AuthData auth) throws DataAccessException {
        if (auth == null || auth.authToken() == null) {
            throw new DataAccessException("auth is null");
        }
        if (getAuthByAuthToken(auth.authToken()) != null) {
            throw new DataAccessException("AuthToken already in database");
        }
        String sql = String.format("INSERT INTO auth values (\"%s\", \"%s\")", auth.authToken(), auth.username());
        executeUpdate(sql);
    }

    public AuthData getAuthByUsername(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Username is null");
        }
        String sql = String.format("SELECT * FROM auth WHERE username = \"%s\";", username);
        Collection<AuthData> matches;
        matches = queryAuthData(sql);
        if (matches.isEmpty()) {
            return null;
        }
        return new ArrayList<>(matches).getFirst(); // would we ever need more than one?
    }

    public AuthData getAuthByAuthToken(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("AuthToken is null");
        }
        String sql = String.format("SELECT * FROM auth WHERE authToken = '%s';", authToken);
        Collection<AuthData> matches;
        matches = queryAuthData(sql);
        if (matches.isEmpty()) {
            return null;
        }
        if (matches.size() > 1) {
            throw new RuntimeException("Uh Oh, We've got two identical authTokens");
        }
        return new ArrayList<>(matches).getFirst();
    }

    public void deleteAuthByAuthToken(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("AuthToken is null");
        }
        String sql = String.format("DELETE FROM auth WHERE authToken = \"%s\";", authToken);
        executeUpdate(sql);
    }

    public void clearAll() {
        String sql = "TRUNCATE auth";
        try {
            executeUpdate(sql);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
