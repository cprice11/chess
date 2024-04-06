package dataAccess.memoryDao;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import model.GameData;
import model.GameSummary;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class MemoryGameDao implements GameDao {
    private final Random randomIdGenerator = new Random(111);
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
            summaries.add(getSummary(g));
        }
        return summaries;
    }

    public GameSummary getSummary(GameData game) {
        return new GameSummary(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
    }

    /**
     * creates a GameData object from a gameName parameter.
     *
     * @param gameName The object to add
     */
    @Override
    public int createGame(String gameName) {
        GameData newGame = new GameData(randomIdGenerator.nextInt() & Integer.MAX_VALUE, null, null, gameName, new ChessGame());
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
    public void setGameState(int gameID, ChessGame game) throws DataAccessException{
        for (GameData g : MemoryDatabase.getGames()) {
            if (g.gameID() == gameID) {
                delete(g);
                add(new GameData(g.gameID(), g.whiteUsername(), g.blackUsername(), g.gameName(), game));
                return;
            }
        }
        throw new DataAccessException("Game with ID '" + gameID + " was not found.");
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

    public GameData getGame(int gameID) throws DataAccessException{
        for (GameData g : MemoryDatabase.getGames()) {
            if (g.gameID() == gameID) return g;
        }
        throw new DataAccessException("No game with ID:" + gameID);
    }

    public HashSet<GameSummary> getGamesByPlayer(String username) {
        HashSet<GameSummary> gamesWith = new HashSet<>();
        for (GameData g : MemoryDatabase.getGames()) {
            if (Objects.equals(g.whiteUsername(), username) || Objects.equals(g.blackUsername(), username)) gamesWith.add(getSummary(g));
        }
        return gamesWith;
    }

    public HashSet<GameSummary> getGamesByName(String name) {
        HashSet<GameSummary> gamesNamed = new HashSet<>();
        for (GameData g : MemoryDatabase.getGames()) {
            if (g.gameName().equals(name)) gamesNamed.add(getSummary(g));
        }
        return gamesNamed;
    }
}
