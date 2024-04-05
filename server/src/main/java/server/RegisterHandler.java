package server;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import server.request.RegisterRequest;
import server.result.RegisterResult;
import server.result.Result;
import service.DevService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        String json;
        try {
            RegisterRequest parsedRequest = serializer.fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = users.register(parsedRequest);
            json = serializer.toJson(result);
            setStatusAndBody(res, 200, json);
        } catch (DataAccessException e) {
            json = serializer.toJson(new Result(401, "Error: unauthorized"));
            setStatusAndBody(res, 401, json);
        }
        return json;
    }
}
