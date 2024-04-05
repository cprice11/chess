package server.handler;

import dataAccess.DataAccessException;
import server.request.JoinGameRequest;
import server.result.Result;
import service.AlreadyTakenException;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
    public static String handleRequest(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            JoinGameRequest parsedRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
            parsedRequest = new JoinGameRequest(authToken, parsedRequest.playerColor(), parsedRequest.gameID());
            games.joinGame(parsedRequest);
            return success(res, serializer.toJson(new Result(null)));
        } catch (DataAccessException e) {
            return unauthorized(res);
        } catch (AlreadyTakenException e) {
            return alreadyTaken(res);
        }
    }
}