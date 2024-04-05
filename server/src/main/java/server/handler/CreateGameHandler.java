package server.handler;

import server.request.CreateGameRequest;
import server.request.CreateGameRequestBody;
import server.request.InvalidRequestException;
import server.result.CreateGameResult;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler{
    public static String safeHandleRequest(Request req, Response res) throws InvalidRequestException, UnauthorizedException {
        String authToken = req.headers("authorization");
        CreateGameRequest parsedRequest = new CreateGameRequest(
                authToken,
                serializer.fromJson(req.body(), CreateGameRequestBody.class).gameName()
        );
        CreateGameResult result = games.createGame(parsedRequest);
        return success(res, serializer.toJson(result, CreateGameResult.class));
    }
}
