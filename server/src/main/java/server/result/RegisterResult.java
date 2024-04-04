package server.result;

public record RegisterResult(
        String authToken,
        String username
) {
}