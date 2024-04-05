package server;


import com.google.gson.Gson;
import dataAccess.*;
import server.result.Result;
import service.AuthService;
import service.DevService;
import service.GameService;
import service.UserService;
import spark.Response;

public class Handler {
    protected static MemoryDatabase db = new MemoryDatabase();
    protected static AuthDAO authDAO = new MemoryAuthDAO();
    protected static GameDAO gameDAO = new MemoryGameDAO();
    protected static UserDAO userDAO = new MemoryUserDAO();
    protected static DevService dev = new DevService(authDAO, gameDAO, userDAO);
    protected static AuthService auth = new AuthService(authDAO);
    protected static GameService games = new GameService(gameDAO, auth);
    protected static UserService users = new UserService(userDAO, auth);
    protected static Gson serializer = new Gson();

    protected static void setStatusAndBody(Response res, int status, String body) {
        res.status(status);
        res.body(body);
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
        return  body;
    }
    protected static String unauthorized(Response res) {
        res.status(401);
        String body = serializer.toJson(new Result("Error: unauthorized"));
        res.body(body);
        return  body;
    }

    protected static String badRequest(Response res) {
        res.status(400);
        String body = serializer.toJson(new Result("Error: badRequest"));
        res.body(body);
        return  body;
    }
    protected static String failure(Response res, String message) {
        res.status(500);
        String body = serializer.toJson(new Result("Error: " + message));
        res.body(body);
        return  body;
    }


//    The server handler classes serve as a translator between HTTP and Java. Your handlers will convert an HTTP request
//    into Java usable objects & data. The handler then calls the appropriate service. When the service responds it
//    converts the response object back to JSON and sends the HTTP response.
//
//    You need to create the number of handler classes that are appropriate for your server design. For a simple server
//    this could be a single class with a few handler methods, or for a complex application it could be dozens of
//    classes each representing a different group of cohesive endpoints.
}
