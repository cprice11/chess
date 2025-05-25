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
    protected static final AuthDAO authDAO = new MemoryAuth();
    protected static final GameDAO gameDAO = new MemoryGame();
    protected static final UserDAO userDAO = new MemoryUser();
    protected static final UserService userService = new UserService(authDAO, userDAO);
    protected static final GameService gameService = new GameService(authDAO, gameDAO);
    protected static final DevService devService = new DevService(authDAO, gameDAO, userDAO);
    private record Message(String message){};
    protected static final Gson gson = new Gson();
    protected record AuthHeader(String authorization){};
    abstract public Object handle(Request request, Response response);

    public Object error(Response response, int code, String message) {
        response.status(code);
        Message m = new Message(message);
        return(gson.toJson(m, Message.class));
    }
}
