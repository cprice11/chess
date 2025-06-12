package dataaccess;

import datamodels.GameData;
import datamodels.GameSummary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlGame extends MySqlDataAccess implements GameDAO {

    public void addGame(GameData game) throws DataAccessException {
        checkConnection();
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
            statement.setString(5, game.game().toJson());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        checkConnection();
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
        checkConnection();
        if (game == null) {
            throw new DataAccessException("game is null");
        }
        if (getGame(gameID) == null) {
            throw new DataAccessException("game doesn't exist");
        }
        if (stringIsUnsafe(game.blackUsername()) || stringIsUnsafe(game.whiteUsername()) || stringIsUnsafe(game.gameName())) {
            return;
        }
        String sql = "UPDATE game SET blackUsername = ?, whiteUsername = ?, gameName = ?, game = ? WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, game.blackUsername());
            statement.setString(2, game.whiteUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, game.game().toJson());
            statement.setInt(5, gameID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public Collection<GameSummary> getGameSummaries() throws DataAccessException {
        checkConnection();
        String sql = "SELECT id, blackUsername, whiteUsername, gameName FROM game";
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
        return matches;
    }

    public void clearAll() throws DataAccessException {
        checkConnection();
        String sql = "TRUNCATE game";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public int getMaxGameID() throws DataAccessException {
        checkConnection();
        String sql = "SELECT MAX(id) FROM game";
        int maxID = 10000;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return Math.max(maxID, rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
