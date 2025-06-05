package service;

import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class Service {
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String lowSodiumHash(String unhashed) {
        return BCrypt.hashpw(unhashed, BCrypt.gensalt());
    }

    public boolean lowSodiumConfirm(String hashed, String unhashed) {
        return BCrypt.checkpw(unhashed, hashed);
    }

    public String hashString(String unhashed) {
        String salt = "UE1OTiJ9aEZ6NGZ1eXk4XW17Xy9Lfm4r";
        String salted = unhashed + salt;
        return BCrypt.hashpw(salted, BCrypt.gensalt());
    }

    public boolean confirmHash(String hashed, String unhashed) {
        String salt = "UE1OTiJ9aEZ6NGZ1eXk4XW17Xy9Lfm4r";
        String salted = unhashed + salt;
        return BCrypt.checkpw(salted, hashed);
    }
}