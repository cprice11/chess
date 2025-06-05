package dao;

import dataaccess.DataAccessException;
import datamodels.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTests extends DbUnitTests {
    // ClearAll
    @Test
    @DisplayName("Clear all user data")
    public void clearUser() {
        addTwoUsers();
        userDAO.clearAll();
        try {
            Assertions.assertNull(userDAO.getUser(userA.username()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    // AddUser
    @Test
    @DisplayName("Add and get")
    public void addAndRetrieveSuccessfully() {
        try {
            Assertions.assertNull(userDAO.getUser(userA.username()));
            userDAO.addUser(userA);
            Assertions.assertEquals(userA, userDAO.getUser(userA.username()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("Add twice")
    public void addDuplicateUser() {
        try {
            Assertions.assertNull(userDAO.getUser(userA.username()));
            userDAO.addUser(userA);
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(userA);
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(new UserData(userA.username(), userC.password(), userC.email()));
        });
    }

    @Test
    @DisplayName("Add malformed")
    public void addMalformedUser() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(null);
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(new UserData(null, null, null));
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(new UserData("uname", null, "email"));
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(new UserData(null, "pass", "email"));
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(new UserData(null, null, "email"));
        });
    }

    // getUser
    @Test
    @DisplayName("Get user by username")
    public void goodGetUserCalls() {
        addTwoUsers();
        try {
            Assertions.assertEquals(userA, userDAO.getUser(userA.username()));
            Assertions.assertEquals(userA.username(), userDAO.getUser(userA.username()).username());
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("Using bad username fails")
    public void badDataGetUserCalls() {
        addTwoUsers();
        try {
            Assertions.assertNull(userDAO.getUser(authC.username()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("Malformed get call fails")
    public void malformedGetUser() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUser(null);
        });
    }

    public void addTwoUsers() {
        try {
            userDAO.addUser(userA);
            userDAO.addUser(userB);
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }
}
