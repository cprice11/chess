package handler;

import datamodels.GameSummary;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class ListGamesHandler extends RequestHandler{
    private record ListGamesResponse(Collection<GameSummary> games) {
    }
    public Object handle(Request request, Response response) {
        System.out.println("Logging out user");
        String authToken = request.headers("authorization");

        if (authToken == null) {
            return error(response, 400, "Error: bad request");
        }
        Collection<GameSummary> gameSummaries;
        try {
            gameSummaries = GAME_SERVICE.listGames(authToken);
        } catch (UnauthorizedException e) {
            return error(response, 401, "Error: unauthorized");
        } catch (Exception e) {
            return error(response, 500, "Error: " + e.getMessage());
        }

        response.status(200);
        return GSON.toJson(new ListGamesResponse(gameSummaries), ListGamesResponse.class);
    }
}
