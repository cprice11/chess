package dao;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import datamodels.GameData;
import datamodels.GameSummary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

public class GameTests extends DbUnitTests {
    // ClearAll
    @Test
    @DisplayName("Clear all games")
    public void clearGame() {
        addTwoGames();
        try {
            gameDAO.clearAll();
            Assertions.assertNull(gameDAO.getGame(gameA.gameID()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    // AddGame
    @Test
    @DisplayName("Add and get")
    public void addAndRetrieveSuccessfully() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(gameA.game(), ChessGame.class);
            ChessGame backToGame = gson.fromJson(json, ChessGame.class);
            Assertions.assertNull(gameDAO.getGame(gameA.gameID()));
            gameDAO.addGame(gameA);
            Assertions.assertEquals(gameA, gameDAO.getGame(gameA.gameID()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("Add twice")
    public void addDuplicateGame() {
        try {
            Assertions.assertNull(gameDAO.getGame(gameA.gameID()));
            gameDAO.addGame(gameA);
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.addGame(gameA);
        });
    }

    @Test
    @DisplayName("Add malformed")
    public void addMalformedGame() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.addGame(null);
        });
    }

    // getGame
    @Test
    @DisplayName("Get game by id")
    public void goodGetGameCalls() {
        addTwoGames();
        try {
            Assertions.assertEquals(gameA, gameDAO.getGame(gameA.gameID()));
            Assertions.assertEquals(gameA.game(), gameDAO.getGame(gameA.gameID()).game());
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("Using bad id fails")
    public void badDataGetGameCalls() {
        addTwoGames();
        try {
            Assertions.assertNull(gameDAO.getGame(gameC.gameID()));
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    // because the ID is an int I'm not sure this test is useful
//    @Test
//    @DisplayName("Malformed get calls fail")
//    public void malformedGetGame() {
//        Assertions.assertThrows(DataAccessException.class, () -> {
//            gameDAO.getGame(null);
//        });
//        Assertions.assertThrows(DataAccessException.class, () -> {
//            gameDAO.getGame(null);
//        });
//    }

    // updateGame
    @Test
    @DisplayName("Update game one move at a time for fools mate")
    public void goodUpdateGameCalls() {
        addTwoGames();
        ChessGame game = null;
        GameData data;
        ChessMove[] moves = {
                new ChessMove(new ChessPosition(2, 7), new ChessPosition(4, 7), null),
                new ChessMove(new ChessPosition(7, 5), new ChessPosition(6, 5), null),
                new ChessMove(new ChessPosition(2, 6), new ChessPosition(4, 6), null),
                new ChessMove(new ChessPosition(8, 4), new ChessPosition(4, 8), null)
        };
        try {
            for (ChessMove move : moves) {
                data = gameDAO.getGame(gameA.gameID());
                game = data.game();
                game.makeMove(move);
                GameData updated = new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), game);
                gameDAO.updateGame(data.gameID(), updated);
            }
        } catch (InvalidMoveException e) {
            throw new RuntimeException("My bad!");
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
        Assertions.assertTrue(game.isInCheckmate(ChessGame.TeamColor.WHITE));
    }

    @Test
    @DisplayName("Using bad id fails")
    public void badUpdateGameCalls() {
        addTwoGames();
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(gameC.gameID(), gameA);
        });
    }

    @Test
    @DisplayName("Malformed update calls fail")
    public void malformedUpdateGame() {
        addTwoGames();
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(gameC.gameID(), null);
        });
    }

    // Get game summaries
    @Test
    @DisplayName("Good get summaries call")
    public void getGameSummaries() {
        addTwoGames();
        Collection<GameSummary> summaries = new HashSet<>();
        summaries.add(summaryA);
        summaries.add(summaryB);
        try {
            Collection<GameSummary> dbSummaries = new HashSet<>(gameDAO.getGameSummaries());
            Assertions.assertEquals(summaries, dbSummaries);
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }

    public void addTwoGames() {
        try {
            gameDAO.addGame(gameA);
            gameDAO.addGame(gameB);
        } catch (DataAccessException e) {
            Assertions.fail(String.format("Test threw an exception:\n%s", e.getMessage()));
        }
    }
}
