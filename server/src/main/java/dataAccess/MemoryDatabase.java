package dataAccess;

import model.GameData;
import model.UserData;

import java.util.Collection;

public class MemoryDatabase {
    public static Collection<GameData> games;
    public static Collection<UserData> users;
    public static Collection<UserData> auth;
}
