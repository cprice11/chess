package server;

record RegisterRequest(
        String username,
        String password,
        String email
) {}
