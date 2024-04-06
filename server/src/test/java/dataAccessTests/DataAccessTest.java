package dataAccessTests;

import dataAccess.memoryDao.MemoryDatabase;
import org.junit.jupiter.api.*;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataAccessTest extends DataAccessVars {
    @BeforeAll
    static void reset() {
        MemoryDatabase.clearAll();
    }

    @Test
    @Order(1)
    void isInitiated() {
        Assertions.assertTrue(MemoryDatabase.getAuth().isEmpty());
        Assertions.assertTrue(MemoryDatabase.getGames().isEmpty());
        Assertions.assertTrue(MemoryDatabase.getUsers().isEmpty());
    }

    @Test
    @Order(2)
    void addData() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
        Assertions.assertEquals(MemoryDatabase.getAuth(), authData);
        Assertions.assertEquals(MemoryDatabase.getGames(), gameData);
        Assertions.assertEquals(MemoryDatabase.getUsers(), userData);
    }

    @Test
    @Order(3)
    void removeData() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
        Assertions.assertFalse(MemoryDatabase.getAuth().isEmpty());
        MemoryDatabase.clearAuth();
        Assertions.assertTrue(MemoryDatabase.getAuth().isEmpty());
        Assertions.assertFalse(MemoryDatabase.getGames().isEmpty());
        Assertions.assertFalse(MemoryDatabase.getUsers().isEmpty());
        MemoryDatabase.clearGames();
        Assertions.assertTrue(MemoryDatabase.getAuth().isEmpty());
        Assertions.assertTrue(MemoryDatabase.getGames().isEmpty());
        Assertions.assertFalse(MemoryDatabase.getUsers().isEmpty());
        MemoryDatabase.clearUsers();
        Assertions.assertTrue(MemoryDatabase.getAuth().isEmpty());
        Assertions.assertTrue(MemoryDatabase.getGames().isEmpty());
        Assertions.assertTrue(MemoryDatabase.getUsers().isEmpty());
    }
}