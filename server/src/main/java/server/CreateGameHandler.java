package server;

import dataAccess.DataAccessException;
import server.request.CreateGameRequest;
import server.result.CreateGameResult;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        try {
            CreateGameRequest parsedRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
            CreateGameResult result = games.createGame(parsedRequest);
            return success(res, serializer.toJson(result, CreateGameResult.class));
        } catch (DataAccessException e) {
            return unauthorized(res);
        }
    }
}
