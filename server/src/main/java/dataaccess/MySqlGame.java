package dataaccess;

import datamodels.GameData;
import datamodels.GameSummary;

import java.util.Collection;
import java.util.List;

public class MySqlGame extends MySqlDataAccess implements GameDAO {
    @Override
    public void addGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {

    }

    @Override
    public Collection<GameSummary> getGameSummaries() {
        return List.of();
    }

    @Override
    public void clearAll() {
        String sql = "TRUNCATE game";
        try {
            executeUpdate(sql);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
