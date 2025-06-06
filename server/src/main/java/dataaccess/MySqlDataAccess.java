package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    protected static GsonBuilder gsonBuilder;



    public MySqlDataAccess() {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessGame.class, new ChessGame.ChessGameDeserializer());
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
        String black = rs.getString("blackUsername");
        String white = rs.getString("whiteUsername");
        String name = rs.getString("gameName");
        String gameJson = rs.getString("game");
        ChessGame game = gsonBuilder.create().fromJson(gameJson, ChessGame.class);
        return new GameData(gameID, black, white, name, game);
    }

    protected static GameSummary summaryFromResponse(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("id");
        String black = rs.getString("blackUsername");
        String white = rs.getString("whiteUsername");
        String name = rs.getString("gameName");
        return new GameSummary(gameID, black, white, name);
    }

    protected static UserData userFromResponse(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    protected static boolean stringIsUnsafe(String dirtyString) {
        if (dirtyString == null) {
            return false;
        }
        return !dirtyString.matches("[a-zA-Z0-9-$!' ]+");
    }
}
