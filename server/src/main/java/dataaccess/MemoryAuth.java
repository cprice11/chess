package dataaccess;

import datamodels.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryAuth implements AuthDAO {
    private final HashMap<String, AuthData> db = new HashMap<>();

    public MemoryAuth() {
    }

    public void addAuth(AuthData auth) throws DataAccessException {
        if (null == auth || null == auth.username() || null == auth.authToken()) {
            throw new DataAccessException("authData is malformed");
        }
        if (getAuthByAuthToken(auth.authToken()) != null) {
            throw new DataAccessException("AuthToken already in database");
        }
        db.put(auth.authToken(), auth);
    }

    public AuthData getAuthByUsername(String username) throws DataAccessException {
        if (null == username) {
            throw new DataAccessException("user is null");
        }
        for (Map.Entry<String, AuthData> entry : db.entrySet()) {
            if (Objects.equals(entry.getValue().username(), username)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public AuthData getAuthByAuthToken(String authToken) throws DataAccessException {
        if (null == authToken) {
            throw new DataAccessException("authToken is null");
        }
        return db.get(authToken);
    }

    public void deleteAuthByAuthToken(String authToken) throws DataAccessException {
        if (null == authToken) {
            throw new DataAccessException("authToken is null");
        }
        db.remove(authToken);
    }

    public void clearAll() {
        db.clear();
    }
}
