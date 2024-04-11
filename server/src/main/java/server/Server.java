package server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.handler.*;
import spark.Spark;

@WebSocket
public class Server {


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.


        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void createRoutes() {
        Spark.webSocket("/connect", Server.class);
        Spark.delete("/db", ClearHandler::handleRequest);
        Spark.post("/user", RegisterHandler::handleRequest);
        Spark.post("/session", LoginHandler::handleRequest);
        Spark.delete("/session", LogoutHandler::handleRequest);
        Spark.get("/game", ListGamesHandler::handleRequest);
        Spark.post("/game", CreateGameHandler::handleRequest);
        Spark.put("/game", JoinGameHandler::handleRequest);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }
}