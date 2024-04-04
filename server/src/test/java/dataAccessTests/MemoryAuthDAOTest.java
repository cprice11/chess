package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryDatabase;
import model.AuthData;
import org.junit.jupiter.api.*;

import java.util.*;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemoryAuthDAOTest extends DataAccessVars {
    MemoryDatabase db = new MemoryDatabase();
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
        HashSet<AuthData> allAuths = authDAO.getAll();
        HashSet<AuthData> oneAndTwo = new HashSet<>(Arrays.asList(a1, a2));
        Assertions.assertEquals(oneAndTwo, allAuths);
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
        AuthData updated = new AuthData("updatedToken", "updatedUsername");
        authDAO.update(a0, updated);
        Assertions.assertEquals(new HashSet<>(List.of(new AuthData[]{updated, a1, a2})), authDAO.getAll());
    }

    @Test
    @Order(5)
    void verify() {
        authDAO.update(a0, a1);
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.verify(a0));
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.verify(a0.authToken()));
        Assertions.assertDoesNotThrow(() -> authDAO.verify(a2));
        Assertions.assertDoesNotThrow(() -> authDAO.verify(a2.authToken()));
    }

    @Test
    @Order(6)
    void add() {
        authDAO.deleteAll();
        authDAO.add(a0);
        HashSet<AuthData> a = new HashSet<>();
        a.add(a0);
        Assertions.assertEquals(authDAO.getAll(), a);
    }

    @Test
    void getAuthToken() {
        Assertions.assertEquals(a0.authToken(), authDAO.getAuthToken(a0.username()));
    }

    @Test
    void getUsername() {
        Assertions.assertEquals(a0.username(), authDAO.getUsername(a0.authToken()));
    }

    @Test
    void createAuth() {
        AuthData auth = authDAO.createAuth(a0.username());
        Assertions.assertNotNull(auth);
        Assertions.assertNotNull(auth.authToken());
        Assertions.assertEquals(auth.username(), a0.username());
        Assertions.assertDoesNotThrow(() -> authDAO.verify(auth.authToken()));
    }
}