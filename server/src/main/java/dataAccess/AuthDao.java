package dataAccess;

import model.AuthData;
import service.AlreadyTakenException;

import java.util.Collection;

public interface AuthDao extends Dao<AuthData> {
    /**
     * Returns all objects in the database
     */
    @Override
    Collection<AuthData> getAll() throws DataAccessException;

    /**
     * @param target The object in the database to be removed
     */
    @Override
    void delete(AuthData target) throws DataAccessException;

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
    void update(AuthData target, AuthData value) throws DataAccessException;

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    void verify(AuthData target) throws DataAccessException;

    /**
     * @param authToken the authentication of the AuthData object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    AuthData verify(String authToken) throws DataAccessException;

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    void add(AuthData entry) throws AlreadyTakenException, DataAccessException;

    Collection<AuthData> getAuthFromUser(String username) throws DataAccessException;

    String getUsername(String authToken) throws DataAccessException;

    AuthData createAuth(String username) throws DataAccessException;

    AuthData getAuthFromToken(String authToken) throws DataAccessException;
}
