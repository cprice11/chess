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
        return !dirtyString.matches("[a-zA-Z0-9-$]+");
    }
}
