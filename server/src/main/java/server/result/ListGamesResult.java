package server.result;

import model.GameSummary;

public record ListGamesResult(
        GameSummary[] games
) {
}
