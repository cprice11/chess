package client;

import chess.ChessGame;
import datamodels.GameSummary;
import datamodels.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.Collection;
import java.util.Iterator;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String userAAuthToken;
    private static String userBAuthToken;
    private static String userCAuthToken;
    private static final UserData userA = new UserData("username", "pass1234", "user0@email");
    private static final UserData userB = new UserData("othername", "pass1234", "user1@email");
    private static final UserData userC = new UserData("problemUser", "pass1234", "problem@email");
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
        } catch (client.ResponseException e) {
            throw new RuntimeException(e.StatusCode() + e.getMessage());
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
            userAAuthToken = facade.registerUser(userA);
            userBAuthToken = facade.registerUser(userB);
        } catch (client.ResponseException e) {
            Assertions.fail(e.StatusCode() + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void quitTest() {
        try {
            facade.logoutUser(userAAuthToken);
        } catch (client.ResponseException e) {
            Assertions.fail(e.StatusCode() + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void loginTest() {
        try {
            userAAuthToken = facade.loginUser(userA.username(), userA.password());
        } catch (client.ResponseException e) {
            Assertions.fail(e.StatusCode() + e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void createGameTest() {
        try {
            facade.createGame("zero's Game", userAAuthToken);
            facade.createGame("another Game", userBAuthToken);
        } catch (client.ResponseException e) {
            Assertions.fail(e.StatusCode() + e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void listGamesTest() {
        try {
            games = facade.listGames(userAAuthToken);
        } catch (client.ResponseException e) {
            Assertions.fail(e.StatusCode() + e.getMessage());
        }
    }

    @Test
    @Order(6)
    public void joinGameTest() {
        try {
        int gameID = games.iterator().next().gameID();
            facade.joinGame(userAAuthToken, gameID, ChessGame.TeamColor.WHITE);
            facade.joinGame(userBAuthToken, gameID, ChessGame.TeamColor.BLACK);
        } catch (client.ResponseException e) {
            Assertions.fail(e.StatusCode() + e.getMessage());
        }
    }

    @Test
    @Order(8)
    public void negativeRegisterTest() {
        Assertions.assertThrows(client.ResponseException.class, () -> facade.registerUser(null));
        try {
            userCAuthToken = facade.registerUser(userC);
        } catch (client.ResponseException e) {
            Assertions.fail(e.StatusCode() + e.getMessage());
        }
        Assertions.assertThrows(Exception.class, () -> facade.registerUser(userC));
    }

    @Test
    @Order(9)
    public void negativeLoginTest() {
        Assertions.assertThrows(client.ResponseException.class, () -> facade.loginUser(null, null));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.loginUser(userC.username(), "wrongPassword"));
    }

    @Test
    @Order(10)
    public void negativeLogoutTest() {
        Assertions.assertThrows(client.ResponseException.class, () -> facade.logoutUser(null));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.logoutUser("badAuthToken"));
        Assertions.assertDoesNotThrow(() -> facade.logoutUser(userCAuthToken));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.logoutUser(userCAuthToken));
    }

    @Test
    @Order(11)
    public void negativeCreateGameTest() {
        Assertions.assertThrows(client.ResponseException.class, () -> facade.createGame(null, userAAuthToken));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.createGame("someGame", null));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.createGame(null, "badAuth"));
    }

    @Test
    @Order(11)
    public void negativeListGamesTest() {
        Assertions.assertThrows(client.ResponseException.class, () -> facade.listGames(null));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.listGames(userCAuthToken));
    }

    @Test
    @Order(12)
    public void negativeJoinGameTest() {
        Iterator<GameSummary> gameSummaries = games.iterator();
        int fullGame = gameSummaries.next().gameID();
        int emptyGame = gameSummaries.next().gameID();
        Assertions.assertThrows(client.ResponseException.class, () -> facade.joinGame(null, emptyGame, ChessGame.TeamColor.WHITE));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.joinGame(userAAuthToken, 0, ChessGame.TeamColor.WHITE));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.joinGame(userAAuthToken, emptyGame, null));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.joinGame(userCAuthToken, emptyGame, ChessGame.TeamColor.WHITE));
        Assertions.assertThrows(client.ResponseException.class, () -> facade.joinGame(userAAuthToken, fullGame, ChessGame.TeamColor.WHITE));
    }
}
