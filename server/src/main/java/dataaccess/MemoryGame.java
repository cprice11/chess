package dataaccess;

import datamodels.GameData;
import datamodels.GameSummary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGame implements GameDAO{
    HashMap<Integer, GameData> db = new HashMap<>();

    public void addGame(GameData game) throws DataAccessException {
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

//    public void deleteGame(int gameID) {
//        db.remove(gameID);
//    }

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

//    public void print() {
//        db.forEach((i, g) -> {
//            System.out.println("ID: " + i + " name: " + g.gameName());
//        });
//    }
}
