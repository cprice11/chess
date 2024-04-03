package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface AuthDAO extends DAO<AuthData>{
    /**
     * Returns all objects in the database
     */
    @Override
    default Collection<AuthData> getAll() {
        return null;
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    default void delete(AuthData target) {

    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    default void deleteAll() {

    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    default void update(AuthData target, AuthData value) {

    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    default void verify(AuthData target) throws DataAccessException {

    }

    /**
     * @param authToken the authToken of the AuthData object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    void verify(String authToken) throws DataAccessException;

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    void add(AuthData entry);

    AuthData getAuthToken(String username);

    AuthData getUsername(String authToken);

    AuthData createAuth(String username);
}
