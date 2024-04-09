package serviceTests;

import dataAccess.*;
import dataAccess.sqlDao.SQLAuthDao;
import dataAccess.sqlDao.SQLGameDao;
import dataAccess.sqlDao.SQLUserDao;
import model.GameData;
import org.junit.jupiter.api.*;
import server.request.InvalidRequestException;
import server.request.RegisterRequest;
import service.*;

import java.util.Arrays;
import java.util.HashSet;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameServiceTest extends SqlServiceVars {
    private GameService gameService;
    private GameDao games;

    @BeforeEach
    void buildDatabase() {
        try {
            DatabaseManager.configureDatabase();
            DatabaseManager.resetData();
            AuthDao auth = new SQLAuthDao();
            games = new SQLGameDao();
            UserDao users = new SQLUserDao();
            AuthService authService = new AuthService(auth);
            gameService = new GameService(games, authService);
            UserService userService = new UserService(users, authService);
            userService.register(new RegisterRequest(u0.username(), u0.password(), u0.email()));
            userService.register(new RegisterRequest(u1.username(), u1.password(), u1.email()));
            userService.register(new RegisterRequest(u2.username(), u2.password(), u2.email()));
            for (GameData g : gameData) {
                games.add(g);
            }
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    @Order(1)
    void testCreateGame() {
        Assertions.assertDoesNotThrow(() -> gameService.createGame(goodCreateGameRequest));
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.createGame(badCreateGameRequest));
    }

    @Test
    @Order(2)
    void testListGames() {
        try {
            Assertions.assertEquals(listGamesResult, gameService.listGames(goodListGamesRequest), "Didn't return expected summary list.");
            Assertions.assertThrows(UnauthorizedException.class, () -> gameService.listGames(badListGamesRequest));
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGame() {
        try {
            games.add(gEmpty);
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestWhite));
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestNewPlayerBlack));
            Assertions.assertEquals(u1.username(), games.getGame(gEmpty.gameID()).whiteUsername(), "User not posted with correct color");
            Assertions.assertEquals(u2.username(), games.getGame(gEmpty.gameID()).blackUsername(), "User not posted with correct color");
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinFullGame() {
        try {
            games.add(gEmpty);
            Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(goodJoinGameRequestFullGameWhite));
            Assertions.assertEquals(g1, games.getGame(1), "full game changed after player tried to join");
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGameSameColor() {
        try {
            games.add(gEmpty);
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestWhite));
            Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(goodJoinGameRequestNewPlayerWhite));
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).whiteUsername(), u1.username());
            Assertions.assertNull(games.getGame(gEmpty.gameID()).blackUsername());
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGameBlackFirst() {
        try {
            games.add(gEmpty);
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestBlack));
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestNewPlayerWhite));
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).whiteUsername(), u2.username());
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).blackUsername(), u1.username());
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGameNoColor() {
        try {
            games.add(gEmpty);
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestSpectator));
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestNewSpectator));
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestFullGameSpectator));
            GameData gameData = games.getGame(gEmpty.gameID());
            Assertions.assertNull(gameData.whiteUsername(), "set spectator as player");
            Assertions.assertNull(gameData.blackUsername(), "set spectator as player");
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGameSamePlayer() {
        try {
            games.add(gEmpty);
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestWhite));
            Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestSamePlayerBlack));
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).whiteUsername(), u1.username());
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).blackUsername(), u1.username());
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void badJoinGameRequests() {
        try {
            games.add(gEmpty);
            Assertions.assertThrows(UnauthorizedException.class, () -> gameService.joinGame(badJoinGameRequestBadToken));
            Assertions.assertThrows(InvalidRequestException.class, () -> gameService.joinGame(badJoinGameRequestWrongId));
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }


    @Test
    void getGame() {
        try {
            Assertions.assertEquals(g1, gameService.getGame(1), "Didn't return correct game");
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
        Assertions.assertThrows(DataAccessException.class, () -> gameService.getGame(-1));
    }

    @Test
    void getGamesByPlayer() {
        try {
            Assertions.assertEquals(new HashSet<>(Arrays.asList(s1, s2)), gameService.getGamesByPlayer("death"));
            Assertions.assertDoesNotThrow(() -> gameService.getGamesByPlayer("death"));
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void getGamesByName() {
        Assertions.assertDoesNotThrow(() -> gameService.getGamesByName("chessGame"));
    }
}