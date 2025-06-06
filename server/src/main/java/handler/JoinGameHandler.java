package handler;

import chess.ChessGame;
import dataaccess.DataAccessException;
import service.AlreadyTakenException;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends RequestHandler{
    private record JoinGameRequestBody(ChessGame.TeamColor playerColor, int gameID) {
    }
    public Object handle(Request request, Response response) {
        System.out.println("Joining game");
        String authToken = request.headers("authorization");
        JoinGameRequestBody requestBody = GSON.fromJson(request.body(), JoinGameRequestBody.class);
        if (authToken == null || requestBody.gameID < 1 || requestBody.playerColor == null) {
            return error(response, 400, "Error: bad request");
        }
        try {
            GAME_SERVICE.joinGame(authToken, requestBody.playerColor, requestBody.gameID);
        } catch (UnauthorizedException e) {
            return error(response, 401, "Error: unauthorized");
        } catch (AlreadyTakenException e) {
            return error(response, 403, "Error: already taken");
        } catch (DataAccessException e) {
            return error(response, 500, "Error: " + e.getMessage());
        }

        response.status(200);
        return GSON.toJson(new Object());
    }
}
