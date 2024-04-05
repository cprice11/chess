package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryDatabase;
import model.GameData;
import org.junit.jupiter.api.*;
import service.AlreadyTakenException;
import service.AuthService;
import service.GameService;

import java.util.Arrays;
import java.util.HashSet;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameServiceTest extends ServiceVars {
    private static AuthService authService = new AuthService(auth);
    private static final GameService gameService = new GameService(games, authService);

    @BeforeEach
    void buildDatabase() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
        auth = new MemoryAuthDAO();
        authService = new AuthService(auth);
    }

    @Test
    @Order(1)
    void testCreateGame() {
        Assertions.assertDoesNotThrow(() -> gameService.createGame(goodCreateGameRequest));
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(badCreateGameRequest));
    }

    @Test
    @Order(2)
    void testListGames() {
        try {
            Assertions.assertEquals(goodListGamesResult, gameService.listGames(goodListGamesRequest), "Didn't return expected summary list.");
            Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames(badListGamesRequest));
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGame() {
        MemoryDatabase.games.add(gEmpty);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestWhite));
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestNewPlayerBlack));
        try {
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).whiteUsername(), u1.username(), "User not posted with correct color");
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).blackUsername(), u2.username(), "User not posted with correct color");
        } catch (DataAccessException e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinFullGame() {
        MemoryDatabase.games.add(gEmpty);
        Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(goodJoinGameRequestFullGameWhite));
        try {
            Assertions.assertEquals(g1, games.getGame(1), "full game changed after player tried to join");
        } catch (Exception e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGameSameColor() {
        MemoryDatabase.games.add(gEmpty);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestWhite));
        Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(goodJoinGameRequestNewPlayerWhite));
        try {
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).whiteUsername(), u1.username());
            Assertions.assertNull(games.getGame(gEmpty.gameID()).blackUsername());
        } catch (DataAccessException e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGameBlackFirst() {
        MemoryDatabase.games.add(gEmpty);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestBlack));
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestNewPlayerWhite));
        try {
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).whiteUsername(), u2.username());
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).blackUsername(), u1.username());
        } catch (DataAccessException e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGameNoColor() {
        MemoryDatabase.games.add(gEmpty);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestSpectator));
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestNewSpectator));
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestFullGameSpectator));
        try {
            GameData gameData = games.getGame(gEmpty.gameID());
            Assertions.assertNull(gameData.whiteUsername(), "set spectator as player");
            Assertions.assertNull(gameData.blackUsername(), "set spectator as player");
        } catch (DataAccessException e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void testJoinGameSamePlayer() {
        MemoryDatabase.games.add(gEmpty);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestWhite));
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequestSamePlayerBlack));
        try {
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).whiteUsername(), u1.username());
            Assertions.assertEquals(games.getGame(gEmpty.gameID()).blackUsername(), u1.username());
        } catch (DataAccessException e) {
            Assertions.fail("Threw unexpected exception");
        }
    }

    @Test
    void badJoinGameRequests() {
        MemoryDatabase.games.add(gEmpty);
        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame(badJoinGameRequestBadToken));
        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame(badJoinGameRequestWrongId));
    }


    @Test
    void getGame() {
        Assertions.assertDoesNotThrow(() ->
                        Assertions.assertEquals(g1, gameService.getGame(1), "Didn't return correct game"),
                "Threw unexpected Exceptions");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.getGame(-1));
    }

    @Test
    void getGamesByPlayer() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(s1, s2)), gameService.getGamesByPlayer("death"));
        Assertions.assertDoesNotThrow(() -> gameService.getGamesByPlayer("death"));
    }

    @Test
    void getGamesByName() {
        Assertions.assertDoesNotThrow(() -> gameService.getGamesByName("chessgame"));
    }
}