package dataAccess.mysqlDao;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import model.GameData;
import model.GameSummary;
import service.AlreadyTakenException;

import java.util.Collection;
import java.util.HashSet;

public class SQLGameDao implements GameDao {
    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<GameData> getAll() {
        return null;
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(GameData target) {

    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() {

    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    public void update(GameData target, GameData value) throws DataAccessException {

    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(GameData target) throws DataAccessException {

    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(GameData entry) throws AlreadyTakenException {

    }

    /**
     * Gets a list of the summaries of every game in the database
     */
    @Override
    public Collection<GameSummary> getGameSummaries() {
        return null;
    }

    /**
     * creates a GameData object from a gameName parameter.
     *
     * @param gameName The object to add
     */
    @Override
    public int createGame(String gameName) {
        return 0;
    }

    /**
     * Confirms that a game is the database
     *
     * @param gameID The ID of the game to confirm
     * @throws DataAccessException if the ID is not found
     */
    @Override
    public GameData verify(int gameID) throws DataAccessException {
        return null;
    }

    /**
     * Updates a game to a new game state
     *
     * @param gameID The ID of the existing game in the database
     * @param game   The value to set the game state to
     */
    @Override
    public void setGameState(int gameID, ChessGame game) throws DataAccessException {

    }

    /**
     * Updates a game to a new game state
     *
     * @param gameData the game object to replace the existing one
     */
    @Override
    public void setGameState(GameData gameData) throws DataAccessException {

    }

    /**
     * @param gameID 
     * @return
     * @throws DataAccessException
     */
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    /**
     * @param game 
     * @return
     */
    @Override
    public GameSummary getSummary(GameData game) {
        return null;
    }

    /**
     * @param username 
     * @return
     */
    @Override
    public HashSet<GameSummary> getGamesByPlayer(String username) {
        return null;
    }

    /**
     * @param name 
     * @return
     */
    @Override
    public HashSet<GameSummary> getGamesByName(String name) {
        return null;
    }
}
