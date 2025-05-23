package server;

import dataaccess.*;
import handler.*;
import service.DevService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    AuthDAO authDAO = new MemoryAuth();
    GameDAO gameDAO = new MemoryGame();
    UserDAO userDAO = new MemoryUser();
    DevService devService = new DevService(authDAO, gameDAO, userDAO);
    GameService gameService = new GameService();
    UserService userService = new UserService();
    ClearHander clearHander = new ClearHander(devService);
    CreateGameHander createGameHander = new CreateGameHander();
    JoinGameHandler joinGameHandler = new JoinGameHandler();
    ListGamesHandler listGamesHandler = new ListGamesHandler();
    LoginUserHandler loginUserHandler = new LoginUserHandler();
    LogoutUserHandler logoutUserHandler = new LogoutUserHandler();
    RegisterUserHandler registerUserHandler = new RegisterUserHandler();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        Spark.post("/user", registerUserHandler);
//        Spark.post("/session", loginUserHandler);
//        Spark.delete("/session", logoutUserHandler);
//        Spark.get("/game", listGamesHandler);
//        Spark.post("/game", createGameHander);
//        Spark.put("/game", joinGameHandler);
        Spark.delete("/db", clearHander);

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
