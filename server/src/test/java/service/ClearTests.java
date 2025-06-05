package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearTests extends UnitTests {

    @Test
    @Order(1)
    @DisplayName("Positive Clear")
    public void confirmDbCleared() {
        try {
            Assertions.assertEquals(authA, authDAO.getAuthByUsername(authA.username()));
            Assertions.assertEquals(authB, authDAO.getAuthByUsername(authB.username()));
            Assertions.assertEquals(gameA, gameDAO.getGame(gameA.gameID()));
            Assertions.assertEquals(gameB, gameDAO.getGame(gameB.gameID()));
            Assertions.assertEquals(userA, userDAO.getUser(userA.username()));
            Assertions.assertEquals(userB, userDAO.getUser(userB.username()));
            Assertions.assertNotNull(authDAO.getAuthByUsername(authA.username()));
            Assertions.assertNotNull(authDAO.getAuthByUsername(authA.username()));
            Assertions.assertNotNull(gameDAO.getGame(gameA.gameID()));
            Assertions.assertNotNull(gameDAO.getGame(gameB.gameID()));
            Assertions.assertNotNull(userDAO.getUser(userA.username()));
            Assertions.assertNotNull(userDAO.getUser(userB.username()));
            dev.clear();
            Assertions.assertNull(authDAO.getAuthByUsername(authA.username()));
            Assertions.assertNull(authDAO.getAuthByUsername(authA.username()));
            Assertions.assertNull(gameDAO.getGame(gameA.gameID()));
            Assertions.assertNull(gameDAO.getGame(gameB.gameID()));
            Assertions.assertNull(userDAO.getUser(userA.username()));
            Assertions.assertNull(userDAO.getUser(userB.username()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("There was an exception thrown while running a test\n\t%s", e.getMessage()));
        }
    }
}
