package dataAccess.sqlDao;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDao;
import model.UserData;
import service.AlreadyTakenException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class SQLUserDao implements UserDao {
    private static final String INSERT_STATEMENT = "INSERT INTO users VALUES (?, ?, ?)";
    private static final String SELECT_STATEMENT = "SELECT * FROM users WHERE username=?";
    private static final String DELETE_STATEMENT = "DELETE FROM users WHERE username=?";
    private static final String TRUNCATE_STATEMENT = "TRUNCATE TABLE users";
    private static final String SELECT_STATEMENT_USER = "SELECT usersToken, username FROM users WHERE username=?";


    private UserData selectUser(String username) throws DataAccessException {
        ArrayList<UserData> entries = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SELECT_STATEMENT)) {
                preparedStatement.setString(1, username);
                ResultSet res = preparedStatement.executeQuery();
                while (res.next()) {
                    entries.add(new UserData(
                            res.getString("username"),
                            res.getString("password"),
                            res.getString("email")
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
    public Collection<UserData> getAll() throws DataAccessException {
        Collection<UserData> userData = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var rs = conn.prepareStatement("SELECT * FROM users;").executeQuery()) {
                while (rs.next()) {
                    userData.add(
                            new UserData(
                                    rs.getString("username"),
                                    rs.getString("password"),
                                    rs.getString("email")
                            )
                    );
                }
            }
            return userData;
        } catch (DataAccessException e) {
            return userData;
        } catch (SQLException e) {
            throw new DataAccessException("Threw exception accessing database: " + e.getMessage());
        }
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(UserData target) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(DELETE_STATEMENT)) {
                preparedStatement.setString(1, target.username());
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
    public void update(UserData target, UserData value) throws DataAccessException {
        delete(target);
        try {
            add(value);
        } catch (AlreadyTakenException e) {
            throw new DataAccessException("multiple targets matched given value: " + e.getMessage());
        }
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(UserData target) throws DataAccessException {
        UserData user = selectUser(target.username());
        if (user == null) throw new DataAccessException("Token not verified");
    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(UserData entry) throws AlreadyTakenException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(INSERT_STATEMENT)) {
                preparedStatement.setString(1, entry.username());
                preparedStatement.setString(2, entry.password());
                preparedStatement.setString(3, entry.email());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new AlreadyTakenException(e.getMessage());
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
    public UserData getUser(String username) throws DataAccessException {
        UserData user =  selectUser(username);
        if (user == null) throw new DataAccessException("User not found");
        return user;
    }

    /**
     * @param username
     * @param password
     * @throws DataAccessException
     */
    @Override
    public void editUserPassword(String username, String password) throws DataAccessException {
        UserData existingData = selectUser(username);
        if (existingData == null) throw new DataAccessException("No user found to update");
        delete(existingData);
        try {
            add(new UserData(username, password, existingData.email()));
        } catch (AlreadyTakenException e) {
            throw new DataAccessException("Multiple targets matching that username were found: " + e.getMessage());
        }
    }

    /**
     * @param username
     * @param email
     * @throws DataAccessException
     */
    @Override
    public void editUserEmail(String username, String email) throws DataAccessException {
        UserData existingData = selectUser(username);
        if (existingData == null) throw new DataAccessException("No user found to update");
        delete(existingData);
        try {
            add(new UserData(username, existingData.password(), email));
        } catch (AlreadyTakenException e) {
            throw new DataAccessException("Multiple targets matching that username were found: " + e.getMessage());
        }
    }
}
