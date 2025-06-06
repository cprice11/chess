package dataaccess;

import datamodels.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlUser extends MySqlDataAccess implements UserDAO {
    public void addUser(UserData user) throws DataAccessException {
        checkConnection();
        if (user == null || user.username() == null || user.password() == null) {
            throw new DataAccessException("user is null");
        }
        if (stringIsUnsafe(user.username())) {
            return;
        }
        if (getUser(user.username()) != null) {
            throw new DataAccessException("username already in database");
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO user values (?, ?, ?)")) {
            statement.setString(1, user.username());
            statement.setString(2, user.password());
            statement.setString(3, user.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed while adding user");
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        checkConnection();
        if (username == null) {
            throw new DataAccessException("Username is null");
        }
        if (stringIsUnsafe(username)) {
            return null;
        }
        Collection<UserData> matches = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    matches.add(userFromResponse(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (matches.isEmpty()) {
            return null;
        }
        if (matches.size() > 1) {
            throw new RuntimeException("Uh Oh, We've got two identical users");
        }
        return new ArrayList<>(matches).getFirst();
    }

    public void clearAll() throws DataAccessException {
        checkConnection();
        String sql = "TRUNCATE user";
        try {
            conn.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
