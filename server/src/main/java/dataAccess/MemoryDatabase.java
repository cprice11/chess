package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashSet;

public class MemoryDatabase {
    public static HashSet<AuthData> auth;
    public static HashSet<GameData> games;
    public static HashSet<UserData> users;

    public MemoryDatabase() {
        auth = new HashSet<>();
        games = new HashSet<>();
        users = new HashSet<>();
    }

    public static HashSet<AuthData> getAuth() {
        return auth;
    }

    public static void setAuth(HashSet<AuthData> auth) {
        MemoryDatabase.auth = new HashSet<>(auth);
    }

    public static HashSet<GameData> getGames() {
        return games;
    }

    public static void setGames(HashSet<GameData> games) {
        MemoryDatabase.games= new HashSet<>(games);
    }

    public static HashSet<UserData> getUsers() {
        return users;
    }

    public static void setUsers(HashSet<UserData> users) {
        MemoryDatabase.users = new HashSet<>(users);
    }


    public static void clearAll() {
        clearAuth();
        clearGames();
        clearUsers();
    }

    public static void clearAuth() {
        auth.clear();
    }

    public static void clearGames() {
        games.clear();
    }

    public static void clearUsers() {
        users.clear();
    }
}
