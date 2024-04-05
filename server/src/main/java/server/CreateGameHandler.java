package server;

import server.request.CreateGameRequest;
import server.request.LoginRequest;
import server.result.CreateGameResult;
import server.result.LoginResult;
import server.result.Result;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        String body;
        try {
            CreateGameRequest parsedRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
            CreateGameResult result = games.createGame(parsedRequest);
            body = serializer.toJson(result, CreateGameResult.class);
            setStatusAndBody(res, 200, body);
        } catch (Exception e) {
            body = serializer.toJson(new Result(500, "Error " + e));
            setStatusAndBody(res, 500, body);
        }
        return body;
    }
}
