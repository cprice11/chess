package serviceTests;

import dataAccess.*;
import model.AuthData;
import org.eclipse.jetty.client.api.Authentication;
import org.junit.jupiter.api.*;
import server.Authorization;
import service.AuthService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest extends ServiceVars {
    private static final AuthDAO auth = new MemoryAuthDAO();
    private static final GameDAO games = new MemoryGameDAO();
    private static final UserDAO users = new MemoryUserDAO();
    private static final AuthService authService = new AuthService();
    private static final MemoryDatabase db = new MemoryDatabase();

    @BeforeEach
    void buildDatabase() {
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
        AuthService service = new AuthService();
        Authorization newAuth = service.createAuth("brand-new-user");
        Assertions.assertNotNull(newAuth.authorizationToken());
        Assertions.assertNotEquals(a0.authToken(), newAuth.authorizationToken());
        Assertions.assertDoesNotThrow(() -> auth.verify(newAuth.authorizationToken()));
        Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuth.authorizationToken(), "brand-new-user")));
    }

    @Test
    @Order(4)
    void CreateTwoAuths() {
        AuthService service = new AuthService();
        Authorization newAuth = service.createAuth("brand-new-user");
        Authorization newAuthTwo = service.createAuth("brand-new-user");
        Assertions.assertNotEquals(newAuth, newAuthTwo);
        Assertions.assertDoesNotThrow(() -> auth.verify(newAuth.authorizationToken()));
        Assertions.assertDoesNotThrow(() -> auth.verify(newAuthTwo.authorizationToken()));
        Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuth.authorizationToken(), "brand-new-user")));
        Assertions.assertTrue(auth.getAll().contains(new AuthData(newAuthTwo.authorizationToken(), "brand-new-user")));
    }

    @Test
    void testLogin() {
    }

    @Test
    void testLogout() {
    }

    @Test
    void testVerify() {
    }


}