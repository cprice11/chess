package server.result;

import model.GameSummary;

public record ListGamesResult(
        java.util.Collection<GameSummary> games
) {
}
