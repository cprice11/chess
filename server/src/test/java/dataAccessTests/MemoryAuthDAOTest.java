package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryDatabase;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemoryAuthDAOTest extends DataAccessVars{
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    @BeforeEach
    void setup() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
    }

    @Test
    @Order(1)
    void getAll() {
        Collection<AuthData> allAuths = authDAO.getAll();
        Assertions.assertEquals(allAuths, authData);
        MemoryDatabase.clearAuth();
        allAuths = authDAO.getAll();
        Assertions.assertTrue(allAuths.isEmpty());
    }

    @Test
    @Order(2)
    void delete() {
        authDAO.delete(a0);
        Collection<AuthData>allAuths = authDAO.getAll();
        Assertions.assertEquals(allAuths, List.of(new AuthData[]{a1, a2}));
    }

    @Test
    @Order(3)
    void deleteAll() {
        authDAO.deleteAll();
        Collection<AuthData> allAuths = authDAO.getAll();
        Assertions.assertTrue(allAuths.isEmpty());
    }

    @Test
    @Order(4)
    void update() {
        authDAO.update(a0, a1);
        Collection<AuthData> allAuths = authDAO.getAll();
        Assertions.assertEquals(allAuths, List.of(new AuthData[]{a1, a1, a2}));
    }

    @Test
    @Order(5)
    void verify() {
        authDAO.update(a0, a1);
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.verify(a0));
        Collection<AuthData>allAuths = authDAO.getAll();
        Assertions.assertEquals(allAuths, List.of(new AuthData[]{a1, a1, a2}));
    }

    @Test
    @Order(6)
    void add() {
        authDAO.deleteAll();
        authDAO.add(a0);
        Assertions.assertEquals(authDAO.getAll(), List.of(new AuthData[]{a1}));
    }

    @Test
    void testVerify() {
    }

    @Test
    void testAdd() {
    }

    @Test
    void getAuthToken() {
    }

    @Test
    void getUsername() {
    }

    @Test
    void createAuth() {
    }
}
