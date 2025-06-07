package client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static Server facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Order(1)
    @Test
    public void registerTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Order(2)
    @Test
    public void quitTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Order(3)
    @Test
    public void loginTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Order(4)
    @Test
    public void createGameTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Order(5)
    @Test
    public void listGamesTest() {
        throw new RuntimeException("Not implemented yet");
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
