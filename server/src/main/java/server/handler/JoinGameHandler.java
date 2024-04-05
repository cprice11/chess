package server.handler;

import dataAccess.DataAccessException;
import server.request.JoinGameRequest;
import server.result.Result;
import service.AlreadyTakenException;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
    public static String safeHandleRequest(Request req, Response res) throws UnauthorizedException, AlreadyTakenException, DataAccessException {
        String authToken = req.headers("authorization");
        JoinGameRequest parsedRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
        parsedRequest = new JoinGameRequest(authToken, parsedRequest.playerColor(), parsedRequest.gameID());
        games.joinGame(parsedRequest);
        return success(res, serializer.toJson(new Result(null)));
    }
}
