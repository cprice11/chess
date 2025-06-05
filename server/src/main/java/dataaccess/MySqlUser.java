package dataaccess;

import datamodels.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MySqlUser extends MySqlDataAccess implements UserDAO {
    public void addUser(UserData user) throws DataAccessException {
        if (user == null || user.username() == null || user.password() == null) {
            throw new DataAccessException("user is null");
        }
        if (getUser(user.username()) != null) {
            throw new DataAccessException("username already in database");
        }
        String sql = String.format("INSERT INTO user values (\"%s\", \"%s\", \"%s\")", user.username(), user.password(), user.email());
        executeUpdate(sql);
    }

    public UserData getUser(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Username is null");
        }
        String sql = String.format("SELECT * FROM user WHERE username = '%s';", username);
        Collection<UserData> matches;
        matches = queryUserData(sql);
        if (matches.isEmpty()) {
            return null;
        }
        if (matches.size() > 1) {
            throw new RuntimeException("Uh Oh, We've got two identical users");
        }
        return new ArrayList<>(matches).getFirst();
    }

    public void clearAll() {
        String sql = "TRUNCATE user";
        try {
            executeUpdate(sql);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
