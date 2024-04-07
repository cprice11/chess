package server.handler;


import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.memoryDao.MemoryAuthDao;
import dataAccess.memoryDao.MemoryDatabase;
import dataAccess.memoryDao.MemoryGameDao;
import dataAccess.memoryDao.MemoryUserDao;
import dataAccess.sqlDao.SQLAuthDao;
import dataAccess.sqlDao.SQLGameDao;
import dataAccess.sqlDao.SQLUserDao;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import server.request.InvalidRequestException;
import server.result.Result;
import service.*;
import spark.Response;

public abstract class Handler {
    protected static DatabaseManager db = new DatabaseManager();
    protected static AuthDao authDAO = new SQLAuthDao();
    protected static GameDao gameDAO = new SQLGameDao();
    protected static UserDao userDAO = new SQLUserDao();
    protected static DevService dev = new DevService(authDAO, gameDAO, userDAO);
    protected static AuthService auth = new AuthService(authDAO);
    protected static GameService games = new GameService(gameDAO, auth);
    protected static UserService users = new UserService(userDAO, auth);
    protected static Gson serializer = new Gson();
    protected static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    protected static String success(Response res, String jsonBody) {
        res.status(200);
        res.body(jsonBody);
        return jsonBody;
    }

    protected static String unauthorized(Response res, String message) {
        res.status(401);
        String body = serializer.toJson(new Result("Error: unauthorized" + message));
        res.body(body);
        return body;
    }

    protected static String unauthorized(Response res) {
        res.status(401);
        String body = serializer.toJson(new Result("Error: unauthorized"));
        res.body(body);
        return body;
    }

    protected static String badRequest(Response res) {
        res.status(400);
        String body = serializer.toJson(new Result("Error: badRequest"));
        res.body(body);
        return body;
    }

    protected static String alreadyTaken(Response res) {
        res.status(403);
        String body = serializer.toJson(new Result("Error: already taken"));
        res.body(body);
        return body;
    }

    protected static String failure(Response res, String message) {
        res.status(500);
        String body = serializer.toJson(new Result("Error: " + message));
        res.body(body);
        return body;
    }

    protected static String catchExceptions(Response res, Exception caughtException) {
        try {
            throw caughtException;
        } catch (UnauthorizedException e) {
            return unauthorized(res);
        } catch (AlreadyTakenException e) {
            return alreadyTaken(res);
        } catch (InvalidRequestException e) {
            return badRequest(res);
        } catch (DataAccessException e) {
            return unauthorized(res, e.getMessage());
        } catch (Exception e) {
            return failure(res, e.getMessage());
        }
    }
}
