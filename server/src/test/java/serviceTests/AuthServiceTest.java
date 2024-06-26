package serviceTests;

import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.sqlDao.SQLAuthDao;
import model.AuthData;
import org.junit.jupiter.api.*;
import service.AuthService;
import service.UnauthorizedException;

import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest extends SqlServiceVars {
    private AuthService authService;
    private AuthDao auth;


    @BeforeEach
    void buildDatabase() {
        try {
            auth = new SQLAuthDao();
            DatabaseManager.resetData();
            authService = new AuthService(auth);
            auth.createAuth(a0.username());
            auth.createAuth(a1.username());
            auth.createAuth(a2.username());
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    @Order(0)
    void testVerify() {
        Assertions.assertThrows(UnauthorizedException.class, () -> authService.verify(aNew.authToken()), "Did not throw exception on incorrect token");
        Assertions.assertDoesNotThrow(() -> authService.verify(a0.authToken()), "Threw exception on valid token");
    }

    @Test
    @Order(1)
    void getAuthByUsername() {
        try {
            Assertions.assertTrue(authService.getAuthByUsername(a0.username()).contains(a0), "Did not return expected AuthData");
            Assertions.assertTrue(authService.getAuthByUsername(a1.username()).contains(a1), "Did not return expected AuthData");
            Assertions.assertTrue(authService.getAuthByUsername(a2.username()).contains(a2), "Did not return expected AuthData");
        } catch (Exception e) {
            Assertions.fail("Threw unexpected Exception" + e);
        }
        Assertions.assertThrows(DataAccessException.class, () -> authService.getAuthByUsername(aNew.username()), "Did not throw exception on incorrect username");
    }


    @Test
    @Order(1)
    void getAuthByAuthToken() {
        try {
            Assertions.assertEquals(a0, authService.getAuthByAuthToken(a0.authToken()), "Did not return expected AuthData");
            Assertions.assertEquals(a1, authService.getAuthByAuthToken(a1.authToken()), "Did not return expected AuthData");
            Assertions.assertEquals(a2, authService.getAuthByAuthToken(a2.authToken()), "Did not return expected AuthData");
        } catch (Exception e) {
            Assertions.fail("Threw unexpected Exception" + e);
        }
        Assertions.assertThrows(DataAccessException.class, () -> authService.getAuthByAuthToken(aNew.authToken()), "Did not throw exception on incorrect token");
    }

    @Test
    @Order(2)
    void authNotRemoved() {
        try {
            authService.getAuthByUsername(a0.username());
            authService.getAuthByAuthToken(a0.authToken());
            Assertions.assertTrue(auth.getAll().contains(a0), "Did not find expected AuthData after retrieval");
        } catch (Exception e) {
            Assertions.fail("Threw unexpected Exception" + e);
        }
    }

    @Test
    @Order(3)
    void createAuth() {
        // AuthTokens are pseudo-random and will generate the same tokens in order after every test init.
        try {
            String username = "brand-new-user";
            AuthData newAuth = authService.createAuth(username);
            Assertions.assertNotNull(newAuth.authToken(), "returned null authentication");
            Assertions.assertEquals(t3, newAuth.authToken(), "unexpected token value");
            Assertions.assertTrue(auth.getAll().stream().anyMatch(auth -> Objects.equals(auth.authToken(), newAuth.authToken())));
            Assertions.assertDoesNotThrow(() -> authService.verify(newAuth.authToken()), "threw Exception verifying valid request");
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void createTwoAuths() {
        try {
            String username = "brand-new-user";
            AuthData newAuth = authService.createAuth(username);
            AuthData newAuthTwo = authService.createAuth(username);
            Assertions.assertNotEquals(newAuth, newAuthTwo, "returned identical auth tokens");
            Assertions.assertDoesNotThrow(() -> auth.verify(newAuth.authToken()), "Threw Exception on valid request");
            Assertions.assertDoesNotThrow(() -> auth.verify(newAuthTwo.authToken()), "Threw Exception on valid second request");
            Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuth.authToken(), username)), "first authData found");
            Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuthTwo.authToken(), username)), "second authData found");
            Assertions.assertEquals(t3, newAuth.authToken(), "returned unexpected auth token");
            Assertions.assertEquals(username, newAuth.username(), "returned unexpected username");
            Assertions.assertEquals(t4, newAuthTwo.authToken(), "returned unexpected auth token");
            Assertions.assertEquals(username, newAuthTwo.username(), "returned unexpected username");
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }

}