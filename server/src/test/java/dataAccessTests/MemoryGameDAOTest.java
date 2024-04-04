package dataAccessTests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryDatabase;
import dataAccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
        HashSet<GameData> shortList = new HashSet<>();
        shortList.add(g1);
        shortList.add(g2);
        Assertions.assertEquals(shortList, gameDAO.getAll());
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
        GameData updated = new GameData(5000, "white", "black", "updated", new ChessGame());
        gameDAO.update(g0, updated);
        HashSet<GameData> allGames = new HashSet<>();
        allGames.add(updated);
        allGames.add(g1);
        allGames.add(g2);
        Assertions.assertEquals(allGames, gameDAO.getAll());
    }

    @Test
    @Order(5)
    void verify() {
        gameDAO.delete(g0);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.verify(g0));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.verify(g0.gameID()));
        Assertions.assertDoesNotThrow(() -> gameDAO.verify(g1));
        Assertions.assertDoesNotThrow(() -> gameDAO.verify(g1.gameID()));
        try {
            GameData game = gameDAO.verify(g2.gameID());
            Assertions.assertNotNull(game);
            Assertions.assertNotEquals(0, game.gameID());
            Assertions.assertNotNull(game.gameName());
            Assertions.assertNotNull(game.blackUsername());
            Assertions.assertNotNull(game.whiteUsername());
        } catch (DataAccessException e) { Assertions.assertNull(e);}
    }

    @Test
    @Order(6)
    void add() {
        gameDAO.deleteAll();
        gameDAO.add(g0);
        HashSet<GameData> justOne = new HashSet<>();
        justOne.add(g0);
        Assertions.assertEquals(justOne, gameDAO.getAll());
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
    void setGameState() {
        try {
            ChessGame newGameState = new ChessGame();
            newGameState.makeMove(
                    new ChessMove(
                            new ChessPosition(2, 1),
                            new ChessPosition(3, 1),
                            null));
            GameData dbGame = gameDAO.getGame(g1.gameID());
            Assertions.assertNotEquals(newGameState, dbGame.game());
            gameDAO.setGameState(g1.gameID(), newGameState);
            dbGame = gameDAO.getGame(g1.gameID());
            Assertions.assertEquals(newGameState, dbGame.game());
        } catch (InvalidMoveException e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    void getGamesByPlayer() {
        HashSet<GameData> games = gameDAO.getGamesByPlayer("death");
        Assertions.assertNotNull(games);
        Assertions.assertEquals(2, games.size());
        Assertions.assertFalse(games.contains(g0));
        Assertions.assertTrue(games.contains(g1));
        Assertions.assertTrue(games.contains(g2));
    }

    @Test
    void getGamesByName() {
        HashSet<GameData> games = gameDAO.getGamesByName("chessgame");
        Assertions.assertNotNull(games);
        Assertions.assertEquals(2, games.size());
        Assertions.assertFalse(games.contains(g0));
        Assertions.assertTrue(games.contains(g1));
        Assertions.assertTrue(games.contains(g2));
    }
}
