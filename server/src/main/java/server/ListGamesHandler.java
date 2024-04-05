package server;

import dataAccess.DataAccessException;
import server.request.InvalidRequestException;
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
            var j = req.body();
            ListGamesRequest parsedRequest = serializer.fromJson(req.body(), ListGamesRequest.class);
            if (parsedRequest == null) throw new InvalidRequestException("Auth or nothin'");
            ListGamesResult result = games.listGames(parsedRequest);
            body = serializer.toJson(result, ListGamesResult.class);
            setStatusAndBody(res, 200, body);
        } catch (DataAccessException e) {
            return fourZeroOne();
        } catch (InvalidRequestException e) {
            body = serializer.toJson(new Result(400, "Error: bad request"));
            setStatusAndBody(res, 401, body);
        }
        return body;
    }
}
