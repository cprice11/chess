package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.GameSummary;

import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<GameData> getAll() {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(GameData target) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    public void update(GameData target, GameData value) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(GameData target) throws DataAccessException {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(GameData entry) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Gets a list of the summaries of every game in the database
     */
    @Override
    public Collection<GameSummary> getGameSummaries() {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * creates a GameData object from a gameName parameter.
     *
     * @param gameName The object to add
     */
    @Override
    public int createGame(String gameName) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Confirms that a game is the database
     *
     * @param gameID The ID of the game to confirm
     * @throws DataAccessException if the ID is not found
     */
    @Override
    public GameData verify(int gameID) throws DataAccessException {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Updates a game to a new game state
     *
     * @param gameID The ID of the existing game in the database
     * @param game   The value to set the game state to
     */
    @Override
    public void setGameState(int gameID, ChessGame game) {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void setGameState(GameData gameData) {
        throw new RuntimeException("Not yet implemented");
    }

    public GameData getGame(int gameID) {
        throw new RuntimeException("Not yet implemented");
    }

    public Collection<GameData> getGamesByPlayer(String username) {
        throw new RuntimeException("Not yet implemented");
    }

    public Collection<GameData> getGamesByName(String name) {
        throw new RuntimeException("Not yet implemented");
    }
}
