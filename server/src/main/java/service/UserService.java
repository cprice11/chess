package service;

import model.UserData;
import server.RegisterRequest;
import server.RegisterResult;

public class UserService extends Service {
    public RegisterResult register(RegisterRequest request) {
        throw new RuntimeException("Not implemented yet");
        // Check if takken
        // Auth.createauth
    }

    public UserData getUserByUsername(String username) {
        throw new RuntimeException("Not implemented yet");
    }

    public UserData getUserByEmail(String username) {
        throw new RuntimeException("Not implemented yet");
    }
}
