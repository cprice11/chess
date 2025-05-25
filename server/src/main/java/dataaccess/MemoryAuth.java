package dataaccess;

import dataModels.AuthData;
import dataModels.UserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class MemoryAuth implements AuthDAO{
    private final HashSet<AuthData> db = new HashSet<>();

    public MemoryAuth() {
    }

    public void addAuth(AuthData auth) {
        db.add(auth);
        print();
    }

    @Override
    public AuthData getAuth(String username) {
        for (AuthData auth : db) {
            if (Objects.equals(auth.username(), username)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public void updateAuth(String username, String authToken) {

    }

    public void deleteAuth(String username) {
        db.removeIf(auth -> Objects.equals(auth.username(), username));
    }

    public void clearAll() {
        db.clear();
    }

    public void print() {
        for (AuthData a : db) {
            System.out.println("username: " + a.username() + " authToken: " + a.authToken());
        }
    }
}
