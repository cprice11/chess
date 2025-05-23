package dataaccess;

import dataModels.AuthData;
import dataModels.UserData;

public interface AuthDAO {
    void addAuth(AuthData auth);
    AuthData getAuth(String username);
    void updateAuth(String username, String authToken);
    void deleteAuth(String username);
    void clearAll();
}
