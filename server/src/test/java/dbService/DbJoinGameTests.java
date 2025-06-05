package dbService;

import chess.ChessGame;
import datamodels.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.AlreadyTakenException;
import service.UnauthorizedException;

public class DbJoinGameTests extends DbUnitTests {
    @Test
    @Order(1)
    @DisplayName("Positive join game")
    public void fullJoinGame() {
        try {
            int newGame = game.createGame(authA.authToken(), "As game");
            game.joinGame(authA.authToken(), ChessGame.TeamColor.WHITE, newGame);
            game.joinGame(authB.authToken(), ChessGame.TeamColor.BLACK, newGame);
            GameData joinedGame = gameDAO.getGame(newGame);
            Assertions.assertEquals(authA.username(), joinedGame.whiteUsername());
            Assertions.assertEquals(authB.username(), joinedGame.blackUsername());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception");
        }
    }

    @Test
    @Order(2)
    @DisplayName("Unauthorized join game")
    public void joinGameUnauthorized() {
        try {
            int newGame = game.createGame(authA.authToken(), "A's game");
            Assertions.assertThrows(UnauthorizedException.class, () -> game.joinGame(authC.authToken(), ChessGame.TeamColor.WHITE, newGame));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception");
        }
    }

    @Test
    @Order(3)
    @DisplayName("games have different IDs")
    public void multipleGames() {
        try {
            int firstGame = game.createGame(authA.authToken(), "A's first game");
            int secondGame = game.createGame(authA.authToken(), "A's second game");
            Assertions.assertNotEquals(firstGame, secondGame);
        } catch (Exception e) {
            Assertions.fail("Unexpected exception");
        }
    }

    @Test
    @Order(4)
    @DisplayName("join full game")
    public void joinFull() {
        try {
            int newGame = game.createGame(authA.authToken(), "game");
            game.joinGame(authA.authToken(), ChessGame.TeamColor.WHITE, newGame);
            game.joinGame(authB.authToken(), ChessGame.TeamColor.BLACK, newGame);
            Assertions.assertThrows(AlreadyTakenException.class, () -> game.joinGame(authB.authToken(), ChessGame.TeamColor.WHITE, newGame));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception");
        }
    }
}
