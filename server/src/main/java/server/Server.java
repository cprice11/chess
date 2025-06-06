package server;

import handler.*;
import spark.Spark;

public class Server {




    public int run(int desiredPort) {
        ClearHandler clearHandler = new ClearHandler();
        CreateGameHander createGameHander = new CreateGameHander();
        JoinGameHandler joinGameHandler = new JoinGameHandler();
        ListGamesHandler listGamesHandler = new ListGamesHandler();
        LoginUserHandler loginUserHandler = new LoginUserHandler();
        LogoutUserHandler logoutUserHandler = new LogoutUserHandler();
        RegisterUserHandler registerUserHandler = new RegisterUserHandler();

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registerUserHandler);
        Spark.post("/session", loginUserHandler);
        Spark.delete("/session", logoutUserHandler);
        Spark.get("/game", listGamesHandler);
        Spark.post("/game", createGameHander);
        Spark.put("/game", joinGameHandler);
        Spark.delete("/db", clearHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
