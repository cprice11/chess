package dataaccess;

import chess.ChessGame;
import datamodels.GameData;
import datamodels.GameSummary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlGame extends MySqlDataAccess implements GameDAO {

    public void addGame(GameData game) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("game is null");
        }
        if (getGame(game.gameID()) != null) {
            throw new DataAccessException("gameID already in database");
        }
        if (stringIsUnsafe(game.blackUsername()) || stringIsUnsafe(game.whiteUsername()) || stringIsUnsafe(game.gameName())) {
            return;
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO game values (?, ?, ?, ?, ?)")) {
            statement.setInt(1, game.gameID());
            statement.setString(2, game.blackUsername());
            statement.setString(3, game.whiteUsername());
            statement.setString(4, game.gameName());
            statement.setString(5, GSON.toJson(game.game(), ChessGame.class));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        Collection<GameData> matches = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM game WHERE id = ?")) {
            statement.setInt(1, gameID);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    matches.add(gameFromResponse(result));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (matches.isEmpty()) {
            return null;
        }
        if (matches.size() > 1) {
            throw new RuntimeException("Uh Oh, We've got two identical gameIDs");
        }
        return new ArrayList<>(matches).getFirst();
    }

    public void updateGame(int gameID, GameData game) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("game is null");
        }
        if (stringIsUnsafe(game.blackUsername()) || stringIsUnsafe(game.whiteUsername()) || stringIsUnsafe(game.gameName())) {
            return;
        }

        try (PreparedStatement statement = conn.prepareStatement("UPDATE game SET blackUsername = ?, whiteUsername = ?, gameName = ?, game = ? WHERE gameID = ?")) {
            statement.setString(1, game.blackUsername());
            statement.setString(2, game.whiteUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, GSON.toJson(game.game(), ChessGame.class));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public Collection<GameSummary> getGameSummaries() throws DataAccessException {
        String sql = "SELECT gameID, blackUsername, whiteUsername, gameName FROM game";
        Collection<GameSummary> matches = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    matches.add(summaryFromResponse(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (matches.isEmpty()) {
            return null;
        }
        return matches;
    }

    public void clearAll() throws DataAccessException {
        String sql = "TRUNCATE game";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
