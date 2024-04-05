package server;

import dataAccess.DataAccessException;
import server.request.JoinGameRequest;
import server.result.Result;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler{
        public static String handleRequest(Request req, Response res) {
            String body;
            try {
                JoinGameRequest parsedRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
                games.joinGame(parsedRequest);
                return success(res, serializer.toJson(new Result(null)));
            } catch (DataAccessException e) {
                return unauthorized(res);
            }
        }
}
