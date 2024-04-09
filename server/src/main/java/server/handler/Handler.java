package server.handler;


import com.google.gson.Gson;
import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import dataAccess.sqlDao.SQLAuthDao;
import dataAccess.sqlDao.SQLGameDao;
import dataAccess.sqlDao.SQLUserDao;
import server.request.InvalidRequestException;
import server.result.Result;
import service.*;
import spark.Response;

public abstract class Handler {
    protected static AuthDao authDAO;
    protected static GameDao gameDAO;
    protected static UserDao userDAO;
    protected static DevService dev;
    protected static AuthService auth;
    protected static GameService games;
    protected static UserService users;
    protected static Gson serializer = new Gson();

    protected static void initialize() throws DataAccessException {
        if (dev != null && games != null && users != null) {
            return;
        }
        authDAO = new SQLAuthDao();
        gameDAO = new SQLGameDao();
        userDAO = new SQLUserDao();
        dev = new DevService(authDAO, gameDAO, userDAO);
        auth = new AuthService(authDAO);
        games = new GameService(gameDAO, auth);
        users = new UserService(userDAO, auth);
    }

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
