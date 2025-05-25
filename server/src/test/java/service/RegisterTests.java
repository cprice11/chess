package service;

import dataModels.AuthData;
import dataModels.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class RegisterTests extends UnitTests{
    @Test
    @Order(1)
    @DisplayName("Positive Register")
    public void registerUser() {
        AuthData registeredAuth;
        try {
            registeredAuth = user.registerUser(userC);
            Assertions.assertEquals(registeredAuth, authDAO.getAuthByUsername(userC.username()));
        } catch (Exception e) {
            Assertions.fail();
        }
    }
    @Test
    @Order(2)
    @DisplayName("Re-use username")
    public void registerReusedUsername() {
        UserData reuseUsername = new UserData(userA.username(), "userDpass", "d@email.com");
        try {
            user.registerUser(reuseUsername);
            Assertions.fail();
        } catch ( AlreadyTakenException e ) {
            System.out.println("Successfully threw exception");
        } catch ( Exception e ) {
            Assertions.fail("Threw an unexpected exception");
        }
    }
}
