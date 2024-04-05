package server;

import dataAccess.DataAccessException;
import server.request.CreateGameRequest;
import server.request.JoinGameRequest;
import server.result.CreateGameResult;
import server.result.Result;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler{
        public static String handleRequest(Request req, Response res) {
            String body;
            try {
                JoinGameRequest parsedRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
                games.joinGame(parsedRequest);
                body = serializer.toJson(new Result(200, null));
                setStatusAndBody(res, 200, body);
            } catch (DataAccessException e) {
                return fourZeroOne();
            }
            return body;
        }
}
