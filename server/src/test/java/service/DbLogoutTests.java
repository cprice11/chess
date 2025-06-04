package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class DbLogoutTests extends DbUnitTests {
    @Test
    @Order(1)
    @DisplayName("positive logout")
    public void logoutUser() {
        try {
            user.logoutUser(authA.authToken());
            Assertions.assertNull(authDAO.getAuthByUsername(authA.username()));
            Assertions.assertNull(authDAO.getAuthByAuthToken(authA.authToken()));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception thrown");
        }
    }

    @Test
    @Order(2)
    @DisplayName("double logout")
    public void registerReusedUsername() {
        try {
            user.logoutUser(authA.authToken());
            user.logoutUser(authA.authToken());
            Assertions.fail();
        } catch (Exception e) {
            System.out.println("Good throw");
        }
    }
}
