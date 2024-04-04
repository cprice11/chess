package service;

import model.AuthData;
import model.UserData;
import server.Authorization;
import server.LoginRequest;
import server.LoginResult;
import server.LogoutRequest;

public class AuthService extends Service {
    public AuthData createAuth(String username) {
        throw new RuntimeException("Not implemented yet");
    }

    public LoginResult login(LoginRequest request) {
        throw new RuntimeException("Not implemented yet");
    }

    public void logout(LogoutRequest request) {
        throw new RuntimeException("Not implemented yet");
    }

    public UserData verify(String AuthToken) {
        throw new RuntimeException("Not implemented yet");
    }

    public AuthData getAuthByUsername(String username) {
        throw new RuntimeException("Not implemented yet");
    }

    public AuthData getAuthByAuthToken(String authToken) {
        throw new RuntimeException("Not implemented yet");
    }
}
