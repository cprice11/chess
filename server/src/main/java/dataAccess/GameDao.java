package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.GameSummary;
import service.AlreadyTakenException;

import java.util.Collection;
import java.util.HashSet;

public interface GameDao extends Dao<GameData> {
    /**
     * Returns all objects in the database
     */
    @Override
    Collection<GameData> getAll() throws DataAccessException;

    /**
     * @param target The object in the database to be removed
     */
    @Override
    void delete(GameData target) throws DataAccessException;

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    void deleteAll() throws DataAccessException;

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    void update(GameData target, GameData value) throws DataAccessException;
    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    void verify(GameData target) throws DataAccessException;

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    void add(GameData entry) throws AlreadyTakenException, DataAccessException;

    /**
     * Gets a list of the summaries of every game in the database
     */
    Collection<GameSummary> getGameSummaries() throws DataAccessException;

    // might move to private in implementation class or move to a utility class to inherit from

    /**
     * creates a GameData object from a gameName parameter.
     *
     * @param gameName The object to add
     */
    int createGame(String gameName) throws DataAccessException;

    /**
     * Confirms that a game is the database
     *
     * @param gameID The ID of the game to confirm
     * @throws DataAccessException if the ID is not found
     */
    GameData verify(int gameID) throws DataAccessException;

    /**
     * Updates a game to a new game state
     *
     * @param gameID The ID of the existing game in the database
     * @param game   The value to set the game state to
     */
    void setGameState(int gameID, ChessGame game) throws DataAccessException;

    /**
     * Updates a game to a new game state
     *
     * @param gameData the game object to replace the existing one
     */
    void setGameState(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    GameSummary getSummary(GameData game) throws DataAccessException;

    HashSet<GameSummary> getGamesByPlayer(String username) throws DataAccessException;

    HashSet<GameSummary> getGamesByName(String name) throws DataAccessException;
}
