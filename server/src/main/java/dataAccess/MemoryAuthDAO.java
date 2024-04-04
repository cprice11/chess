package dataAccess;

import model.AuthData;

import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO {

    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<AuthData> getAll() {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(AuthData target) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    public void update(AuthData target, AuthData value) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(AuthData target) throws DataAccessException {
        throw new RuntimeException("Not yet implemented");
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
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param authToken
     * @return
     */
    @Override
    public AuthData getUsername(String authToken) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param username
     * @return
     */
    @Override
    public AuthData createAuth(String username) {
        throw new RuntimeException("Not yet implemented");
    }
}
