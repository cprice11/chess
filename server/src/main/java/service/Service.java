package service;

import java.util.UUID;

public class Service {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
