package clientTests;

import dataAccess.AuthDao;
import dataAccess.GameDao;
import dataAccess.UserDao;
import dataAccess.sqlDao.SQLAuthDao;
import dataAccess.sqlDao.SQLGameDao;
import dataAccess.sqlDao.SQLUserDao;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerFacade;

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
            System.out.println("Started test HTTP server on " + port);
            serverFacade.register("user0", "0spas", "zero@site.com");
        } catch (Exception e) {
            Assertions.fail("Unexpected exception in setup: " + e.getMessage());
        }
    }



    @Test
    @Order(1)
    public void registerCorrectly() throws Exception {
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
        Assertions.fail("NOT WRITTEN");
    }

    @Test
    @Order(4)
    public void loginIncorrectly() throws Exception {
        Assertions.fail("NOT WRITTEN");
    }

    @Test
    @Order(5)
    public void logoutCorrectly() throws Exception {
        Assertions.fail("NOT WRITTEN");
    }

    @Test
    @Order(6)
    public void logoutIncorrectly() throws Exception {
        Assertions.fail("NOT WRITTEN");
    }

    @Test
    @Order(7)
    public void listCorrectly() throws Exception {
        Assertions.fail("NOT WRITTEN");
    }

    @Test
    @Order(8)
    public void listIncorrectly() throws Exception {
        Assertions.fail("NOT WRITTEN");
    }

    @Test
    @Order(9)
    public void createCorrectly() throws Exception {
        Assertions.fail("NOT WRITTEN");
    }

    @Test
    @Order(10)
    public void createIncorrectly() throws Exception {
        Assertions.fail("NOT WRITTEN");
    }

    @Test
    @Order(11)
    public void joinCorrectly() throws Exception {
        Assertions.fail("NOT WRITTEN");
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