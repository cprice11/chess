package clientTests;

import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import dataAccess.memoryDao.MemoryDatabase;
import dataAccess.sqlDao.SQLAuthDao;
import dataAccess.sqlDao.SQLGameDao;
import dataAccess.sqlDao.SQLUserDao;
import model.AuthData;
import model.GameSummary;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static int port;
    private static AuthDao auth;
    private static GameDao games;
    private static UserDao users;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);

        serverFacade = new ServerFacade(port, "http://localhost:");
        try {
            auth = new SQLAuthDao();
            games = new SQLGameDao();
            users = new SQLUserDao();
            auth.deleteAll();
            users.deleteAll();
            games.deleteAll();
            var a = auth.getAll();
            var u = users.getAll();
            System.out.println("Started test HTTP server on " + port);
        } catch (Exception e) {
            Assertions.fail("Unexpected exception in setup: " + e.getMessage());
        }
    }


    @Test
    @Order(1)
    public void registerCorrectly() throws Exception {
        var a = auth.getAll();
        String authToken = serverFacade.register("user0", "0spas", "zero@site.com");
        Assertions.assertTrue(auth.getAll().contains(new AuthData(authToken, "user0")));
    }

    @Test
    @Order(2)
    public void registerIncorrectly() throws Exception {
        String authToken = serverFacade.register("user0", "newpaas", "diff@site.com");
        Assertions.assertFalse(auth.getAll().contains(new AuthData(authToken, "user0")));
    }

    @Test
    @Order(3)
    public void loginCorrectly() throws Exception {
        String authToken = serverFacade.login("user0", "0spas");
        Assertions.assertEquals(2, auth.getAll().size());
    }

    @Test
    @Order(4)
    public void loginIncorrectly() throws Exception {
        Assertions.assertEquals(null, serverFacade.login("user0", "badpass"));
    }

    @Test
    @Order(5)
    public void logoutCorrectly() throws Exception {
        var a = auth.getAll();
        String authToken = serverFacade.register("user1", "onepaas", "one@site.com");
        Assertions.assertEquals(3, auth.getAll().size());
        serverFacade.logout(authToken);
        var b = auth.getAll();
        Assertions.assertEquals(a, auth.getAll());
    }

    @Test
    @Order(6)
    public void logoutIncorrectly() throws Exception {
        var a = auth.getAll();
        serverFacade.logout("0spas");
        Assertions.assertEquals(a, auth.getAll());
    }

    @Test
    @Order(7)
    public void createCorrectly() throws Exception {
        String authToken = serverFacade.register("John", "lucy", "@");
        String authToken2 = serverFacade.register("Paul", "in", "@");
        Assertions.assertEquals(0, games.getAll().size());
        serverFacade.createGame(authToken,"I'm a game");
        serverFacade.createGame(authToken2,"I'm a game");
        serverFacade.createGame(authToken,"I'm another game");
        Assertions.assertEquals(3, games.getAll().size());
    }

    @Test
    @Order(8)
    public void createIncorrectly() throws Exception {
        Assertions.assertEquals(3, games.getAll().size());
        serverFacade.createGame("asd", "name");
        Assertions.assertEquals(3, games.getAll().size());
    }

    @Test
    @Order(9)
    public void listCorrectly() throws Exception {
        String authToken = serverFacade.register("george", "the", "@");
        String authToken2 = serverFacade.register("ringo", "sky", "@");
        Collection<GameSummary> sums = serverFacade.listGames(authToken);
        Collection<GameSummary> sums2 = serverFacade.listGames(authToken2);
        Assertions.assertEquals(sums, sums2);
    }

    @Test
    @Order(10)
    public void listIncorrectly() throws Exception {
        String authToken = serverFacade.register("kieth", "with", "@");
        serverFacade.createGame(authToken,"I'm a game");
        serverFacade.createGame(authToken,"I'm a game");
        Collection<GameSummary> sums = serverFacade.listGames(authToken);
        HashSet<GameSummary> hashedSums = new HashSet<>(sums);
        Assertions.assertEquals(sums.size(), hashedSums.size());
    }

    @Test
    @Order(11)
    public void joinCorrectly() throws Exception {
        String authToken = serverFacade.register("calvin", "diamonods", "@");
        serverFacade.createGame(authToken,"I'm a game");
        serverFacade.j
    }

    @Test
    @Order(12)
    public void joinIncorrectly() throws Exception {
        Assertions.fail("NOT WRITTEN");
    }

    @Test
    @Order(13)
    public void joinObserve() throws Exception {
        Assertions.fail("NOT WRITTEN");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }
}