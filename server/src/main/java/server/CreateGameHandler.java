package server;

import dataAccess.DataAccessException;
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
        } catch (DataAccessException e) {
            return fourZeroOne();
        }
        return body;
    }
}
