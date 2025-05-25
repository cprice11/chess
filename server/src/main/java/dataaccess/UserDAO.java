package dataaccess;

import datamodels.UserData;

public interface UserDAO {
    void addUser(UserData user) throws DataAccessException;
    UserData getUser(String username);
    void clearAll();
}
