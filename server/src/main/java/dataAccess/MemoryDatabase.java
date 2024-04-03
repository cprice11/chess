package dataAccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryDatabase {
    public static Collection<GameData> games;
    public static Collection<UserData> users;
    public static Collection<UserData> auth;

    public MemoryDatabase() {
        games = new HashSet<>();
        users = new HashSet<>();
        auth = new HashSet<>();
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

    public static Collection<UserData> getAuth() {
        return auth;
    }

    public static void setAuth(Collection<UserData> auth) {
        MemoryDatabase.auth = auth;
    }
}
