package serviceTests;

import dataAccess.*;
import dataAccess.memoryDao.MemoryAuthDao;
import dataAccess.memoryDao.MemoryDatabase;
import dataAccess.memoryDao.MemoryGameDao;
import dataAccess.memoryDao.MemoryUserDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DevService;

class DevServiceTest extends ServiceVars {
    private static final AuthDao auth = new MemoryAuthDao();
    private static final GameDao games = new MemoryGameDao();
    private static final UserDao users = new MemoryUserDao();
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