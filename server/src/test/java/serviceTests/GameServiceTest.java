package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryDatabase;
import org.junit.jupiter.api.*;
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
    void testJoinGame() {
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(goodJoinGameRequest));
        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame(badJoinGameRequestBadToken));
    }


    @Test
    void testGetGames() {
        Assertions.assertDoesNotThrow(() ->
                        Assertions.assertEquals(gameSummaries, gameService.listGames(goodListGamesRequest)),
                "Threw unexpected exception");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames(badListGamesRequest));
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