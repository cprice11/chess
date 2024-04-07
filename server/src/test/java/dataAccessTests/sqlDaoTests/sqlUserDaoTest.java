package dataAccessTests.sqlDaoTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDao;
import dataAccess.sqlDao.SQLUserDao;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class sqlUserDaoTest extends sqlDataAccessVars {
    UserDao userDAO = new SQLUserDao();

    @BeforeAll
    @Order(1)
    static void initialize() {
        try {
            DatabaseManager.configureDatabase();
            DatabaseManager.resetData();
        } catch (DataAccessException e) {
            Assertions.fail("Could not set up database: " + e.getMessage());
        }
    }

    @BeforeEach
    void setup() {
        try {
            DatabaseManager.resetData();
            userDAO.add(u0);
            userDAO.add(u1);
            userDAO.add(u2);
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void getAll() {
        try {
            Collection<UserData> allUsers = userDAO.getAll();
            Assertions.assertEquals(userData, allUsers);
            Assertions.assertDoesNotThrow(DatabaseManager::resetData);
            allUsers = userDAO.getAll();
            Assertions.assertTrue(allUsers.isEmpty());
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void delete() {
        try {
            userDAO.delete(u0);
            HashSet<UserData> smallerSet = new HashSet<>();
            smallerSet.add(u1);
            smallerSet.add(u2);
            Assertions.assertEquals(smallerSet, userDAO.getAll());
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    void deleteAll() {
        try {
            userDAO.deleteAll();
            Collection<UserData> allUsers = userDAO.getAll();
            Assertions.assertTrue(allUsers.isEmpty());
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void update() {
        try {
            UserData newGuy = new UserData("jeff", "1234", "not.an@email");
            HashSet<UserData> withNewGuy = new HashSet<>();
            withNewGuy.add(newGuy);
            withNewGuy.add(u1);
            withNewGuy.add(u2);
            Assertions.assertDoesNotThrow(() -> userDAO.update(u0, newGuy));
            Assertions.assertEquals(withNewGuy, userDAO.getAll());
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    void verify() {
        try {
        userDAO.delete(u0);
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.verify(u0));
        Assertions.assertDoesNotThrow(() -> userDAO.verify(u2));
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(6)
    void add() {
        try {
        userDAO.deleteAll();
        Assertions.assertDoesNotThrow(() -> userDAO.add(u0));
        HashSet<UserData> justOne = new HashSet<>();
        justOne.add(u0);
        Assertions.assertEquals(justOne, userDAO.getAll());
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    void getUser() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(userDAO.getUser(u0.username()), u0), "Threw unexpected exception");
    }

    @Test
    @Order(9)
    void editUserPassword() {
        try {
            userDAO.editUserPassword(u0.username(), "betterP@ssword");
            Assertions.assertEquals(userDAO.getUser(u0.username()).password(), "betterP@ssword");
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(10)
    void editUserEmail() {
        try {
            userDAO.editUserEmail(u0.username(), "workemail@company.com");
            Assertions.assertEquals(userDAO.getUser(u0.username()).email(), "workemail@company.com");
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }
}
