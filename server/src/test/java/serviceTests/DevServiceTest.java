package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DevService;

class DevServiceTest extends ServiceVars {
    private static final AuthDAO auth = new MemoryAuthDAO();
    private static final GameDAO games = new MemoryGameDAO();
    private static final UserDAO users = new MemoryUserDAO();
    private static final DevService dev = new DevService(auth, games, users);

    @BeforeEach
    void buildDatabase() {
        MemoryDatabase.setAuth(authData);
        MemoryDatabase.setGames(gameData);
        MemoryDatabase.setUsers(userData);
    }

    @Test
    void clearDatabase() {
        dev.clearDatabase();
        Assertions.assertTrue(MemoryDatabase.getAuth().isEmpty());
        Assertions.assertTrue(MemoryDatabase.getGames().isEmpty());
        Assertions.assertTrue(MemoryDatabase.getUsers().isEmpty());
    }
}