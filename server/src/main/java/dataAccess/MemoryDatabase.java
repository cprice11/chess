package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryDatabase {
    static final Collection<AuthData> blankAuth = List.of(new AuthData[]{});
    static final Collection<GameData> blankGames = List.of(new GameData[]{});
    static final Collection<UserData> blankUsers = List.of(new UserData[]{});
    public static Collection<AuthData> auth = new ArrayList<>();
    public static Collection<GameData> games = new ArrayList<>();
    public static Collection<UserData> users = new ArrayList<>();

    public MemoryDatabase() {
    }

    public static Collection<AuthData> getAuth() {
        return auth;
    }

    public static void setAuth(Collection<AuthData> auth) {
        MemoryDatabase.auth = auth;
    }

    public static Collection<GameData> getGames() {
        return games;
    }

    public static void setGames(Collection<GameData> games) {
        MemoryDatabase.games = games;
    }

    public static Collection<UserData> getUsers() {
        return users;
    }

    public static void setUsers(Collection<UserData> users) {
        MemoryDatabase.users = users;
    }


    public static void clearAll() {
        clearAuth();
        clearGames();
        clearUsers();
    }

    public static void clearAuth() {
        setAuth(blankAuth);
    }

    public static void clearGames() {
        setGames(blankGames);
    }

    public static void clearUsers() {
        setUsers(blankUsers);
    }
}
