package dataaccess;

import datamodels.GameData;
import datamodels.GameSummary;

import java.util.Collection;

public interface GameDAO {
    void addGame(GameData game) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(int gameID, GameData game) throws DataAccessException;

    Collection<GameSummary> getGameSummaries() throws DataAccessException;

    void clearAll() throws DataAccessException;
}
