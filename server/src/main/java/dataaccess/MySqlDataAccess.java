package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import datamodels.AuthData;
import datamodels.GameData;
import datamodels.GameSummary;
import datamodels.UserData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlDataAccess {
    // I don't like that we're rolling and unrolling JSON on the
    // server side but, at least it should work
    protected static final Gson GSON = new Gson();
    protected static Connection conn;

    public MySqlDataAccess() {
        try {
            DatabaseManager.createDatabase();
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected static void executeUpdate(String sqlStatement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlStatement, RETURN_GENERATED_KEYS)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("The following statement threw an exception when trying to access the database:\n\t%s\n\t%s", sqlStatement, e.getMessage()));
        }
    }


    protected static Collection<AuthData> queryAuthData(String sqlStatement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlStatement, RETURN_GENERATED_KEYS)) {
                ResultSet result = preparedStatement.executeQuery();
                Collection<AuthData> matches = new ArrayList<>();
                while (result.next()) {
                    matches.add(authFromResponse(result));
                }
                return matches;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("The following statement threw an exception when trying to access the database:\n\t%s\n\t%s", sqlStatement, e.getMessage()));
        }
    }

    protected static Collection<GameData> queryGameData(String sqlStatement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlStatement, RETURN_GENERATED_KEYS)) {
                ResultSet result = preparedStatement.executeQuery();
                Collection<GameData> matches = new ArrayList<>();
                while (result.next()) {
                    matches.add(gameFromResponse(result));
                }
                return matches;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("The following statement threw an exception when trying to access the database:\n\t%s\n\t%s", sqlStatement, e.getMessage()));
        }
    }

    protected static Collection<GameSummary> queryGameSummary(String sqlStatement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlStatement, RETURN_GENERATED_KEYS)) {
                preparedStatement.executeQuery();
                ResultSet result = preparedStatement.getGeneratedKeys();
                Collection<GameSummary> matches = new ArrayList<>();
                while (result.next()) {
                    matches.add(summaryFromResponse(result));
                }
                return matches;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("The following statement threw an exception when trying to access the database:\n\t%s\n\t%s", sqlStatement, e.getMessage()));
        }
    }

    protected static Collection<UserData> queryUserData(String sqlStatement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlStatement, RETURN_GENERATED_KEYS)) {
                preparedStatement.executeQuery();
                ResultSet result = preparedStatement.getGeneratedKeys();
                Collection<UserData> matches = new ArrayList<>();
                while (result.next()) {
                    matches.add(userFromResponse(result));
                }
                return matches;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("The following statement threw an exception when trying to access the database:\n\t%s\n\t%s", sqlStatement, e.getMessage()));
        }
    }


    protected static AuthData authFromResponse(ResultSet rs) throws SQLException {
        String authToken = rs.getString("authToken");
        String username = rs.getString("username");
        return new AuthData(username, authToken);
    }

    protected static GameData gameFromResponse(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("id");
        String white = rs.getString("whiteUsername");
        String black = rs.getString("blackUsername");
        String name = rs.getString("gameName");
        String gameJson = rs.getString("game");
        ChessGame game = GSON.fromJson(gameJson, ChessGame.class);
        return new GameData(gameID, white, black, name, game);
    }

    protected static GameSummary summaryFromResponse(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("id");
        String white = rs.getString("whiteUsername");
        String black = rs.getString("blackUsername");
        String name = rs.getString("gameName");
        return new GameSummary(gameID, white, black, name);
    }

    protected static UserData userFromResponse(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    protected static boolean stringIsUnsafe(String dirtyString) {
        return !dirtyString.matches("[a-zA-Z0-9]+");
    }
}
