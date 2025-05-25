package service;

import dataModels.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class LogoutTests extends UnitTests{
    @Test
    @Order(1)
    @DisplayName("positive logout")
    public void logoutUser() {
        user.logoutUser(authA.authToken());
        Assertions.assertNull(authDAO.getAuthByUsername(authA.username()));
        Assertions.assertNull(authDAO.getAuthByAuthToken(authA.authToken()));
    }

//    @Test
//    @Order(2)
//    @DisplayName("wrong password")
//    public void registerReusedUsername() {
//        try {
//            user.loginUser(userA.username(), userB.password());
//            Assertions.fail();
//        } catch (Exception e) {
//            System.out.println("Good throw");
//        }
//    }
}
