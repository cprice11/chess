package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryDatabase;
import model.AuthData;
import org.junit.jupiter.api.*;
import service.AuthService;
import service.UnauthorizedException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest extends ServiceVars {
    private static AuthService authService;

    private static final MemoryDatabase db = new MemoryDatabase();


    @BeforeEach
    void buildDatabase() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
        auth = new MemoryAuthDAO();
        authService = new AuthService(auth);
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
        String username = "brand-new-user";
        AuthData newAuth = authService.createAuth(username);
        Assertions.assertNotNull(newAuth.authToken(), "returned null authentication");
        Assertions.assertEquals(t0, newAuth.authToken(), "unexpected token value");
        Assertions.assertNotEquals(a0.authToken(), newAuth.authToken(), "Unexpected token value");
        Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuth.authToken(), username)), "AuthData not found after creation");
        Assertions.assertDoesNotThrow(() -> authService.verify(newAuth.authToken()), "threw Exception verifying valid request");
    }

    @Test
    @Order(4)
    void createTwoAuths() {
        String username = "brand-new-user";
        AuthData newAuth = authService.createAuth(username);
        AuthData newAuthTwo = authService.createAuth(username);
        Assertions.assertNotEquals(newAuth, newAuthTwo, "returned identical auth tokens");
        Assertions.assertDoesNotThrow(() -> auth.verify(newAuth.authToken()), "Threw Exception on valid request");
        Assertions.assertDoesNotThrow(() -> auth.verify(newAuthTwo.authToken()), "Threw Exception on valid second request");
        Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuth.authToken(), username)), "first authData found");
        Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuthTwo.authToken(), username)), "second authData found");
        Assertions.assertEquals(t0, newAuth.authToken(), "returned unexpected auth token");
        Assertions.assertEquals(username, newAuth.username(), "returned unexpected username");
        Assertions.assertEquals(t1, newAuthTwo.authToken(), "returned unexpected auth token");
        Assertions.assertEquals(username, newAuthTwo.username(), "returned unexpected username");
    }

}