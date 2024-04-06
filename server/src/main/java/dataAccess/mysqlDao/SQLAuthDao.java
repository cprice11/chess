package dataAccess.mysqlDao;

import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import model.AuthData;

import java.util.Collection;

public class SQLAuthDao implements AuthDao {
    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<AuthData> getAll() {
        return null;
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(AuthData target) {

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
    public void update(AuthData target, AuthData value) {

    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(AuthData target) throws DataAccessException {

    }

    /**
     * @param authToken the authentication of the AuthData object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public AuthData verify(String authToken) throws DataAccessException {
        return null;
    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(AuthData entry) {

    }

    /**
     * @param username 
     * @return
     * @throws DataAccessException
     */
    @Override
    public Collection<AuthData> getAuthFromUser(String username) throws DataAccessException {
        return null;
    }

    /**
     * @param authToken 
     * @return
     * @throws DataAccessException
     */
    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return null;
    }

    /**
     * @param username 
     * @return
     */
    @Override
    public AuthData createAuth(String username) {
        return null;
    }

    /**
     * @param authToken 
     * @return
     * @throws DataAccessException
     */
    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        return null;
    }
}
