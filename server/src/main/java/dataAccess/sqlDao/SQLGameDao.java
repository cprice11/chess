package dataAccess.sqlDao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDao;
import dataAccess.memoryDao.MemoryDatabase;
import model.AuthData;
import model.GameData;
import model.GameSummary;
import service.AlreadyTakenException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class SQLGameDao implements GameDao {
    private static final String INSERT_STATEMENT = "INSERT INTO games VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_STATEMENT = "SELECT * FROM games WHERE gameID=?";
    private static final String SELECT_SUMMARIES_STATEMENT = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games WHERE gameID=?";
    private static final String DELETE_STATEMENT = "DELETE FROM games WHERE gameID=?";
    private static final String TRUNCATE_STATEMENT = "TRUNCATE TABLE games";
    private static final String UPDATE_STATEMENT = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
    private final Random randomIdGenerator = new Random(111);


    private static GameData selectGame(String gameID) throws DataAccessException {
        ArrayList<GameData> entries = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SELECT_STATEMENT)) {
                preparedStatement.setString(1, gameID);
                ResultSet res = preparedStatement.executeQuery();
                while (res.next()) {
                        int id = res.getInt("gameID");
                        String white = res.getString("whiteUsername");
                        String black = res.getString("blackUsername");
                        String name = res.getString("gameName");

                        String json = res.getString("game");
                        ChessGame game = new Gson().fromJson(json, ChessGame.class);

                        entries.add(new GameData(id, white, black, name, game));
                }
                if(entries.isEmpty()) return null;
                if(entries.size() > 1) throw new DataAccessException("Multiple matches found.");
                return entries.getFirst();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<GameData> getAll() throws DataAccessException {
        Collection<GameData> entries = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM games")) {
                ResultSet res = preparedStatement.executeQuery();
                while (res.next()) {
                    int id = res.getInt("gameID");
                    String white = res.getString("whiteUsername");
                    String black = res.getString("blackUsername");
                    String name = res.getString("gameName");

                    String json = res.getString("game");
                    ChessGame game = new Gson().fromJson(json, ChessGame.class);

                    entries.add(new GameData(id, white, black, name, game));
                }
                return entries;
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(GameData target) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(DELETE_STATEMENT)) {
                preparedStatement.setInt(1, target.gameID());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(TRUNCATE_STATEMENT)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    // Really should just be ID. Something to fix in the next one.
    public void update(GameData target, GameData value) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(UPDATE_STATEMENT)) {
                preparedStatement.setString(1, value.whiteUsername());
                preparedStatement.setString(2, value.blackUsername());
                preparedStatement.setString(3, value.gameName());

                String json = new Gson().toJson(value.game());
                preparedStatement.setString(4, json);

                preparedStatement.setInt(5, value.gameID());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
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
    public void add(GameData entry) throws AlreadyTakenException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(INSERT_STATEMENT)) {
                preparedStatement.setInt(1, entry.gameID());
                preparedStatement.setString(2, entry.whiteUsername());
                preparedStatement.setString(3, entry.blackUsername());
                preparedStatement.setString(4, entry.gameName());

                String json = new Gson().toJson(entry.game());
                preparedStatement.setString(5, json);

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Gets a list of the summaries of every game in the database
     */
    @Override
    public Collection<GameSummary> getGameSummaries() throws DataAccessException{
        Collection<GameSummary> entries = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SELECT_SUMMARIES_STATEMENT)) {
                ResultSet res = preparedStatement.executeQuery();
                while (res.next()) {
                    int id = res.getInt("gameID");
                    String white = res.getString("whiteUsername");
                    String black = res.getString("blackUsername");
                    String name = res.getString("gameName");

                    entries.add(new GameSummary(id, white, black, name));
                }
                return entries;
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * creates a GameData object from a gameName parameter.
     *
     * @param gameName The object to add
     */
    @Override
    public int createGame(String gameName) throws DataAccessException, AlreadyTakenException {
        GameData newGame = new GameData(randomIdGenerator.nextInt() & Integer.MAX_VALUE, null, null, gameName, new ChessGame());
        add(newGame);
        return newGame.gameID();
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
