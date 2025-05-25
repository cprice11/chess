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
        AuthData registered_auth;
        try {
            registered_auth = user.registerUser(userC);
            Assertions.assertEquals(registered_auth, authDAO.getAuthByUsername(userC.username()));
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
        } catch ( DataAccessException e ) {
            System.out.println("Successfully threw exception");
        }
    }
}
