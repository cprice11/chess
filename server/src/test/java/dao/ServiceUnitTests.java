package dao;

import chess.ChessGame;
import dataaccess.*;
import datamodels.AuthData;
import datamodels.GameData;
import datamodels.GameSummary;
import datamodels.UserData;
import org.junit.jupiter.api.BeforeEach;
import service.GameService;
import service.UserService;

public class ServiceUnitTests {
    public AuthDAO authDAO = new MemoryAuth();
    public GameDAO gameDAO = new MemoryGame();
    public UserDAO userDAO = new MemoryUser();
    public final AuthData authA = new AuthData("userA", "token-a");
    public final AuthData authB = new AuthData("userB", "token-b");
    public final AuthData authC = new AuthData("userC", "token-c");
    public final GameData gameA = new GameData(1, "userA", "userB", "gameA", new ChessGame());
    public final GameData gameB = new GameData(2, "userB", "userC", "gameB", new ChessGame());
    public final GameData gameC = new GameData(3, "userC", "userA", "gameC", new ChessGame());
    public final UserData userA = new UserData("userA", "userApass", "a@email.com");
    public final UserData userB = new UserData("userB", "userBpass", "b@email.com");
    public final UserData userC = new UserData("userC", "userCpass", "c@email.com");
    public final GameSummary summaryA = new GameSummary(gameA.gameID(), gameA.blackUsername(), gameA.whiteUsername(), gameA.gameName());
    public final GameSummary summaryB = new GameSummary(gameB.gameID(), gameB.blackUsername(), gameB.whiteUsername(), gameB.gameName());

    public final GameService game = new GameService(authDAO, gameDAO);
    public final UserService user = new UserService(authDAO, userDAO);

    @BeforeEach
    public void setupDataBase() {
        try {
            authDAO.clearAll();
            gameDAO.clearAll();
            userDAO.clearAll();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
