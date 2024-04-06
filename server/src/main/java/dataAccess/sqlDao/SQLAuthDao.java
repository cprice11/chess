package dataAccess.sqlDao;

import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;

public class SQLAuthDao implements AuthDao {
    private static final String INSERT_STRING = "INSERT INTO auth VALUES (?, ?)";
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
    public void delete(AuthData target) {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() {
        throw new RuntimeException("NOT YET IMPLEMENTED");
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
        throw new RuntimeException("NOT YET IMPLEMENTED");
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
            try (var preparedStatement = conn.prepareStatement(INSERT_STRING)) {
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
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    private static String sanitizeInput(String input) {
        input = input.replace("'", "\\'");
        input = input.replace("\\", "\\\\");
        return input;
    }
}
