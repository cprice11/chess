package server;

import model.GameSummary;

public record ListGamesResult(
        GameSummary[] games
) {
}
