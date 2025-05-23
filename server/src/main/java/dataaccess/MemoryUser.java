package dataaccess;

import dataModels.AuthData;
import dataModels.UserData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryUser implements UserDAO{
    HashSet<UserData> db = new HashSet<>();
    public void addUser(UserData user) {
        db.add(user);
    }

    public UserData getUser(String username) {
        for (UserData user : db) {
            if (Objects.equals(user.username(), username)) {
                return user;
            }
        }
        return null;
    }

    public void updateUser(String username, UserData updatedUser) {
        deleteUser(username);
        addUser(updatedUser);
    }

    public void deleteUser(String username) {
        db.removeIf(user -> user.username().equals(username));
    }

    public void clearAll() {
        db.clear();
    }
}
