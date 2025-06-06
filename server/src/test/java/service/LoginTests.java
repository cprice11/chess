package service;

import datamodels.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class LoginTests extends UnitTests{
    @Test
    @Order(1)
    @DisplayName("Positive login")
    public void loginUser() {
        try {
            AuthData register = user.registerUser(userC);
            user.logoutUser(register.authToken());
            AuthData goodAuth = user.loginUser(userC.username(), userC.password());
            Assertions.assertEquals(goodAuth, authDAO.getAuthByUsername(goodAuth.username()));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    @Order(2)
    @DisplayName("wrong password")
    public void registerReusedUsername() {
        try {
            user.loginUser(userA.username(), userB.password());
            Assertions.fail();
        } catch (Exception e) {
            System.out.println("Good throw");
        }
    }
}
