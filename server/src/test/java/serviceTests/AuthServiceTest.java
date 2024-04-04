package serviceTests;

import dataAccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.LoginResult;
import server.LogoutRequest;
import service.AuthService;

import java.util.stream.IntStream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest extends ServiceVars {
    private AuthDAO auth = new MemoryAuthDAO();
    private static final GameDAO games = new MemoryGameDAO();
    private static final UserDAO users = new MemoryUserDAO();
    private static final AuthService authService = new AuthService();
    private static final MemoryDatabase db = new MemoryDatabase();


    @BeforeEach
    void buildDatabase() {
        IntStream.range(0, 10).forEach(n -> System.out.println("Auth Token = " + auth.createAuth("").authToken()));
        IntStream.range(0, 10).forEach(n -> System.out.println("Auth Token = " + games.createGame("")));
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
    }

    @Test
    @Order(0)
    void testVerify() {
        Assertions.assertThrows(DataAccessException.class, () -> authService.verify(aNew.authToken()), "Did not throw exception on incorrect token");
        Assertions.assertDoesNotThrow(() -> authService.verify(a0.authToken()), "Threw exception on valid token");
    }

    @Test
    @Order(1)
    void getAuthByUsername() {
        Assertions.assertEquals(a0, authService.getAuthByUsername(a0.username()), "Did not return expected AuthData");
        Assertions.assertEquals(a1, authService.getAuthByUsername(a1.username()), "Did not return expected AuthData");
        Assertions.assertEquals(a2, authService.getAuthByUsername(a2.username()), "Did not return expected AuthData");
        Assertions.assertThrows(DataAccessException.class, () -> authService.getAuthByUsername(a2.username()), "Did not throw exception on incorrect username");
    }


    @Test
    @Order(1)
    void getAuthByAuthToken() {
        Assertions.assertEquals(a0, authService.getAuthByAuthToken(a0.authToken()), "Did not return expected AuthData");
        Assertions.assertEquals(a1, authService.getAuthByAuthToken(a1.authToken()), "Did not return expected AuthData");
        Assertions.assertEquals(a2, authService.getAuthByAuthToken(a2.authToken()), "Did not return expected AuthData");
        Assertions.assertThrows(DataAccessException.class, () -> authService.getAuthByUsername(a2.username()), "Did not throw exception on incorrect token");
    }

    @Test
    @Order(2)
    void authNotRemoved() {
        authService.getAuthByUsername(a0.username());
        authService.getAuthByAuthToken(a0.authToken());
        Assertions.assertTrue(auth.getAll().contains(a0), "Did not find expected AuthData after retrieval");
    }

    @Test
    @Order(3)
    void createAuth() {
        // AuthTokens are pseudo-random and will generate the same tokens in order after every test init.
        String username = "brand-new-user";
        AuthData newAuth = authService.createAuth(username);
        Assertions.assertNotNull(newAuth.authToken(), "returned null authToken");
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

    @Test
    @Order(5)
    void testLogin() {
        auth.deleteAll();
        Assertions.assertDoesNotThrow(() -> authService.login(goodLoginRequest), "Threw Exception on valid request");
        LoginResult result = authService.login(goodLoginRequest);
        Assertions.assertEquals(t1, result.authToken(), "Returned unexpected auth token");
        Assertions.assertEquals(goodLoginResult.username(), result.username(), "Returned unexpected username");
        Assertions.assertTrue(auth.getAll().contains(aNew), "new auth not found in database after request");
        Assertions.assertThrows(DataAccessException.class, () -> authService.login(badLoginRequest), "No Exception thrown on invalid request");
    }

    @Test
    @Order(6)
    void testLogout() {
        authService.logout(goodLogoutRequest);
        Assertions.assertFalse(auth.getAll().contains(a0), "AuthData remained after logout");
        Assertions.assertTrue(auth.getAll().contains(a1), "incorrect AuthData dropped after logout");
        Assertions.assertThrows(DataAccessException.class, () -> authService.logout(badLogoutRequest), "No error thrown on invalid request");
    }

    @Test
    @Order(7)
    void testMultipleLogin() {
        // I'm going to assume that multiple auths is ok, but one logout clears everything?
        // If multiple logins are received, and the client sends either authToken both should work
        // If a user logs out, but still has a live AuthData, that one may not ever get cleared if the client doesn't
        // have the authToken.
        // clearing both would prevent password sharing between multiple clients.
        // IRL Auth would have a lifetime and be connected to a physical device.
        auth.deleteAll();
        LoginResult firstResult = authService.login(goodLoginRequest);
        LoginResult secondResult = authService.login(goodLoginRequest);
        Assertions.assertEquals(t0, firstResult.authToken(), "Unexpected authToken from login");
        Assertions.assertEquals(t1, secondResult.authToken(), "Unexpected authToken from login");
        Assertions.assertDoesNotThrow(() -> auth.verify(t0), "Exception thrown on valid login request");
        Assertions.assertDoesNotThrow(() -> auth.verify(t1), "Exception thrown on valid login request");
        Assertions.assertDoesNotThrow(() -> authService.logout(new LogoutRequest(firstResult.authToken())), "Threw error on valid logout request");
        Assertions.assertThrows(DataAccessException.class, () -> authService.verify(t0), "Verified invalid authToken");
        Assertions.assertThrows(DataAccessException.class, () -> authService.verify(goodLoginRequest.username()), "Verified logged out user");
        Assertions.assertThrows(DataAccessException.class, () -> authService.logout(new LogoutRequest(secondResult.authToken())), "Verified invalid logout request");
    }

    @Test
    @Order(8)
    void testReverseMultipleLogin() {
        auth.deleteAll();
        LoginResult firstResult = authService.login(goodLoginRequest);
        LoginResult secondResult = authService.login(goodLoginRequest);
        Assertions.assertEquals(t0, firstResult.authToken(), "Unexpected authToken from login");
        Assertions.assertEquals(t1, secondResult.authToken(), "Unexpected authToken from login");
        Assertions.assertDoesNotThrow(() -> auth.verify(t0), "Exception thrown on valid login request");
        Assertions.assertDoesNotThrow(() -> auth.verify(t1), "Exception thrown on valid login request");
        Assertions.assertDoesNotThrow(() -> authService.logout(new LogoutRequest(secondResult.authToken())), "Threw error on valid logout request");
        Assertions.assertThrows(DataAccessException.class, () -> authService.verify(t0), "Verified invalid authToken");
        Assertions.assertThrows(DataAccessException.class, () -> authService.verify(goodLoginRequest.username()), "Verified logged out user");
        Assertions.assertThrows(DataAccessException.class, () -> authService.logout(new LogoutRequest(firstResult.authToken())), "Verified invalid logout request");
    }
}