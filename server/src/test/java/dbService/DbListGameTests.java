package dbService;

import datamodels.GameSummary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.UnauthorizedException;

import java.util.ArrayList;
import java.util.Collection;

public class DbListGameTests extends DbUnitTests {
    @Test
    @Order(1)
    @DisplayName("Positive list games")
    public void normalListGames() {
        Collection<GameSummary> gameSummaries = new ArrayList<>();
        gameSummaries.add(new GameSummary(gameA.gameID(), gameA.blackUsername(), gameA.whiteUsername(), gameA.gameName()));
        gameSummaries.add(new GameSummary(gameB.gameID(), gameB.blackUsername(), gameB.whiteUsername(), gameB.gameName()));
        try {
            Assertions.assertEquals(gameSummaries, game.listGames(authA.authToken()));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception");
        }
    }

    @Test
    @Order(2)
    @DisplayName("Unauthorized list games")
    public void unauthorizedListGames() {
        Assertions.assertThrows(UnauthorizedException.class, () -> game.listGames(authC.authToken()));
    }
}
