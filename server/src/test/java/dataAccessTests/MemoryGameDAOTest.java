package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryDatabase;
import dataAccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemoryGameDAOTest extends DataAccessVars {
    MemoryGameDAO gameDAO = new MemoryGameDAO();

    @BeforeEach
    void setup() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
    }

    @Test
    @Order(1)
    void getAll() {
        Collection<GameData> allGames = gameDAO.getAll();
        Assertions.assertEquals(allGames, gameData);
        MemoryDatabase.clearGames();
        allGames = gameDAO.getAll();
        Assertions.assertTrue(allGames.isEmpty());
    }

    @Test
    @Order(2)
    void delete() {
        gameDAO.delete(g0);
        Collection<GameData> allGames = gameDAO.getAll();
        Assertions.assertEquals(allGames, List.of(new GameData[]{g1, g2}));
    }

    @Test
    @Order(3)
    void deleteAll() {
        gameDAO.deleteAll();
        Collection<GameData> allGames = gameDAO.getAll();
        Assertions.assertTrue(allGames.isEmpty());
    }

    @Test
    @Order(4)
    void update() {
        gameDAO.update(g0, g1);
        Collection<GameData> allGames = gameDAO.getAll();
        Assertions.assertEquals(allGames, List.of(new GameData[]{g1, g1, g2}));
    }

    @Test
    @Order(5)
    void verify() {
        gameDAO.update(g0, g1);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.verify(g0));
        Collection<GameData> allGames = gameDAO.getAll();
        Assertions.assertEquals(allGames, List.of(new GameData[]{g1, g1, g2}));
    }

    @Test
    @Order(6)
    void add() {
        gameDAO.deleteAll();
        gameDAO.add(g0);
        Assertions.assertEquals(gameDAO.getAll(), List.of(new GameData[]{g1}));
    }

    @Test
    @Order(7)
    void getGame() {
        Assertions.assertEquals(gameDAO.getGame(g0.gameID()), g0);
    }

    @Test
    void getGameSummaries() {
        Assertions.assertEquals(gameSummaries, gameDAO.getGameSummaries());
    }

    @Test
    void createGame() {
        int newGameID = gameDAO.createGame("exampleGameName");
        Assertions.assertSame("exampleGameName", gameDAO.getGame(newGameID).gameName());
    }

    @Test
    void validate() {
    }

    @Test
    void setGameState() {
    }
}
