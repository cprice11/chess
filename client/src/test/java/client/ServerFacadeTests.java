package client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void quitTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void loginTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void logoutTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void createGameTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void listGamesTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void playGamesTest() {
        throw new RuntimeException("Not implemented yet");
    }

    @Test
    public void observeGameTest() {
        throw new RuntimeException("Not implemented yet");
    }
}
