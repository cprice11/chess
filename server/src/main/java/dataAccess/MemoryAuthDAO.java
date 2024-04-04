package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {
    public MemoryDatabase db;
    public MemoryAuthDAO(MemoryDatabase db) {
        this.db = db;
    }

    /**
     * Returns all objects in the database
     */
    @Override
    public HashSet<AuthData> getAll() {
        return MemoryDatabase.getAuth();
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(AuthData target) {
        MemoryDatabase.auth.remove(target);
    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() {
        MemoryDatabase.clearAuth();
    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    public void update(AuthData target, AuthData value) {
        MemoryDatabase.auth.remove(target);
        MemoryDatabase.auth.add(value);
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(AuthData target) throws DataAccessException {
        if (MemoryDatabase.getAuth().contains(target)) return;
        throw new DataAccessException(target.toString() + " Does not exist in database");
    }

    /**
     * @param authToken the authToken of the AuthData object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public AuthData verify(String authToken) throws DataAccessException {
        Collection<AuthData> authData = MemoryDatabase.getAuth();
        for (AuthData a : authData) {
            if (a.authToken().equals(authToken)) return a;
        }
        throw new DataAccessException(authToken + " Does not exist in database");
    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(AuthData entry) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param username
     * @return
     */
    @Override
    public String getAuthToken(String username) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param authToken
     * @return
     */
    @Override
    public String getUsername(String authToken) {
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
