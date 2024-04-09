package dataAccessTests.sqlDaoTests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataAccess.*;
import dataAccess.sqlDao.SQLAuthDao;
import dataAccess.sqlDao.SQLGameDao;
import dataAccess.sqlDao.SQLUserDao;
import model.GameData;
import model.GameSummary;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class sqlGameDaoTest extends sqlDataAccessVars {
    private static AuthDao authDao;
    private static GameDao gameDao;
    private static UserDao userDao;

    @BeforeEach
    void setup() {
        try {
            authDao = new SQLAuthDao();
            gameDao = new SQLGameDao();
            userDao = new SQLUserDao();
            DatabaseManager.resetData();
        } catch (DataAccessException e) {
            Assertions.fail("Could not set up database: " + e.getMessage());
        }
        try {
            DatabaseManager.resetData();
            gameDao.add(g0);
            gameDao.add(g1);
            gameDao.add(g2);
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void getAll() {
        try {
            var g = gameDao.getAll();
            HashSet<GameData> allGames = (HashSet<GameData>) gameDao.getAll();
            Assertions.assertEquals(gameData, allGames);
            Assertions.assertDoesNotThrow(DatabaseManager::resetData);
            allGames = (HashSet<GameData>) gameDao.getAll();
            Assertions.assertTrue(allGames.isEmpty());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void delete() {
        try {
            gameDao.delete(g0);
            HashSet<GameData> shortList = new HashSet<>();
            shortList.add(g1);
            shortList.add(g2);
            Assertions.assertEquals(shortList, gameDao.getAll());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    void deleteAll() {
        try {
            gameDao.deleteAll();
            Collection<GameData> allGames = gameDao.getAll();
            Assertions.assertTrue(allGames.isEmpty());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void update() {
        try {
            GameData updated = new GameData(0, "white", "black", "updated", new ChessGame());
            Assertions.assertDoesNotThrow(() -> gameDao.update(g0, updated));
            HashSet<GameData> allGames = new HashSet<>();
            allGames.add(updated);
            allGames.add(g1);
            allGames.add(g2);
            Assertions.assertEquals(allGames, gameDao.getAll());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    void verify() {
        try {
            gameDao.delete(g0);
            Assertions.assertThrows(DataAccessException.class, () -> gameDao.verify(g0));
            Assertions.assertThrows(DataAccessException.class, () -> gameDao.verify(g0.gameID()));
            Assertions.assertDoesNotThrow(() -> gameDao.verify(g1));
            Assertions.assertDoesNotThrow(() -> gameDao.verify(g1.gameID()));
            GameData game = gameDao.verify(g2.gameID());
            Assertions.assertNotNull(game);
            Assertions.assertNotEquals(0, game.gameID());
            Assertions.assertNotNull(game.gameName());
            Assertions.assertNotNull(game.blackUsername());
            Assertions.assertNotNull(game.whiteUsername());
        } catch (DataAccessException e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    @Order(6)
    void add() {
        try {
            gameDao.deleteAll();
            Assertions.assertDoesNotThrow(() -> gameDao.add(g0));
            HashSet<GameData> justOne = new HashSet<>();
            justOne.add(g0);
            Assertions.assertEquals(justOne, gameDao.getAll());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    void getGame() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(gameDao.getGame(g0.gameID()), g0));
    }

    @Test
    void getGameSummaries() {
        try {
            Assertions.assertEquals(gameSummaries, gameDao.getGameSummaries());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void createGame() {
        try {
            int newGameID = gameDao.createGame("exampleGameName");
            Assertions.assertEquals("exampleGameName", gameDao.getGame(newGameID).gameName());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void setGameState() {
        ChessGame newGameState = new ChessGame();
        Assertions.assertDoesNotThrow(() -> newGameState.makeMove(
                new ChessMove(
                        new ChessPosition(2, 1),
                        new ChessPosition(3, 1),
                        null)));
        try {
            GameData dbGame = gameDao.getGame(g1.gameID());
            Assertions.assertNotEquals(newGameState, dbGame.game());
            Assertions.assertDoesNotThrow(() -> gameDao.setGameState(g1.gameID(), newGameState));
            dbGame = gameDao.getGame(g1.gameID());
            Assertions.assertEquals(newGameState, dbGame.game());
        } catch (DataAccessException e) {
            Assertions.fail("Unexpected Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void getGamesByPlayer() {
        try {
            HashSet<GameSummary> games = gameDao.getGamesByPlayer("death");
            Assertions.assertNotNull(games);
            Assertions.assertEquals(2, games.size());
            Assertions.assertFalse(games.contains(s0));
            Assertions.assertTrue(games.contains(s1));
            Assertions.assertTrue(games.contains(s2));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void getGamesByName() {
        try {
            HashSet<GameSummary> games = gameDao.getGamesByName("chessgame");
            Assertions.assertNotNull(games);
            Assertions.assertEquals(2, games.size());
            Assertions.assertFalse(games.contains(s0));
            Assertions.assertTrue(games.contains(s1));
            Assertions.assertTrue(games.contains(s2));
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }
}
