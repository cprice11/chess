package serviceTests;

import dataAccess.AuthDao;
import dataAccess.DatabaseManager;
import dataAccess.UserDao;
import dataAccess.sqlDao.SQLAuthDao;
import dataAccess.sqlDao.SQLUserDao;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.request.LogoutRequest;
import server.request.RegisterRequest;
import server.result.LoginResult;
import service.AlreadyTakenException;
import service.AuthService;
import service.UnauthorizedException;
import service.UserService;

import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest extends SqlServiceVars {
    private AuthService authService;
    private UserService userService;
    private AuthDao auth;
    private UserDao users;


    @BeforeEach
    void buildDatabase() {
        try {
            DatabaseManager.configureDatabase();
            DatabaseManager.resetData();
            auth = new SQLAuthDao();
            users = new SQLUserDao();
            authService = new AuthService(auth);
            userService = new UserService(users, authService);
            userService.register(new RegisterRequest(u0.username(), u0.password(), u0.email()));
            userService.register(new RegisterRequest(u1.username(), u1.password(), u1.email()));
            userService.register(new RegisterRequest(u2.username(), u2.password(), u2.email()));
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }


    @Test
    @Order(4)
    void testRegister() {
        try {
            Assertions.assertDoesNotThrow(() -> userService.register(goodRegisterRequest), "Threw exception on valid register request");
            Assertions.assertTrue(users.getAll().stream().anyMatch(user -> Objects.equals(user.username(), uNew.username())));
            Assertions.assertThrows(AlreadyTakenException.class, () -> userService.register(badRegisterRequest));
            Assertions.assertFalse(users.getAll().stream().anyMatch(user ->
                    user.email().equals(uNew.email()) && user.username().equals(u2.username())
            ));
        } catch (Exception e) {
            Assertions.fail("Failed due to an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    void testLogin() {
        try {
            auth.deleteAll();
            LoginResult result = userService.login(goodLoginRequest);
            Assertions.assertEquals(t3, result.authToken(), "Returned unexpected auth token");
            Assertions.assertEquals(loginResult.username(), result.username(), "Returned unexpected username");
            Assertions.assertTrue(auth.getAll().contains(new AuthData(result.authToken(), result.username())), "new auth not found in database after request");
        } catch (Exception e) {
            Assertions.fail("Threw Unexpected Exception: " + e.getMessage());
        }
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(badLoginRequest), "No Exception thrown on invalid request");
    }

    @Test
    @Order(6)
    void testLogout() {
        try {
            userService.logout(goodLogoutRequest);
            Assertions.assertFalse(auth.getAll().contains(a0), "AuthData remained after logout");
            Assertions.assertTrue(auth.getAll().contains(a1), "incorrect AuthData dropped after logout");
            Assertions.assertThrows(UnauthorizedException.class, () -> userService.logout(badLogoutRequest), "No error thrown on invalid request");
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    void testMultipleLogin() {
        // One login one token.
        // According to tests, one logout deletes one AuthData.
        try {
            auth.deleteAll();
            LoginResult firstResult = userService.login(goodLoginRequest);
            LoginResult secondResult = userService.login(goodLoginRequest);
            Assertions.assertEquals(t3, firstResult.authToken(), "Unexpected authToken from login");
            Assertions.assertEquals(t4, secondResult.authToken(), "Unexpected authToken from login");
            Assertions.assertDoesNotThrow(() -> auth.verify(firstResult.authToken()), "Exception thrown on valid login request");
            Assertions.assertDoesNotThrow(() -> auth.verify(secondResult.authToken()), "Exception thrown on valid login request");
            Assertions.assertDoesNotThrow(() -> userService.logout(new LogoutRequest(firstResult.authToken())), "Threw error on valid logout request");
            Assertions.assertThrows(UnauthorizedException.class, () -> authService.verify(firstResult.authToken()), "Verified invalid authentication");
            Assertions.assertDoesNotThrow(() -> authService.getAuthByUsername(goodLoginRequest.username()), "No auth for logged in user");
            Assertions.assertDoesNotThrow(() -> userService.logout(new LogoutRequest(secondResult.authToken())), "Rejected valid logout request");
        } catch (Exception e) {
            Assertions.fail("Threw Unexpected Exception");
        }
    }

    @Test
    @Order(8)
    void testReverseMultipleLogin() {
        try {
            auth.deleteAll();
            LoginResult firstResult = userService.login(goodLoginRequest);
            LoginResult secondResult = userService.login(goodLoginRequest);
            Assertions.assertEquals(t3, firstResult.authToken(), "Unexpected authentication from login");
            Assertions.assertEquals(t4, secondResult.authToken(), "Unexpected authentication from login");
            Assertions.assertDoesNotThrow(() -> auth.verify(firstResult.authToken()), "Exception thrown on valid login request");
            Assertions.assertDoesNotThrow(() -> auth.verify(secondResult.authToken()), "Exception thrown on valid login request");
            Assertions.assertDoesNotThrow(() -> userService.logout(new LogoutRequest(secondResult.authToken())), "Threw error on valid logout request");
            Assertions.assertThrows(UnauthorizedException.class, () -> authService.verify(secondResult.authToken()), "Verified invalid authentication");
            Assertions.assertDoesNotThrow(() -> authService.getAuthByUsername(goodLoginRequest.username()), "No auth for logged in user");
            Assertions.assertDoesNotThrow(() -> userService.logout(new LogoutRequest(firstResult.authToken())), "Rejected valid logout request");
        } catch (Exception e) {
            Assertions.fail("Threw Unexpected Exception");
        }
    }
}