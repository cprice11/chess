package dataaccess;

import dataModels.UserData;

public interface UserDAO {
    void addUser(UserData user);
    UserData getUser(String username);
    void updateUser(String username, UserData updatedUser);
    void deleteUser(String username);
    void clearAll();
}
