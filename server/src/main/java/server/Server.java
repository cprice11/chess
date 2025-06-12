package server;

import handler.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;

@WebSocket
public class Server {
    WebsocketHandler websocketHandler = new WebsocketHandler();
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
        websocketHandler.handle(session, message);
    }

    @OnWebSocketError
    public void onError(Throwable cause) {
        System.out.println("WEBSOCKET ERROR! " + cause.getMessage());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
