package dataAccess;

import model.UserData;
import org.eclipse.jetty.servlet.jmx.ServletMappingMBean;

import java.util.Collection;

public class MemoryUserDAO implements UserDAO {

    public MemoryUserDAO() {
    }

    /**
     * Returns all objects in the database
     */
    @Override
    public Collection<UserData> getAll() {
        return MemoryDatabase.users;
    }

    /**
     * @param target The object in the database to be removed
     */
    @Override
    public void delete(UserData target) {
        MemoryDatabase.getUsers().remove(target);
    }

    /**
     * Deletes all objects in the database, leaving the tables
     */
    @Override
    public void deleteAll() {
        MemoryDatabase.clearUsers();
    }

    /**
     * @param target The existing object in the database
     * @param value  The object to replace the target object
     */
    @Override
    public void update(UserData target, UserData value) {
        MemoryDatabase.users.remove(target);
        MemoryDatabase.users.add(value);
    }

    /**
     * @param target the object to search for in the database
     * @throws DataAccessException if the object is not found
     */
    @Override
    public void verify(UserData target) throws DataAccessException {
        if (MemoryDatabase.getUsers().contains(target)) return;
        throw new DataAccessException(target.toString() + " Does not exist in database");
    }

    /**
     * Adds a new object in the database
     *
     * @param entry The object to add
     */
    @Override
    public void add(UserData entry) {
        MemoryDatabase.users.add(entry);
    }

    /**
     * @param username
     * @return
     */
    @Override
    public UserData getUser(String username) {
        for (UserData u : MemoryDatabase.getUsers()) {
            if (u.username().equals(username)) return u;
        }
        return null;
    }

    /**
     * @param username
     * @param password
     */
    @Override
    public void editUserPassword(String username, String password) {
        for (UserData u : MemoryDatabase.getUsers()) {
            if (u.username().equals(username)) {
                MemoryDatabase.users.remove(u);
                UserData updated = new UserData(username, password, u.email());
                MemoryDatabase.users.add(updated);
                return;
            }
        }
    }

    /**
     * @param username
     * @param email
     */
    @Override
    public void editUserEmail(String username, String email) {
        for (UserData u : MemoryDatabase.getUsers()) {
            if (u.username().equals(username)) {
                MemoryDatabase.users.remove(u);
                UserData updated = new UserData(username, u.password(), email);
                MemoryDatabase.users.add(updated);
                return;
            }
        }
    }
}
