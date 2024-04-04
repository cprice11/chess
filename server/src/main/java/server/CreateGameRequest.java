package server;

public record CreateGameRequest(
        String authorization,
        String gameName
) {
}
