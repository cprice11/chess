package server.handler;

import server.request.JoinGameRequest;
import server.result.Result;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
    public static String handleRequest(Request req, Response res) {
        try {
            initialize();
            String authToken = req.headers("authorization");
            JoinGameRequest parsedRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
            parsedRequest = new JoinGameRequest(authToken, parsedRequest.playerColor(), parsedRequest.gameID());
            games.joinGame(parsedRequest);
            return success(res, serializer.toJson(new Result(null)));
        } catch (Exception e) {
            return catchExceptions(res, e);
        }
    }
}
