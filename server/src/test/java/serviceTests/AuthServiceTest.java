package serviceTests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import service.AuthService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {
    private final static String username = "exampleUsername37";

    @Test
    @Order(1)
    void getAuthByUsername() {
    }

    @Test
    @Order(2)
    void getAuthByAuthToken() {
    }

    @Test
    @Order(3)
    void testCreateAuth() {
        AuthService service = new AuthService();
        service.createAuth(username);
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