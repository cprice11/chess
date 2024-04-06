package dataAccess;

import service.AlreadyTakenException;

import java.util.Collection;

public interface Dao<T> {
    /**
     * Returns all objects in the database
     */
    Collection<T> getAll() throws DataAccessException;

    /**
     * @param target The object in the database to be removed
     */
    void delete(T target);

    /**
     * Deletes all objects in the database, leaving the tables
     */
    void deleteAll();

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    void update(T target, T value) throws DataAccessException;

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    void verify(T target) throws DataAccessException;

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    void add(T entry) throws AlreadyTakenException;
}
