package dataAccessTests.sqlDaoTests;

import dataAccess.DataAccessException;
import dataAccess.memoryDao.MemoryDatabase;
import dataAccess.memoryDao.MemoryUserDao;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class sqlUserDaoTest extends sqlDataAccessVars {
    MemoryUserDao userDAO = new MemoryUserDao();

    @BeforeEach
    void setup() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
    }

    @Test
    @Order(1)
    void getAll() {
        Collection<UserData> allUsers = userDAO.getAll();
        Assertions.assertEquals(allUsers, userData);
        MemoryDatabase.clearUsers();
        allUsers = userDAO.getAll();
        Assertions.assertTrue(allUsers.isEmpty());
    }

    @Test
    @Order(2)
    void delete() {
        userDAO.delete(u0);
        HashSet<UserData> smallerSet = new HashSet<>();
        smallerSet.add(u1);
        smallerSet.add(u2);
        Assertions.assertEquals(smallerSet, userDAO.getAll());
    }

    @Test
    @Order(3)
    void deleteAll() {
        userDAO.deleteAll();
        Collection<UserData> allUsers = userDAO.getAll();
        Assertions.assertTrue(allUsers.isEmpty());
    }

    @Test
    @Order(4)
    void update() {
        UserData newGuy = new UserData("jeff", "1234", "not.an@email");
        HashSet<UserData> withNewGuy = new HashSet<>();
        withNewGuy.add(newGuy);
        withNewGuy.add(u1);
        withNewGuy.add(u2);
        Assertions.assertDoesNotThrow(() -> userDAO.update(u0, newGuy));
        Assertions.assertEquals(withNewGuy, userDAO.getAll());
    }

    @Test
    @Order(5)
    void verify() {
        userDAO.delete(u0);
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.verify(u0));
        Assertions.assertDoesNotThrow(() -> userDAO.verify(u2));
    }

    @Test
    @Order(6)
    void add() {
        userDAO.deleteAll();
        Assertions.assertDoesNotThrow(() -> userDAO.add(u0));
        HashSet<UserData> justOne = new HashSet<>();
        justOne.add(u0);
        Assertions.assertEquals(justOne, userDAO.getAll());
    }

    @Test
    @Order(7)
    void getUser() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(userDAO.getUser(u0.username()), u0), "Threw unexpected exception");
    }

    @Test
    @Order(9)
    void editUserPassword() {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.editUserPassword(u0.username(), "betterP@ssword");
            Assertions.assertEquals(userDAO.getUser(u0.username()).password(), "betterP@ssword");
        }, "Threw unexpected exception");
    }

    @Test
    @Order(10)
    void editUserEmail() {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.editUserEmail(u0.username(), "workemail@company.com");
            Assertions.assertEquals(userDAO.getUser(u0.username()).email(), "workemail@company.com");
        }, "Threw unexpected exception");
    }
}
