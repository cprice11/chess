package client;

import chess.ChessGame;
import datamodels.GameSummary;
import datamodels.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.Collection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String userOauthToken;
    private static String user1authToken;
    private UserData user0 = new UserData("username", "pass1234", "user0@email");
    private UserData user1 = new UserData("othername", "pass1234", "user1@email");
    private static int gameId = 0;
    private static Collection<GameSummary> games;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("running server facade");
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(1)
    public void registerTest() {
        userOauthToken = facade.registerUser(user0);
        user1authToken = facade.registerUser(user1);
    }

    @Test
    @Order(2)
    public void quitTest() {
        facade.logoutUser(userOauthToken);
    }

    @Test
    @Order(3)
    public void loginTest() {
        userOauthToken = facade.loginUser(user0.username(), user0.password());
    }

    @Test
    @Order(4)
    public void createGameTest() {
        gameId = facade.createGame("zero's Game", userOauthToken);
        gameId = facade.createGame("another Game", user1authToken);
    }

    @Test
    @Order(5)
    public void listGamesTest() {
        games = facade.listGames(userOauthToken);
    }

    @Test
    @Order(6)
    public void joinGameTest() {
        int gameID = games.iterator().next().gameID();
        facade.joinGame(userOauthToken, gameID, ChessGame.TeamColor.WHITE);
        facade.joinGame(user1authToken, gameID, ChessGame.TeamColor.BLACK);
    }

    @Test
    @Order(7)
    public void observeGameTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void negativeRegisterTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void negativeQuitTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void negativeLoginTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void negativeLogoutTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void negativeCreateGameTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void negativeListGamesTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void negativeJoinGameTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void negativeObserveGameTest() {
        throw new RuntimeException("Not implemented yet");
    }
}
