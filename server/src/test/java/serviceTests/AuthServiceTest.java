package serviceTests;

import dataAccess.*;
import model.AuthData;
import org.eclipse.jetty.client.api.Authentication;
import org.junit.jupiter.api.*;
import server.Authorization;
import server.LoginRequest;
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
    @Order(1)
    void getAuthByUsername() {
        Assertions.assertEquals(a0, authService.getAuthByUsername(a0.username()), "Did not return expected AuthData");
        Assertions.assertEquals(a1, authService.getAuthByUsername(a1.username()), "Did not return expected AuthData");
        Assertions.assertEquals(a2, authService.getAuthByUsername(a2.username()), "Did not return expected AuthData");
    }


    @Test
    @Order(1)
    void getAuthByAuthToken() {
        Assertions.assertEquals(a0, authService.getAuthByAuthToken(a0.authToken()), "Did not return expected AuthData");
        Assertions.assertEquals(a1, authService.getAuthByAuthToken(a1.authToken()), "Did not return expected AuthData");
        Assertions.assertEquals(a2, authService.getAuthByAuthToken(a2.authToken()), "Did not return expected AuthData");
    }
    @Test
    @Order(2)
    void authNotRemoved() {
        authService.getAuthByUsername(a0.username());
        authService.getAuthByAuthToken(a0.authToken());
        Assertions.assertTrue(auth.getAll().contains(a0));
    }

    @Test
    @Order(3)
    void testCreateAuth() {
        // AuthTokens are pseudo-random and will generate the same tokens in order after every test init.
        AuthData newAuth = authService.createAuth("brand-new-user");
        Assertions.assertNotNull(newAuth.authToken());
        Assertions.assertNotEquals(a0.authToken(), newAuth.authToken());
        Assertions.assertDoesNotThrow(() -> auth.verify(newAuth.authToken()));
        Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuth.authToken(), "brand-new-user")));
    }

    @Test
    @Order(4)
    void CreateTwoAuths() {
        String username = "brand-new-user";
        AuthData newAuth = authService.createAuth(username);
        AuthData newAuthTwo = authService.createAuth(username);
        Assertions.assertNotEquals(newAuth, newAuthTwo);
        Assertions.assertDoesNotThrow(() -> auth.verify(newAuth.authToken()));
        Assertions.assertDoesNotThrow(() -> auth.verify(newAuthTwo.authToken()));
        Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuth.authToken(), "brand-new-user")));
        Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuthTwo.authToken(), "brand-new-user")));
        Assertions.assertEquals(t0, newAuth.authToken());
        Assertions.assertEquals(username, newAuth.username());
        Assertions.assertEquals(t1, newAuthTwo.authToken());
        Assertions.assertEquals(username, newAuthTwo.username());
    }

    @Test
    @Order(5)
    void testLogin() {
        LoginResult result = authService.login(goodLoginRequest);
        Assertions.assertEquals(t0, result.authToken());
        Assertions.assertEquals(goodLoginResult.username(), result.username());

//        goodLoginRequest = new LoginRequest(u0.username(), u0.password());
//        badLoginRequest = new LoginRequest(u0.username(), u2.password());
//        goodLoginResult = new LoginResult(u0.username(), null); // authToken shouldn't be created here
//    static final LoginResult badLoginResult = new LoginResult();
    }

    @Test
    @Order(6)
    void testLogout() {
        authService.logout(goodLogoutRequest);
        Assertions.assertFalse(auth.getAll().contains(a0));
        Assertions.assertFalse(auth.getAll().contains(a1));
    }

    @Test
    @Order(7)
    void testMultipleLogin() {
        LoginResult firstResult = authService.login(goodLoginRequest);
        LoginResult secondResult = authService.login(goodLoginRequest);
        Assertions.assertEquals(t0, firstResult.authToken());
        Assertions.assertEquals(t1, secondResult.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> auth.verify(t0));
        Assertions.assertDoesNotThrow(() -> auth.verify(t1));
    }

    @Test
    void testVerify() {
    }


}