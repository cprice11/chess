package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryDatabase;
import model.UserData;
import org.junit.jupiter.api.*;
import server.request.LogoutRequest;
import server.result.LoginResult;
import service.AuthService;
import service.UserService;

import java.util.stream.IntStream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest extends ServiceVars {

    private static final UserService userService = new UserService(users);
    private static final AuthService authService = new AuthService(auth);
    private static final MemoryDatabase db = new MemoryDatabase();


    @BeforeEach
    void buildDatabase() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
    }


    @Test
    void testRegister() {
        Assertions.assertDoesNotThrow(() -> userService.register(goodRegisterRequest), "Threw exception on valid register request");
        Assertions.assertTrue(users.getAll().contains(uNew));
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(badRegisterRequest));
        Assertions.assertFalse(users.getAll().contains(
                new UserData(
                        badRegisterRequest.username(),
                        badRegisterRequest.password(),
                        badRegisterRequest.email())
                )
        );
    }

//    @Test
//    void getUserByUsername() {
//    }
//
//    @Test
//    void getUserByEmail() {
//    }

    @Test
    @Order(5)
    void testLogin() {
        auth.deleteAll();
        Assertions.assertDoesNotThrow(() -> userService.login(goodLoginRequest), "Threw Exception on valid request");
        LoginResult result = userService.login(goodLoginRequest);
        Assertions.assertEquals(t1, result.authToken(), "Returned unexpected auth token");
        Assertions.assertEquals(goodLoginResult.username(), result.username(), "Returned unexpected username");
        Assertions.assertTrue(auth.getAll().contains(aNew), "new auth not found in database after request");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(badLoginRequest), "No Exception thrown on invalid request");
    }

    @Test
    @Order(6)
    void testLogout() {
        userService.logout(goodLogoutRequest);
        Assertions.assertFalse(auth.getAll().contains(a0), "AuthData remained after logout");
        Assertions.assertTrue(auth.getAll().contains(a1), "incorrect AuthData dropped after logout");
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout(badLogoutRequest), "No error thrown on invalid request");
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
        LoginResult firstResult = userService.login(goodLoginRequest);
        LoginResult secondResult = userService.login(goodLoginRequest);
        Assertions.assertEquals(t0, firstResult.authToken(), "Unexpected authToken from login");
        Assertions.assertEquals(t1, secondResult.authToken(), "Unexpected authToken from login");
        Assertions.assertDoesNotThrow(() -> auth.verify(t0), "Exception thrown on valid login request");
        Assertions.assertDoesNotThrow(() -> auth.verify(t1), "Exception thrown on valid login request");
        Assertions.assertDoesNotThrow(() -> userService.logout(new LogoutRequest(firstResult.authToken())), "Threw error on valid logout request");
        Assertions.assertThrows(DataAccessException.class, () -> authService.verify(t0), "Verified invalid authToken");
        Assertions.assertThrows(DataAccessException.class, () -> authService.verify(goodLoginRequest.username()), "Verified logged out user");
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout(new LogoutRequest(secondResult.authToken())), "Verified invalid logout request");
    }

    @Test
    @Order(8)
    void testReverseMultipleLogin() {
        auth.deleteAll();
        LoginResult firstResult = userService.login(goodLoginRequest);
        LoginResult secondResult = userService.login(goodLoginRequest);
        Assertions.assertEquals(t0, firstResult.authToken(), "Unexpected authToken from login");
        Assertions.assertEquals(t1, secondResult.authToken(), "Unexpected authToken from login");
        Assertions.assertDoesNotThrow(() -> auth.verify(t0), "Exception thrown on valid login request");
        Assertions.assertDoesNotThrow(() -> auth.verify(t1), "Exception thrown on valid login request");
        Assertions.assertDoesNotThrow(() -> userService.logout(new LogoutRequest(secondResult.authToken())), "Threw error on valid logout request");
        Assertions.assertThrows(DataAccessException.class, () -> authService.verify(t0), "Verified invalid authToken");
        Assertions.assertThrows(DataAccessException.class, () -> authService.verify(goodLoginRequest.username()), "Verified logged out user");
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout(new LogoutRequest(firstResult.authToken())), "Verified invalid logout request");
    }
}