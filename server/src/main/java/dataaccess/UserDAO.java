package dataaccess;

import dataModels.UserData;

public interface UserDAO {
    void addUser(UserData user) throws DataAccessException;
    UserData getUser(String username);
    void updateUser(String username, UserData updatedUser) throws DataAccessException;
    void deleteUser(String username);
    void clearAll();
}
