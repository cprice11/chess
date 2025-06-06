package dataaccess;

import datamodels.UserData;

import java.util.HashMap;

public class MemoryUser implements UserDAO{
    HashMap<String, UserData> db = new HashMap<>();
    public void addUser(UserData user) throws DataAccessException{
        if (null == user || null == user.username() || null == user.password()) {
            throw new DataAccessException("user is malformed");
        }
        if (db.get(user.username()) != null) {
            throw new DataAccessException("User '" + "' already exists in database.");
        }
        db.put(user.username(), user);
    }

    public UserData getUser(String username) throws DataAccessException {
        if (null == username) {
            throw new DataAccessException("username is null");
        }
        return db.get(username);
    }

    public void clearAll() {
        db.clear();
    }
}
