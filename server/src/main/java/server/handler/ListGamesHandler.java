package server.handler;

import dataAccess.DataAccessException;
import server.request.InvalidRequestException;
import server.request.ListGamesRequest;
import server.result.ListGamesResult;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler {
    // FIXME Throwing invalid request is undefined behavior
    public static String safeHandleRequest(Request req, Response res) throws UnauthorizedException, InvalidRequestException{
        String authToken = req.headers("authorization");
        if (authToken == null) throw new InvalidRequestException("Auth or nothin'");
        ListGamesResult result = games.listGames(new ListGamesRequest(authToken));
        return success(res, serializer.toJson(result, ListGamesResult.class));
    }
}
