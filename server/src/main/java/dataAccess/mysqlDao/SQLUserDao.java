package dataAccess.mysqlDao;

import dataAccess.DataAccessException;
import dataAccess.UserDao;
import model.UserData;
import service.AlreadyTakenException;

import java.util.Collection;

public class SQLUserDao implements UserDao {
    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<UserData> getAll() {
        return null;
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(UserData target) {

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
    public void update(UserData target, UserData value) throws DataAccessException {

    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(UserData target) throws DataAccessException {

    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(UserData entry) throws AlreadyTakenException {

    }

    /**
     * @param username 
     * @return
     * @throws DataAccessException
     */
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    /**
     * @param username 
     * @param password
     * @throws DataAccessException
     */
    @Override
    public void editUserPassword(String username, String password) throws DataAccessException {

    }

    /**
     * @param username 
     * @param email
     * @throws DataAccessException
     */
    @Override
    public void editUserEmail(String username, String email) throws DataAccessException {

    }
}
