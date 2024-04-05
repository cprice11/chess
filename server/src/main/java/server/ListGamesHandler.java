package server;

import dataAccess.DataAccessException;
import server.request.ListGamesRequest;
import server.result.CreateGameResult;
import server.result.ListGamesResult;
import server.result.Result;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        String body;
        try {
            ListGamesRequest parsedRequest = serializer.fromJson(req.body(), ListGamesRequest.class);
            ListGamesResult result = games.listGames(parsedRequest);
            body = serializer.toJson(result, ListGamesResult.class);
            setStatusAndBody(res, 200, body);
        } catch (DataAccessException e) {
            body = serializer.toJson(new Result(401, "Error: unauthorized"));
            setStatusAndBody(res, 401, body);
        }
        return body;
    }
}
