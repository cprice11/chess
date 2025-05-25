package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class CreateGameTests extends UnitTests{
    @Test
    @Order(1)
    @DisplayName("Positive create game")
    public void createGame() {
        try {
            int aGameID = game.createGame(authA.authToken(), "A's game");
            Assertions.assertNotNull(gameDAO.getGame(aGameID));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception");
        }
    }
    @Test
    @Order(2)
    @DisplayName("Unauthorized create game")
    public void createGameWithoutAuth() {
        int cGameID = -1;
        try {
            cGameID = game.createGame(authC.authToken(), "C's game");
            Assertions.fail("didn't throw unauthorized exception");
        } catch (UnauthorizedException e) {
            Assertions.assertEquals(-1, cGameID);
        } catch (Exception e) {
            Assertions.fail("Unexpected exception");
        }
    }
}
