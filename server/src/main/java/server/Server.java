package server;

import com.google.gson.Gson;
import handler.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
import websocket.commands.UserGameCommand;

@WebSocket
public class Server {
    public Server() {
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        // Websocket connection
        Spark.webSocket("/ws", Server.class);

        ClearHandler clearHandler = new ClearHandler();
        CreateGameHander createGameHander = new CreateGameHander();
        JoinGameHandler joinGameHandler = new JoinGameHandler();
        ListGamesHandler listGamesHandler = new ListGamesHandler();
        LoginUserHandler loginUserHandler = new LoginUserHandler();
        LogoutUserHandler logoutUserHandler = new LogoutUserHandler();
        RegisterUserHandler registerUserHandler = new RegisterUserHandler();


        Spark.staticFiles.location("web");

        // Register endpoints and handle exceptions here.
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

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        session.getRemote().sendString("Received: " + message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
