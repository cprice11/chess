package server;

public record CreateGameResult(
        String username,
        String password,
        String email
) {}
