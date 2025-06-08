package handler;

import dataaccess.DataAccessException;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class CreateGameHander extends RequestHandler{
    private record CreateGameRequestBody(String gameName) {
    }

    private record CreateGameResponse(int gameID) {
    }
    public Object handle(Request request, Response response) {
        System.out.println("Creating game");
        String authToken = request.headers("authorization");
        CreateGameRequestBody requestBody = GSON.fromJson(request.body(), CreateGameRequestBody.class);

        if (authToken == null || requestBody.gameName == null || authToken.isEmpty() || requestBody.gameName.isEmpty()) {
            return error(response, 400, "Error: bad request");
        }
        int gameID;
        try {
            gameID = GAME_SERVICE.createGame(authToken, requestBody.gameName);
        } catch (UnauthorizedException e) {
            return error(response, 401, "Error: unauthorized");
        } catch (DataAccessException e) {
            return error(response, 500, "Error: " + e.getMessage());
        }

        response.status(200);
        return GSON.toJson(new CreateGameResponse(gameID));
    }
}
