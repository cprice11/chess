package dataaccess;

import dataModels.GameData;
import dataModels.GameSummary;

import java.util.Collection;

public interface GameDAO {
    void addGame(GameData game);
    GameData getGame(int gameID);
    void updateGame(int gameID, GameData game);
    void deleteGame(int gameID);
    Collection<GameSummary> getGameSummaries();
    void clearAll();
}
