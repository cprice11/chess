package server;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import server.request.RegisterRequest;
import server.result.Result;
import service.DevService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        try {
            RegisterRequest parsedRequest = serializer.fromJson(req.body(), RegisterRequest.class);
            users.register(parsedRequest);
            return serializer.toJson(new Result(200, ""));
        } catch (Exception e) {
            return serializer.toJson(new Result(501, "Error: " + e));
        }
    }
}
