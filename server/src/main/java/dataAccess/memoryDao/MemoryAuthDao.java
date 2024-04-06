package dataAccess.memoryDao;

import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class MemoryAuthDao implements AuthDao {
    private static final int AUTH_TOKEN_LENGTH = 40;
    private final Random randomTokenGenerator = new Random(111);

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
     * @param authToken the authentication of the AuthData object to search for in the database
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
        MemoryDatabase.auth.add(entry);
    }

    /**
     * @param username
     * @return
     * @throws DataAccessException
     */
    @Override
    public Collection<AuthData> getAuthFromUser(String username) throws DataAccessException {
        Collection<AuthData> allResults = new ArrayList<>();
        for (AuthData authData : MemoryDatabase.getAuth()) {
            if (authData.username().equals(username)) allResults.add(authData);
        }
        if (allResults.isEmpty()) throw new DataAccessException("No auth data found for user");
        return allResults;
    }

    /**
     * @param authToken
     * @return
     */
    @Override
    public String getUsername(String authToken) {
        for (AuthData a : MemoryDatabase.auth) {
            if (a.authToken().equals(authToken)) return a.username();
        }
        return null;
    }

    /**
     * @param username
     * @return
     */
    @Override
    public AuthData createAuth(String username) {
        AuthData newAuth = new AuthData(pseudoRandomToken(), username);
        add(newAuth);
        return newAuth;
    }

    /**
     * @param authToken
     * @return
     * @throws DataAccessException
     */
    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        return verify(authToken);
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
