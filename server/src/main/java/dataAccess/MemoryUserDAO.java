package dataAccess;

import model.UserData;

import java.util.Collection;

public class MemoryUserDAO implements UserDAO {

    public MemoryUserDAO() {
    }

    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<UserData> getAll() {
        return MemoryDatabase.getUsers();
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
        throw new RuntimeException("Not yet implimented");
    }

    /**
     * @param user
     * @param newUsername
     */
    @Override
    public void editUserUsername(UserData user, String newUsername) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param user
     * @param Password
     */
    @Override
    public void editUserPassword(UserData user, String Password) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param user
     * @param email
     */
    @Override
    public void editUserEmail(UserData user, String email) {
        throw new RuntimeException("Not yet implemented");
    }
}
