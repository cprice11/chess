package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO{

    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<AuthData> getAll() {
        return AuthDAO.super.getAll();
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(AuthData target) {
        AuthDAO.super.delete(target);
    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() {
        AuthDAO.super.deleteAll();
    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    public void update(AuthData target, AuthData value) {
        AuthDAO.super.update(target, value);
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(AuthData target) throws DataAccessException {
        AuthDAO.super.verify(target);
    }

    /**
     * @param authToken the authToken of the AuthData object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(String authToken) throws DataAccessException {

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
     */
    @Override
    public AuthData getAuthToken(String username) {
        return null;
    }

    /**
     * @param authToken 
     * @return
     */
    @Override
    public AuthData getUsername(String authToken) {
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
}
