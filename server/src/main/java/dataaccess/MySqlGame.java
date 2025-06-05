package dataaccess;

import chess.ChessGame;
import datamodels.GameData;
import datamodels.GameSummary;

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
        String sql = String.format(
                "INSERT INTO game values (\"%s\", \"%s\", \"%s\", \"%s\", '%s')",
                game.gameID(), game.blackUsername(), game.whiteUsername(), game.gameName(),
                GSON.toJson(game.game(), ChessGame.class)
        );
        executeUpdate(sql);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        String sql = String.format("SELECT * FROM game WHERE id = %s", gameID);
        Collection<GameData> matches;
        matches = queryGameData(sql);
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
        String sql = String.format("UPDATE game SET blackUsername = %s, whiteUsername = %s, gameName = %s, game = %s WHERE gameID = \"%s\";",
                game.blackUsername(), game.whiteUsername(), game.gameName(),
                GSON.toJson(game.game(), ChessGame.class), gameID);
        executeUpdate(sql);
    }

    public Collection<GameSummary> getGameSummaries() throws DataAccessException {
        String sql = "SELECT gameID, blackUsername, whiteUsername, gameName FROM game";
        Collection<GameSummary> matches;
        matches = queryGameSummary(sql);
        if (matches.isEmpty()) {
            return null;
        }
        return matches;
    }

    public void clearAll() {
        String sql = "TRUNCATE game";
        try {
            executeUpdate(sql);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
