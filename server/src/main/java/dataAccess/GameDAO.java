package dataAccess;

import chess.ChessGame;
import chess.InvalidMoveException;
import model.AuthData;
import model.GameData;
import model.GameSummary;

import java.util.Collection;

public interface GameDAO extends DAO<GameData> {
    /**
     * Returns all objects in the database
     */
    @Override
    default Collection<GameData> getAll() {
        return null;
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    default void delete(GameData target) {

    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    default void deleteAll() {

    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    default void update(GameData target, GameData value) {

    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    default void verify(GameData target) throws DataAccessException {

    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    default void add(GameData entry) {

    }

    /**
     * Gets a list of the summaries of every game in the database
     */
    Collection<GameSummary> getGameSummaries();

    // might move to private in implementation class or move to a utility class to inherit from

    /**
     * creates a GameData object from a gameName parameter.
     *
     * @param gameName The object to add
     */
    int createGame(String gameName);

    /**
     * Confirms that a game is the database
     *
     * @param gameID The ID of the game to confirm
     * @throws DataAccessException if the ID is not found
     */
    GameData validate(int gameID) throws DataAccessException;

    /**
     * Updates a game to a new game state
     *
     * @param gameID The ID of the existing game in the database
     * @param game   The value to set the game state to
     */
    void setGameState(int gameID, ChessGame game);
}
