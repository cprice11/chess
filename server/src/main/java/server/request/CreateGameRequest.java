package server.request;

public record CreateGameRequest(
        String authorization,
        String gameName
) {
}
