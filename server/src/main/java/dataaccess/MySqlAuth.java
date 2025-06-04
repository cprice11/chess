package dataaccess;

import datamodels.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlAuth extends MySqlDataAccess implements AuthDAO {
    public void addAuth(AuthData auth) throws DataAccessException {
        if (getAuthByAuthToken(auth.authToken()) != null) {
            throw new DataAccessException("AuthToken already in database");
        }
        String sql = String.format("INSERT INTO auth values(%s, %s)", auth.authToken(), auth.username());
        executeUpdate(sql);
    }

    @Override
    public AuthData getAuthByUsername(String username) {
        return null;
    }

    public AuthData getAuthByAuthToken(String authToken) throws DataAccessException {
        String sql = String.format("SELECT * FROM auth WHERE authToken = %s", authToken);
        ArrayList<AuthData> matches = new ArrayList<>();
        try {
            ResultSet result = executeUpdate(sql);
            while (result.next()) {
                matches.add(authFromResponse(result));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new DataAccessException(String.format("The following statement threw an exception when trying to access the database:\n\t%s\n\t%s", sql, e.getMessage()));
        }
        if (matches.isEmpty()) {
            return null;
        }
        if (matches.size() > 1) {
            throw new RuntimeException("Uh Oh, We've got two identical authTokens");
        }
        return matches.getFirst();
    }

    @Override
    public void deleteAuthByAuthToken(String authToken) {

    }

    public void clearAll() {
        String sql = "TRUNCATE auth";
        try {
            executeUpdate(sql);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthData authFromResponse(ResultSet rs) throws SQLException {
        String authToken = rs.getString("authToken");
        String username = rs.getString("username");
        return new AuthData(authToken, username);
    }
}
