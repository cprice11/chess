package service;

import chess.ChessGame;
import dataaccess.*;
import datamodels.AuthData;
import datamodels.GameData;
import datamodels.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

// literally copied over from UnitTest but using the MySQL DAOs
// There was probably a way to have junit repeat all the child tests,
// but this seemed like a much faster solution
public class DbUnitTests {
    public AuthDAO authDAO = new MySqlAuth();
    public GameDAO gameDAO = new MySqlGame();
    public UserDAO userDAO = new MySqlUser();
    public final AuthData authA = new AuthData("userA", "token-a");
    public final AuthData authB = new AuthData("userB", "token-b");
    public final AuthData authC = new AuthData("userC", "token-c");
    public final GameData gameA = new GameData(1, "userA", "userB", "gameA", new ChessGame());
    public final GameData gameB = new GameData(2, "userB", "userC", "gameB", new ChessGame());
    public final UserData userA = new UserData("userA", "userApass", "a@email.com");
    public final UserData userB = new UserData("userB", "userBpass", "b@email.com");
    public final UserData userC = new UserData("userC", "userCpass", "c@email.com");

    public final DevService dev = new DevService(authDAO, gameDAO, userDAO);
    public final GameService game = new GameService(authDAO, gameDAO);
    public final UserService user = new UserService(authDAO, userDAO);

    @BeforeEach
    public void setupDataBase() {
        authDAO.clearAll();
        gameDAO.clearAll();
        userDAO.clearAll();
        try {
            authDAO.addAuth(authA);
            authDAO.addAuth(authB);
            gameDAO.addGame(gameA);
            gameDAO.addGame(gameB);
            userDAO.addUser(userA);
            userDAO.addUser(userB);
        } catch (DataAccessException e) {
            Assertions.fail("Failed during setup");
        }
    }
}
