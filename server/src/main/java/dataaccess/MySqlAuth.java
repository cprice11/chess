package dataaccess;

import datamodels.AuthData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlAuth extends MySqlDataAccess implements AuthDAO {
    public void addAuth(AuthData auth) throws DataAccessException {
        if (auth == null || auth.authToken() == null) {
            throw new DataAccessException("auth is null");
        }
        if (stringIsUnsafe(auth.authToken()) || stringIsUnsafe(auth.username())) {
            return;
        }
        if (getAuthByAuthToken(auth.authToken()) != null) {
            throw new DataAccessException("AuthToken already in database");
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO auth values (?, ?)")) {
            statement.setString(1, auth.authToken());
            statement.setString(2, auth.username());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed while adding auth");
        }
    }

    public AuthData getAuthByUsername(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Username is null");
        }
        if (stringIsUnsafe(username)) {
            return null;
        }
        Collection<AuthData> matches = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM auth WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    matches.add(authFromResponse(result));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (matches.isEmpty()) {
            return null;
        }
        return new ArrayList<>(matches).getFirst(); // would we ever need more than one?
    }

    public AuthData getAuthByAuthToken(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("AuthToken is null");
        }
        if (stringIsUnsafe(authToken)) {
            return null;
        }
        Collection<AuthData> matches = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM auth WHERE authToken = ?")) {
            statement.setString(1, authToken);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    matches.add(authFromResponse(result));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (matches.isEmpty()) {
            return null;
        }
        return new ArrayList<>(matches).getFirst();
    }

    public void deleteAuthByAuthToken(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("AuthToken is null");
        }
        try (PreparedStatement statement = conn.prepareStatement("DELETE FROM auth WHERE authToken = ?")) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clearAll() throws DataAccessException {
        String sql = "TRUNCATE auth";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
