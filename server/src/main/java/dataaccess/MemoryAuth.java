package dataaccess;

import dataModels.AuthData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class MemoryAuth implements AuthDAO{
    private final HashMap<String, String> db = new HashMap<>();
    private final HashSet<String> users = new HashSet<>();

    public MemoryAuth() {
    }

    public void addAuth(AuthData auth) throws DataAccessException {
        if (getAuthByAuthToken(auth.authToken()) != null) throw new DataAccessException("AuthToken already in database");
        if (getAuthByUsername(auth.username()) != null) throw new DataAccessException("username already in database");
        db.put(auth.authToken(), auth.username());
        users.add(auth.username());
    }

    public AuthData getAuthByUsername(String username) {
        if (!users.contains(username)) return null;
        for (Map.Entry<String, String> auth : db.entrySet()) {
            if (Objects.equals(auth.getValue(), username)) {
                return new AuthData(auth.getValue(), auth.getKey());
            }
        }
        return null;
    }

    public AuthData getAuthByAuthToken(String authToken) {
        String username = db.get(authToken);
        if (username == null) return null;
        return new AuthData(username, authToken);
    }

    public void updateAuth(String username, String authToken) throws DataAccessException {
        deleteAuthByUsername(username);
        addAuth(new AuthData(username, authToken));
    }

    public void deleteAuth(AuthData authData) {
        db.remove(authData.authToken());
        users.remove(authData.username());
    }

    public void deleteAuthByAuthToken(String authToken) {
        AuthData auth = getAuthByAuthToken(authToken);
        if (auth == null) return;
        deleteAuth(auth);
    }

    public void deleteAuthByUsername(String username) {
        AuthData auth = getAuthByUsername(username);
        if (auth == null) return;
        deleteAuth(auth);
    }

    public void clearAll() {
        db.clear();
        users.clear();
    }

    public void print() {
        for (Map.Entry<String, String> entry : db.entrySet()) {
            System.out.println("username: " + entry.getValue() + " authToken: " + entry.getKey());
        }
    }
}
