package server.handler;

import server.request.InvalidRequestException;
import server.request.ListGamesRequest;
import server.result.ListGamesResult;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler {
    // Throwing invalid request is technically undefined behavior
    public static String handleRequest(Request req, Response res) {
        try {
            initialize();
            String authToken = req.headers("authorization");
            if (authToken == null) throw new InvalidRequestException("Auth or nothin'");
            ListGamesResult result = games.listGames(new ListGamesRequest(authToken));
            return success(res, serializer.toJson(result, ListGamesResult.class));
        } catch (Exception e) {
            return catchExceptions(res, e);
        }
    }
}
