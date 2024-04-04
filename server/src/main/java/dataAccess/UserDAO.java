package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO extends DAO<UserData> {
    /**
     * Returns all objects in the database
     */
    @Override
    default Collection<UserData> getAll() {
        return null;
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    default void delete(UserData target) {

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
    default void update(UserData target, UserData value) {

    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    default void verify(UserData target) throws DataAccessException {

    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    default void add(UserData entry) {

    }

    UserData getUser(String username);

    void editUserPassword(String username, String Password);

    void editUserEmail(String username, String email);
}
