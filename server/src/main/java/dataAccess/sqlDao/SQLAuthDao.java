package dataAccess.sqlDao;

import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class SQLAuthDao implements AuthDao {
    private static final String INSERT_STATEMENT = "INSERT INTO auth VALUES (?, ?)";
    private static final String SELECT_STATEMENT = "SELECT authToken, username FROM auth WHERE authToken=?";
    private static final String DELETE_STATEMENT = "DELETE FROM auth WHERE authToken=?";
    private static final String TRUNCATE_STATEMENT = "TRUNCATE TABLE auth";
    private static final int AUTH_TOKEN_LENGTH = 40;
    private static final Random randomTokenGenerator = new Random(111);

    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<AuthData> getAll() throws DataAccessException {
        Collection<AuthData> authData = new HashSet<>();
        try {
            Connection conn = DatabaseManager.getConnection();
            try (var rs = conn.prepareStatement("SELECT authToken, username FROM auth;").executeQuery()) {
                while (rs.next()) {
                    authData.add(new AuthData(rs.getString("authToken"), rs.getString("username")));
                }
            }
            return authData;
        } catch (DataAccessException e) {
            return authData;
        } catch (SQLException e) {
            throw new DataAccessException("Threw exception accessing database: " + e.getMessage());
        }
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(AuthData target) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(DELETE_STATEMENT)) {
                preparedStatement.setString(1, target.authToken());
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
    public void update(AuthData target, AuthData value) {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(AuthData target) throws DataAccessException {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    /**
     * @param authToken the authentication of the AuthData object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public AuthData verify(String authToken) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        AuthData result;
        try {
            try (var preparedStatement = conn.prepareStatement(SELECT_STATEMENT)) {
                preparedStatement.setString(1, authToken);
                ResultSet res = preparedStatement.executeQuery();
                if (res.next()) {
                    result = new AuthData(res.getString("authToken"), res.getString("username"));
                    if (res.next()) throw new DataAccessException("More than one result returned");
                } else  throw new DataAccessException("More than one result returned");
                return result;
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(AuthData entry) throws DataAccessException{
        Connection conn = DatabaseManager.getConnection();
        try {
            try (var preparedStatement = conn.prepareStatement(INSERT_STATEMENT)) {
                preparedStatement.setString(1, entry.authToken());
                preparedStatement.setString(2, entry.username());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * @param username 
     * @return
     * @throws DataAccessException
     */
    @Override
    public Collection<AuthData> getAuthFromUser(String username) throws DataAccessException {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    /**
     * @param authToken 
     * @return
     * @throws DataAccessException
     */
    @Override
    public String getUsername(String authToken) throws DataAccessException {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    /**
     * @param username 
     * @return
     */
    @Override
    public AuthData createAuth(String username) {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    /**
     * @param authToken 
     * @return
     * @throws DataAccessException
     */
    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try {
        try (var preparedStatement = conn.prepareStatement(SELECT_STATEMENT)) {
                preparedStatement.setString(1, authToken);
                ResultSet res = preparedStatement.executeQuery();
                return new AuthData(res.getString("authToken"), res.getString("username"));
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static String sanitizeInput(String input) {
        input = input.replace("'", "\\'");
        input = input.replace("\\", "\\\\");
        return input;
    }

    private String pseudoRandomToken() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < AUTH_TOKEN_LENGTH; i++ ){
            int myInt = randomTokenGenerator.nextInt(94) + 33;
            char myChar = (char) myInt;
            id.append(myChar);
        }
        return id.toString();
    }
}
