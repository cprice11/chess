package dataaccess;

import datamodels.GameData;
import datamodels.GameSummary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGame implements GameDAO{
    HashMap<Integer, GameData> db = new HashMap<>();

    public void addGame(GameData game) throws DataAccessException {
        if (null == game) {
            throw new DataAccessException("game is null");
        }
        if (db.get(game.gameID()) != null) {
            throw new DataAccessException("Invalid ID");
        }
        db.put(game.gameID(), game);
    }

    public GameData getGame(int gameID) {
        return db.get(gameID);
    }

    public void updateGame(int gameID, GameData game) throws DataAccessException{
        if (db.get(gameID) == null) {
            throw new DataAccessException("Game with ID '" + gameID + "' does not exist");
        }
        db.put(gameID, game);
    }

    public Collection<GameSummary> getGameSummaries() {
        ArrayList<GameSummary> summaries = new ArrayList<>();
        db.forEach((gameID, game) -> summaries.add(
                new GameSummary(gameID, game.blackUsername(), game.whiteUsername(), game.gameName())
        ));
        return summaries;
    }

    public void clearAll() {
        db.clear();
    }
}
