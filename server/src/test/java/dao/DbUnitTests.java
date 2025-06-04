package dao;

import chess.ChessGame;
import dataaccess.*;
import datamodels.AuthData;
import datamodels.GameData;
import datamodels.UserData;
import org.junit.jupiter.api.BeforeEach;
import service.DevService;
import service.GameService;
import service.UserService;

// TODO: Write tests for each method
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

    // I think this muddies the workflow in this case
    @BeforeEach
    public void setupDataBase() {
        authDAO.clearAll();
        gameDAO.clearAll();
        userDAO.clearAll();
    }
}
