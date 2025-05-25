package service;

import java.util.UUID;

public class Service {
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
