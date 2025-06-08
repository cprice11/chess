package client;

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
    private static String authToken;
    private UserData user = new UserData("username", "pass1234", "user@email");
    private static int gameId = 0;

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
        authToken = facade.registerUser(user);
    }

    @Test
    @Order(2)
    public void quitTest() {
        facade.logoutUser(authToken);
    }

    @Test
    @Order(3)
    public void loginTest() {
        authToken = facade.loginUser(user.username(), user.password());
    }

    @Test
    @Order(4)
    public void createGameTest() {
        gameId = facade.createGame("myGame", authToken);
    }

    @Test
    @Order(5)
    public void listGamesTest() {
        Collection<GameSummary> games = facade.listGames(authToken);
        System.out.println(games);
    }

    @Order(6)
    @Test
    public void joinGameTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Order(7)
    @Test
    public void observeGameTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Order(8)
    @Test
    public void logoutTest() {
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
