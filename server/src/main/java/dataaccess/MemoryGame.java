package dataaccess;

import dataModels.AuthData;
import dataModels.GameData;
import dataModels.GameSummary;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class MemoryGame implements GameDAO{
    HashMap<Integer, GameData> db = new HashMap<>();
    public void addGame(GameData game) {
        db.put(game.gameID(), game);
    }

    public GameData getGame(int gameID) {
        return db.get(gameID);
    }

    public void updateGame(int gameID, GameData game) {
        db.put(gameID, game);
    }

    public void deleteGame(int gameID) {
        db.remove(gameID);
    }

    public Collection<GameSummary> getGameSummaries() {
        ArrayList<GameSummary> summaries = new ArrayList<>();
        db.forEach((gameID, game)  -> {
            summaries.add(new GameSummary(gameID, game.blackUsername(), game.whiteUsername(), game.gameName()));
        });
        return summaries;
    }

    public void clearAll() {
        db.clear();
    }

    public void print() {
        db.forEach((i, g) -> {
            System.out.println("ID: " + i + " name: " + g.gameName());
        });
    }
}
