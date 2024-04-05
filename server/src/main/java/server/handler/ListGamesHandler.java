package server.handler;

import dataAccess.DataAccessException;
import server.request.InvalidRequestException;
import server.request.ListGamesRequest;
import server.result.ListGamesResult;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            if (authToken == null) throw new InvalidRequestException("Auth or nothin'");
            ListGamesResult result = games.listGames(new ListGamesRequest(authToken));
            return success(res, serializer.toJson(result, ListGamesResult.class));
        } catch (DataAccessException e) {
            return unauthorized(res);
        } catch (InvalidRequestException e) {
            return badRequest(res);
        }
    }
}
