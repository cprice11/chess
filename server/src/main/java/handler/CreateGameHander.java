package handler;

import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class CreateGameHander extends RequestHandler{
    private record CreateGameRequestBody(String gameName){};
    private record CreateGameResponse(int gameID){};
    public Object handle(Request request, Response response) {
        System.out.println("Logging out user");
        AuthHeader authHeader = gson.fromJson(request.headers().toString(), AuthHeader.class);
        CreateGameRequestBody requestBody = gson.fromJson(request.body(), CreateGameRequestBody.class);

        if (authHeader.authToken() == null || requestBody.gameName == null) {
            return error(response, 400, "Error: bad request");
        }
        int gameID;
        try {
            gameID = gameService.createGame(authHeader.authToken(), requestBody.gameName);
        } catch (UnauthorizedException e) {
            return error(response, 401, "Error: Unauthorized");
        } catch (Exception e) {
            return error(response, 500, e.getMessage());
        }

        response.status(200);
        return gson.toJson(new CreateGameResponse(gameID));
    }
}
