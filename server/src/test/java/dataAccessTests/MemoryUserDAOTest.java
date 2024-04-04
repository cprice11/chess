package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryDatabase;
import dataAccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemoryUserDAOTest extends DataAccessVars {
    MemoryUserDAO userDAO = new MemoryUserDAO();

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
        Collection<UserData> allUsers = userDAO.getAll();
        Assertions.assertEquals(allUsers, List.of(new UserData[]{u1, u2}));
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
        userDAO.update(u0, u1);
        Collection<UserData> allUsers = userDAO.getAll();
        Assertions.assertEquals(allUsers, List.of(new UserData[]{u1, u1, u2}));
    }

    @Test
    @Order(5)
    void verify() {
        userDAO.update(u0, u1);
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.verify(u0));
        Collection<UserData> allUsers = userDAO.getAll();
        Assertions.assertEquals(allUsers, List.of(new UserData[]{u1, u1, u2}));
    }

    @Test
    @Order(6)
    void add() {
        userDAO.deleteAll();
        userDAO.add(u0);
        Assertions.assertEquals(userDAO.getAll(), List.of(new UserData[]{u1}));
    }

    @Test
    @Order(7)
    void getUser() {
        Assertions.assertEquals(userDAO.getUser(u0.username()), u0);
    }

    @Test
    @Order(8)
    void editUserUsername() {
        userDAO.editUserUsername(u0, "max");
        Assertions.assertEquals(userDAO.getUser("max").password(), u0.password());
    }

    @Test
    @Order(9)
    void editUserPassword() {
        userDAO.editUserPassword(u0, "betterP@ssword");
        Assertions.assertEquals(userDAO.getUser(u0.username()).password(), "betterP@ssword");
    }

    @Test
    @Order(10)
    void editUserEmail() {
        userDAO.editUserEmail(u0, "workemail@company.com");
        Assertions.assertEquals(userDAO.getUser(u0.username()).email(), "workemail@company.com");
    }
}
