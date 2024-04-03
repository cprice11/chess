package dataAccess;

import model.UserData;

import java.util.Collection;

public class MemoryUserDAO implements UserDAO{
    MemoryDatabase db;

    public MemoryUserDAO(MemoryDatabase db) {
        this.db = db;
    }
    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<UserData> getAll() {
        return db.getUsers();
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(UserData target) {
        UserDAO.super.delete(target);
    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() {
        UserDAO.super.deleteAll();
    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    public void update(UserData target, UserData value) {
        UserDAO.super.update(target, value);
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(UserData target) throws DataAccessException {
        UserDAO.super.verify(target);
    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(UserData entry) {
        UserDAO.super.add(entry);
    }

    /**
     * @param username 
     * @return
     */
    @Override
    public UserData getUser(String username) {
        return null;
    }

    /**
     * @param user 
     * @param newUsername
     */
    @Override
    public void EditUserUsername(UserData user, String newUsername) {

    }

    /**
     * @param user 
     * @param Password
     */
    @Override
    public void EditUserPassword(UserData user, String Password) {

    }

    /**
     * @param user 
     * @param email
     */
    @Override
    public void EditUserEmail(UserData user, String email) {

    }
}
