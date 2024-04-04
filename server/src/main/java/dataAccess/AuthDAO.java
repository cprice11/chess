package dataAccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDAO extends DAO<AuthData> {
    /**
     * Returns all objects in the database
     */
    @Override
    Collection<AuthData> getAll();

    /**
     * @param target The object in the database to be removed
     */
    @Override
    void delete(AuthData target);

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    void deleteAll();

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    void update(AuthData target, AuthData value);

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    void verify(AuthData target) throws DataAccessException;

    /**
     * @param authToken the authToken of the AuthData object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    AuthData verify(String authToken) throws DataAccessException;

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    void add(AuthData entry);

    String getAuthToken(String username);

    String getUsername(String authToken);

    AuthData createAuth(String username);
}
