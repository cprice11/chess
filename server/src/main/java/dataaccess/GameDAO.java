package dataaccess;

import datamodels.GameData;
import datamodels.GameSummary;

import java.util.Collection;

public interface GameDAO {
    void addGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID);
    void updateGame(int gameID, GameData game) throws DataAccessException;
//    void deleteGame(int gameID);
    Collection<GameSummary> getGameSummaries();
    void clearAll();
}
