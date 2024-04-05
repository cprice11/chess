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

    protected static String fourZeroOne() {
        return serializer.toJson(new Result(401, "Error: unauthorized"));
    }


//    The server handler classes serve as a translator between HTTP and Java. Your handlers will convert an HTTP request
//    into Java usable objects & data. The handler then calls the appropriate service. When the service responds it
//    converts the response object back to JSON and sends the HTTP response.
//
//    You need to create the number of handler classes that are appropriate for your server design. For a simple server
//    this could be a single class with a few handler methods, or for a complex application it could be dozens of
//    classes each representing a different group of cohesive endpoints.
}
