package dataAccess;

import chess.ChessGame;
import chess.GameState;
import model.AuthData;
import model.GameData;
import model.GameSummary;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {
    private Random randomIdGenerator = new Random(111);
    /**
     * Returns all objects in the database
     */
    @Override
    public HashSet<GameData> getAll() {
        return MemoryDatabase.getGames();
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(GameData target) {
        MemoryDatabase.getGames().remove(target);
    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() {
        MemoryDatabase.clearGames();
    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    public void update(GameData target, GameData value) {
        MemoryDatabase.games.remove(target);
        MemoryDatabase.games.add(value);
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(GameData target) throws DataAccessException {
        if (MemoryDatabase.getGames().contains(target)) return;
        throw new DataAccessException(target.toString() + " Does not exist in database");
    }

    /**
     * Confirms that a game is the database
     *
     * @param gameID The ID of the game to confirm
     * @throws DataAccessException if the ID is not found
     */
    @Override
    public GameData verify(int gameID) throws DataAccessException {
        for (GameData g : MemoryDatabase.getGames()) {
            if (g.gameID() == gameID) return g;
        }
        throw new DataAccessException(gameID + " Does not exist in database");
    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(GameData entry) {
        MemoryDatabase.games.add(entry);
    }

    /**
     * Gets a list of the summaries of every game in the database
     */
    @Override
    public HashSet<GameSummary> getGameSummaries() {
        HashSet<GameSummary> summaries = new HashSet<>();
        for (GameData g : MemoryDatabase.games) {
            summaries.add(new GameSummary(g.gameID(), g.whiteUsername(), g.blackUsername(), g.gameName()));
        }
        return summaries;
    }

    /**
     * creates a GameData object from a gameName parameter.
     *
     * @param gameName The object to add
     */
    @Override
    public int createGame(String gameName) {
        GameData newGame = new GameData(randomIdGenerator.nextInt(), null, null, gameName, new ChessGame());
        MemoryDatabase.games.add(newGame);
        return newGame.gameID();
    }

    /**
     * Updates a game to a new game state
     *
     * @param gameID The ID of the existing game in the database
     * @param game   The value to set the game state to
     */
    @Override
    public void setGameState(int gameID, ChessGame game) {
        for (GameData g : MemoryDatabase.getGames()) {
            if (g.gameID() == gameID) {
                delete(g);
                add(new GameData(g.gameID(), g.whiteUsername(), g.blackUsername(), g.gameName(), game));
                return;
            }
        }
    }

    @Override
    public void setGameState(GameData gameData) {
        for (GameData g : MemoryDatabase.getGames()) {
            if (g.gameID() == gameData.gameID()){
                update(g, gameData);
                return;
            }
        }
    }

    public GameData getGame(int gameID) {
        for (GameData g : MemoryDatabase.getGames()) {
            if (g.gameID() == gameID) return g;
        }
        return null;
    }

    public HashSet<GameData> getGamesByPlayer(String username) {
        HashSet<GameData> gamesWith = new HashSet<>();
        for (GameData g : MemoryDatabase.getGames()) {
            if (g.whiteUsername() == username || g.blackUsername() == username) gamesWith.add(g);
        }
        return gamesWith;
    }

    public HashSet<GameData> getGamesByName(String name) {
        HashSet<GameData> gamesNamed = new HashSet<>();
        for (GameData g : MemoryDatabase.getGames()) {
            if (g.gameName().equals(name)) gamesNamed.add(g);
        }
        return gamesNamed;
    }
}
