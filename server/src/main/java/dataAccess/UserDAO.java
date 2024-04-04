package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO extends DAO<UserData> {
    /**
     * Returns all objects in the database
     */
    @Override
    Collection<UserData> getAll();

    /**
     * @param target The object in the database to be removed
     */
    @Override
    void delete(UserData target);

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
    void update(UserData target, UserData value) throws DataAccessException;

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    void verify(UserData target) throws DataAccessException;

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    void add(UserData entry);

    UserData getUser(String username) throws DataAccessException;

    void editUserPassword(String username, String Password) throws DataAccessException;

    void editUserEmail(String username, String email) throws DataAccessException;
}
