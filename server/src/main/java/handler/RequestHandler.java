package handler;

import com.google.gson.Gson;
import dataaccess.*;
import service.DevService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public abstract class RequestHandler implements Route {
    protected static final AuthDAO AUTH_DAO = new MySqlAuth();
    protected static final GameDAO GAME_DAO = new MySqlGame();
    protected static final UserDAO USER_DAO = new MySqlUser();
    protected static final UserService USER_SERVICE = new UserService(AUTH_DAO, USER_DAO);
    protected static final GameService GAME_SERVICE = new GameService(AUTH_DAO, GAME_DAO);
    protected static final DevService DEV_SERVICE = new DevService(AUTH_DAO, GAME_DAO, USER_DAO);
    protected static final Gson GSON = new Gson();

    private record Message(String message) {
    }
    abstract public Object handle(Request request, Response response);

    public Object error(Response response, int code, String message) {
        response.status(code);
        Message m = new Message(message);
        return(GSON.toJson(m, Message.class));
    }
}
