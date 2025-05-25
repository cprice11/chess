package dataaccess;

import dataModels.AuthData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class MemoryAuth implements AuthDAO{
    private final HashMap<String, AuthData> db = new HashMap<>();

    public MemoryAuth() {
    }

    public void addAuth(AuthData auth) throws DataAccessException {
        if (getAuthByAuthToken(auth.authToken()) != null) throw new DataAccessException("AuthToken already in database");
//        if (getAuthByUsername(auth.username()) != null) throw new DataAccessException("username already in database");
        db.put(auth.authToken(), auth);
        print();
    }

    public AuthData getAuthByUsername(String username) {
        for (Map.Entry<String, AuthData> entry : db.entrySet()) {
            if (Objects.equals(entry.getValue().username(), username)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public AuthData getAuthByAuthToken(String authToken) {
        return db.get(authToken);
    }

    public void updateAuth(AuthData auth, String authToken) throws DataAccessException {
        deleteAuth(auth);
        addAuth(new AuthData(auth.username(), authToken));
    }

    public void deleteAuth(AuthData authData) {
        db.remove(authData.authToken());
    }

    public void deleteAuthByAuthToken(String authToken) {
        db.remove(authToken);
    }

    public void deleteAuthByUsername(String username) {
        AuthData auth = getAuthByUsername(username);
        if (auth == null) return;
        deleteAuth(auth);
    }

    public void clearAll() {
        db.clear();
    }

    public void print() {
        for (Map.Entry<String, AuthData> entry : db.entrySet()) {
            System.out.println("username: " + entry.getValue().username() + " authToken: " + entry.getKey());
        }
    }
}
