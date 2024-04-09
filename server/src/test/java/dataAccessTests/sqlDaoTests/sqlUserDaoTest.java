package dataAccessTests.sqlDaoTests;

import dataAccess.*;
import dataAccess.sqlDao.SQLAuthDao;
import dataAccess.sqlDao.SQLGameDao;
import dataAccess.sqlDao.SQLUserDao;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class sqlUserDaoTest extends sqlDataAccessVars {
    private static AuthDao authDao;
    private static GameDao gamesDao;
    private static UserDao userDao;

    @BeforeAll
    @Order(1)
    static void initialize() {
        try {
            authDao = new SQLAuthDao();
            gamesDao = new SQLGameDao();
            userDao = new SQLUserDao();
            DatabaseManager.resetData();
        } catch (DataAccessException e) {
            Assertions.fail("Could not set up database: " + e.getMessage());
        }
    }

    @BeforeEach
    void setup() {
        try {
            DatabaseManager.resetData();
            userDao.add(u0);
            userDao.add(u1);
            userDao.add(u2);
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void getAll() {
        try {
            Collection<UserData> allUsers = userDao.getAll();
            Assertions.assertEquals(userData, allUsers);
            Assertions.assertDoesNotThrow(DatabaseManager::resetData);
            allUsers = userDao.getAll();
            Assertions.assertTrue(allUsers.isEmpty());
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void delete() {
        try {
            userDao.delete(u0);
            HashSet<UserData> smallerSet = new HashSet<>();
            smallerSet.add(u1);
            smallerSet.add(u2);
            Assertions.assertEquals(smallerSet, userDao.getAll());
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    void deleteAll() {
        try {
            userDao.deleteAll();
            Collection<UserData> allUsers = userDao.getAll();
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
            Assertions.assertDoesNotThrow(() -> userDao.update(u0, newGuy));
            Assertions.assertEquals(withNewGuy, userDao.getAll());
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    void verify() {
        try {
        userDao.delete(u0);
        Assertions.assertThrows(DataAccessException.class, () -> userDao.verify(u0));
        Assertions.assertDoesNotThrow(() -> userDao.verify(u2));
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(6)
    void add() {
        try {
        userDao.deleteAll();
        Assertions.assertDoesNotThrow(() -> userDao.add(u0));
        HashSet<UserData> justOne = new HashSet<>();
        justOne.add(u0);
        Assertions.assertEquals(justOne, userDao.getAll());
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    void getUser() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(userDao.getUser(u0.username()), u0), "Threw unexpected exception");
    }

    @Test
    @Order(9)
    void editUserPassword() {
        try {
            userDao.editUserPassword(u0.username(), "betterP@ssword");
            Assertions.assertEquals(userDao.getUser(u0.username()).password(), "betterP@ssword");
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(10)
    void editUserEmail() {
        try {
            userDao.editUserEmail(u0.username(), "workemail@company.com");
            Assertions.assertEquals(userDao.getUser(u0.username()).email(), "workemail@company.com");
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }
}
