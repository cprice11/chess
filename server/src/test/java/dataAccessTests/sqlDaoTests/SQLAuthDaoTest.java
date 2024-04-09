package dataAccessTests.sqlDaoTests;

import dataAccess.*;
import dataAccess.sqlDao.SQLAuthDao;
import dataAccess.sqlDao.SQLGameDao;
import dataAccess.sqlDao.SQLUserDao;
import model.AuthData;
import org.junit.jupiter.api.*;

import java.util.*;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLAuthDaoTest extends sqlDataAccessVars {
    private static AuthDao authDao;
    private static GameDao gamesDao;
    private static UserDao userDao;

    @BeforeEach
    void setup() {
        try {
            authDao = new SQLAuthDao();
            gamesDao = new SQLGameDao();
            userDao = new SQLUserDao();
            DatabaseManager.resetData();
        } catch (DataAccessException e) {
            Assertions.fail("Could not set up database: " + e.getMessage());
        }
        try {
            DatabaseManager.resetData();
            authDao.add(a0);
            authDao.add(a1);
            authDao.add(a2);
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void getAll() {
        try {
            Collection<AuthData> allAuths = authDao.getAll();
            Assertions.assertEquals(authData, allAuths);
            Assertions.assertDoesNotThrow(DatabaseManager::resetData);
            allAuths = authDao.getAll();
            Assertions.assertTrue(allAuths.isEmpty());
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(2)
    void delete() {
        try {
            authDao.delete(a0);
            Collection<AuthData> allAuths = authDao.getAll();
            Collection<AuthData> oneAndTwo = new HashSet<>(Arrays.asList(a1, a2));
            Assertions.assertEquals(oneAndTwo, allAuths);
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    @Order(3)
    void deleteAll() {
        try {
            authDao.deleteAll();
            Collection<AuthData> allAuths = authDao.getAll();
            Assertions.assertTrue(allAuths.isEmpty());
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(4)
    void update() {
        try {
            AuthData updated = new AuthData("updatedToken", "updatedUsername");
            authDao.update(a0, updated);
            Assertions.assertEquals(new HashSet<>(List.of(new AuthData[]{updated, a1, a2})), authDao.getAll());
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(5)
    void verify() {
        Assertions.assertThrows(DataAccessException.class, () -> authDao.update(a0, a1));
        Assertions.assertThrows(DataAccessException.class, () -> authDao.verify(a0));
        Assertions.assertThrows(DataAccessException.class, () -> authDao.verify(a0.authToken()));
        Assertions.assertDoesNotThrow(() -> authDao.verify(a2));
        Assertions.assertDoesNotThrow(() -> authDao.verify(a2.authToken()));

    }

    @Test
    @Order(6)
    void add() {
        try {
            authDao.deleteAll();
            authDao.add(a0);
            HashSet<AuthData> a = new HashSet<>();
            a.add(a0);
            Assertions.assertEquals(authDao.getAll(), a);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getAuthToken() {
        try {
            Assertions.assertEquals(new ArrayList<>(Arrays.asList(a0)), authDao.getAuthFromUser(a0.username()));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown during test. Exception: " + e.getMessage());
        }
    }

    @Test
    void getUsername() {
        try {
            Assertions.assertEquals(a0.username(), authDao.getUsername(a0.authToken()));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown during test. Exception: " + e.getMessage());
        }
    }

    @Test
    void createAuth() {
        try {
            AuthData auth = authDao.createAuth(a0.username());
            Assertions.assertNotNull(auth);
            Assertions.assertNotNull(auth.authToken());
            Assertions.assertEquals(auth.username(), a0.username());
            Assertions.assertDoesNotThrow(() -> authDao.verify(auth.authToken()));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception was thrown during test. Exception: " + e.getMessage());
        }
    }
}