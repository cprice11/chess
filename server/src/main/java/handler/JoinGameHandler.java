package handler;

import chess.ChessGame;
import service.AlreadyTakenException;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends RequestHandler{
    private record JoinGameRequestBody(ChessGame.TeamColor playerColor, int gameID){};
    public Object handle(Request request, Response response) {
        System.out.println("Joining game");
        String authToken = request.headers("authorization");
        JoinGameRequestBody requestBody = gson.fromJson(request.body(), JoinGameRequestBody.class);
        if (authToken == null || requestBody.gameID < 1 || requestBody.playerColor == null) {
            return error(response, 400, "Error: bad request");
        }
        try {
            gameService.joinGame(authToken, requestBody.playerColor, requestBody.gameID);
        } catch (UnauthorizedException e) {
            return error(response, 401, "Error: unauthorized");
        } catch (AlreadyTakenException e) {
            return error(response, 403, "Error: already taken");
        } catch (Exception e) {
            return error(response, 500, "Error: " + e.getMessage());
        }

        response.status(200);
        return gson.toJson(new Object());
    }
}
