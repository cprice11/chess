package serviceTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryDatabase;
import org.junit.jupiter.api.*;
import service.AuthService;
import service.GameService;
import service.UserService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameServiceTest extends ServiceVars{
    private static AuthService authService = new AuthService(auth);
    private static UserService userService = new UserService(users, authService);
    private static GameService gameService = new GameService();

    @BeforeEach
    void buildDatabase() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
        auth = new MemoryAuthDAO();
        authService = new AuthService(auth);
        userService = new UserService(users, authService);
    }

    @Test
    @Order(1)
    void testCreateGame() {
        gameService.createGame(goodCreateGameRequest)
    }

    @Test
    void testJoinGame() {

    }


    @Test
    void testGetGames() {
    }

    @Test
    void getGame() {
    }

    @Test
    void getGamesByPlayer() {
    }

    @Test
    void getGamesByName() {
    }
}