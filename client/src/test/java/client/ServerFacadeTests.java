package client;

import chess.ChessGame;
import datamodels.GameSummary;
import datamodels.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

import java.util.Collection;
import java.util.Iterator;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String userAAuthToken;
    private static String userBAuthToken;
    private static String userCAuthToken;
    private static final UserData USER_A = new UserData("username", "pass1234", "user0@email");
    private static final UserData USER_B = new UserData("othername", "pass1234", "user1@email");
    private static final UserData USER_C = new UserData("problemUser", "pass1234", "problem@email");
    private static Collection<GameSummary> games;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("running server facade");
        try {
            facade.clear();
        } catch (ResponseException e) {
            throw new RuntimeException(e.statusCode() + e.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(1)
    public void registerTest() {
        try {
            userAAuthToken = facade.registerUser(USER_A);
            userBAuthToken = facade.registerUser(USER_B);
        } catch (ResponseException e) {
            Assertions.fail(e.statusCode() + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void quitTest() {
        try {
            facade.logoutUser(userAAuthToken);
        } catch (ResponseException e) {
            Assertions.fail(e.statusCode() + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void loginTest() {
        try {
            userAAuthToken = facade.loginUser(USER_A.username(), USER_A.password());
        } catch (ResponseException e) {
            Assertions.fail(e.statusCode() + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void createGameTest() {
        try {
            facade.createGame("zero's Game", userAAuthToken);
            facade.createGame("another Game", userBAuthToken);
        } catch (ResponseException e) {
            Assertions.fail(e.statusCode() + e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void listGamesTest() {
        try {
            games = facade.listGames(userAAuthToken);
        } catch (ResponseException e) {
            Assertions.fail(e.statusCode() + e.getMessage());
        }
    }

    @Test
    @Order(6)
    public void joinGameTest() {
        try {
            int gameID = games.iterator().next().gameID();
            facade.joinGame(userAAuthToken, gameID, ChessGame.TeamColor.WHITE);
            facade.joinGame(userBAuthToken, gameID, ChessGame.TeamColor.BLACK);
        } catch (ResponseException e) {
            Assertions.fail(e.statusCode() + e.getMessage());
        }
    }

    @Test
    @Order(8)
    public void negativeRegisterTest() {
        Assertions.assertThrows(ResponseException.class, () -> facade.registerUser(null));
        try {
            userCAuthToken = facade.registerUser(USER_C);
        } catch (ResponseException e) {
            Assertions.fail(e.statusCode() + e.getMessage());
        }
        Assertions.assertThrows(Exception.class, () -> facade.registerUser(USER_C));
    }

    @Test
    @Order(9)
    public void negativeLoginTest() {
        Assertions.assertThrows(ResponseException.class, () -> facade.loginUser(null, null));
        Assertions.assertThrows(ResponseException.class, () -> facade.loginUser(USER_C.username(), "wrongPassword"));
    }

    @Test
    @Order(10)
    public void negativeLogoutTest() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logoutUser(null));
        Assertions.assertThrows(ResponseException.class, () -> facade.logoutUser("badAuthToken"));
        Assertions.assertDoesNotThrow(() -> facade.logoutUser(userCAuthToken));
        Assertions.assertThrows(ResponseException.class, () -> facade.logoutUser(userCAuthToken));
    }

    @Test
    @Order(11)
    public void negativeCreateGameTest() {
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(null, userAAuthToken));
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame("someGame", null));
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(null, "badAuth"));
    }

    @Test
    @Order(11)
    public void negativeListGamesTest() {
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames(null));
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames(userCAuthToken));
    }

    @Test
    @Order(12)
    public void negativeJoinGameTest() {
        Iterator<GameSummary> gameSummaries = games.iterator();
        int fullGame = gameSummaries.next().gameID();
        int emptyGame = gameSummaries.next().gameID();
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(null, emptyGame, ChessGame.TeamColor.WHITE));
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(userAAuthToken, 0, ChessGame.TeamColor.WHITE));
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(userAAuthToken, emptyGame, null));
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(userCAuthToken, emptyGame, ChessGame.TeamColor.WHITE));
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(userAAuthToken, fullGame, ChessGame.TeamColor.WHITE));
    }
}
