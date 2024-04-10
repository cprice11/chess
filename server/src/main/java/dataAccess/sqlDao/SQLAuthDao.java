package dataAccess.sqlDao;

import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class SQLAuthDao extends SQLDaoHelpers implements AuthDao {
    private static final String INSERT_STATEMENT = "INSERT INTO auth VALUES (?, ?)";
    private static final String SELECT_STATEMENT = "SELECT authToken, username FROM auth WHERE authToken=?";
    private static final String DELETE_STATEMENT = "DELETE FROM auth WHERE authToken=?";
    private static final String TRUNCATE_STATEMENT = "TRUNCATE TABLE auth";
    private static final String SELECT_STATEMENT_USER = "SELECT authToken, username FROM auth WHERE username=?";
    private static final int AUTH_TOKEN_LENGTH = 40;
    private final Random randomTokenGenerator = new Random(System.nanoTime());

    public SQLAuthDao() throws DataAccessException{
        DatabaseManager.configureDatabase();
    }


    private AuthData selectAuth(String authToken) throws DataAccessException {
        ArrayList<AuthData> entries = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SELECT_STATEMENT)) {
                preparedStatement.setString(1, authToken);
                ResultSet res = preparedStatement.executeQuery();
                while (res.next()) {
                    entries.add(new AuthData(
                            res.getString("authToken"),
                            res.getString("username")
                    ));
                }
                if (entries.isEmpty()) return null;
                if (entries.size() > 1) throw new DataAccessException("Multiple matches found.");
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
    public Collection<AuthData> getAll() throws DataAccessException {
        Collection<AuthData> authData = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
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
    public void delete(AuthData target) throws DataAccessException {
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
    public void deleteAll() throws DataAccessException {
        executePreparedStatement(TRUNCATE_STATEMENT);
    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    public void update(AuthData target, AuthData value) throws DataAccessException {
        // this ends up being better than UPDATE in this case
        delete(target);
        add(value);
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(AuthData target) throws DataAccessException {
        verify(target.authToken());
    }

    /**
     * @param authToken the authentication of the AuthData object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public AuthData verify(String authToken) throws DataAccessException {
        AuthData auth = selectAuth(authToken);
        if (auth == null) throw new DataAccessException("Token not verified");
        return auth;
    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(AuthData entry) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
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
     * @return All AuthData objects for the given user
     * @throws DataAccessException if no AuthToken is found in the database
     */
    @Override
    public Collection<AuthData> getAuthFromUser(String username) throws DataAccessException {
        ArrayList<AuthData> entries = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SELECT_STATEMENT_USER)) {
                preparedStatement.setString(1, username);
                ResultSet res = preparedStatement.executeQuery();
                while (res.next()) {
                    entries.add(new AuthData(
                            res.getString("authToken"),
                            res.getString("username")
                    ));
                }
                if (entries.isEmpty()) throw new DataAccessException("No matches found.");
                return entries;
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * @return the username paired with the given authToken
     */
    @Override
    public String getUsername(String authToken) throws DataAccessException {
        AuthData auth = selectAuth(authToken);
        return (auth == null) ? null : auth.username();
    }

    /**
     * @param username the user to create an auth token for
     * @return the AuthData object created and added to the database
     */
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        AuthData newAuth = new AuthData(pseudoRandomToken(), username);
        add(newAuth);
        return newAuth;
    }

    /**
     * @return the AuthData object with the given authToken
     * @throws DataAccessException if the authToken is not valid
     */
    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        return verify(authToken);
    }

    private String pseudoRandomToken() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < AUTH_TOKEN_LENGTH; i++) {
            int myInt = randomTokenGenerator.nextInt(94) + 33;
            char myChar = (char) myInt;
            id.append(myChar);
        }
        return id.toString();
    }
}
