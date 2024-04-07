package dataAccessTests.sqlDaoTests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.GameState;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDao;
import dataAccess.sqlDao.SQLGameDao;
import model.GameData;
import model.GameSummary;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class sqlGameDaoTest extends sqlDataAccessVars {
    GameDao gameDAO = new SQLGameDao();

    @BeforeAll
    @Order(1)
    static void initialize() {
        try {
            DatabaseManager.configureDatabase();
            DatabaseManager.resetData();
        } catch (DataAccessException e) {
            Assertions.fail("Could not set up database: " + e.getMessage());
        }
    }

    @BeforeEach
    void setup() {
        try {
            DatabaseManager.resetData();
            var games = gameDAO.getAll();
            gameDAO.add(g0);
            gameDAO.add(g1);
            gameDAO.add(g2);
            games = gameDAO.getAll();
                int i = 0;
            for (GameData g : games) {
                var s1 = g.game();
                var s2 = g0.game();
                if (s1.equals(s2)) {
                    i++;
                } else i--;
            }
        } catch (Exception e) {
            Assertions.fail("Unable to setup database for tests. Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void getAll() {
        try {
            var g = gameDAO.getAll();
            HashSet<GameData> allGames = (HashSet<GameData>) gameDAO.getAll();
            Assertions.assertEquals(gameData, allGames);
            Assertions.assertDoesNotThrow(DatabaseManager::resetData);
            allGames = (HashSet<GameData>) gameDAO.getAll();
            Assertions.assertTrue(allGames.isEmpty());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void delete() {
        try {
            gameDAO.delete(g0);
            HashSet<GameData> shortList = new HashSet<>();
            shortList.add(g1);
            shortList.add(g2);
            Assertions.assertEquals(shortList, gameDAO.getAll());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    void deleteAll() {
        try {
            gameDAO.deleteAll();
            Collection<GameData> allGames = gameDAO.getAll();
            Assertions.assertTrue(allGames.isEmpty());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void update() {
        try {
            GameData updated = new GameData(5000, "white", "black", "updated", new ChessGame());
            Assertions.assertDoesNotThrow(() -> gameDAO.update(g0, updated));
            HashSet<GameData> allGames = new HashSet<>();
            allGames.add(updated);
            allGames.add(g1);
            allGames.add(g2);
            Assertions.assertEquals(allGames, gameDAO.getAll());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    void verify() {
        try {
            gameDAO.delete(g0);
            Assertions.assertThrows(DataAccessException.class, () -> gameDAO.verify(g0));
            Assertions.assertThrows(DataAccessException.class, () -> gameDAO.verify(g0.gameID()));
            Assertions.assertDoesNotThrow(() -> gameDAO.verify(g1));
            Assertions.assertDoesNotThrow(() -> gameDAO.verify(g1.gameID()));
            GameData game = gameDAO.verify(g2.gameID());
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
            gameDAO.deleteAll();
            Assertions.assertDoesNotThrow(() -> gameDAO.add(g0));
            HashSet<GameData> justOne = new HashSet<>();
            justOne.add(g0);
            Assertions.assertEquals(justOne, gameDAO.getAll());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    void getGame() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(gameDAO.getGame(g0.gameID()), g0));
    }

    @Test
    void getGameSummaries() {
        try {
            Assertions.assertEquals(gameSummaries, gameDAO.getGameSummaries());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void createGame() {
        try {
            int newGameID = gameDAO.createGame("exampleGameName");
            Assertions.assertDoesNotThrow(() -> Assertions.assertSame("exampleGameName", gameDAO.getGame(newGameID).gameName())
            );
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
        Assertions.assertDoesNotThrow(() -> {
            GameData dbGame = gameDAO.getGame(g1.gameID());
            Assertions.assertNotEquals(newGameState, dbGame.game());
            Assertions.assertDoesNotThrow(() -> gameDAO.setGameState(g1.gameID(), newGameState));
            dbGame = gameDAO.getGame(g1.gameID());
            Assertions.assertEquals(newGameState, dbGame.game());
        });
    }

    @Test
    void getGamesByPlayer() {
        try {
            HashSet<GameSummary> games = gameDAO.getGamesByPlayer("death");
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
            HashSet<GameSummary> games = gameDAO.getGamesByName("chessgame");
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
