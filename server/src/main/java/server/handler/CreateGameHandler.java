package server.handler;

import server.request.CreateGameRequest;
import server.request.CreateGameRequestBody;
import server.result.CreateGameResult;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {
    public static String handleRequest(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            CreateGameRequest parsedRequest = new CreateGameRequest(
                    authToken,
                    serializer.fromJson(req.body(), CreateGameRequestBody.class).gameName()
            );
            CreateGameResult result = games.createGame(parsedRequest);
            return success(res, serializer.toJson(result, CreateGameResult.class));
        } catch (Exception e) {
            return catchExceptions(res, e);
        }
    }
}
