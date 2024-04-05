package server;

import dataAccess.DataAccessException;
import server.request.LoginRequest;
import server.request.RegisterRequest;
import server.result.LoginResult;
import server.result.RegisterResult;
import server.result.Result;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    public static String handleRequest(Request req, Response res) {
        String body;
        try {
            LoginRequest parsedRequest = serializer.fromJson(req.body(), LoginRequest.class);
            LoginResult result = users.login(parsedRequest);
            body = serializer.toJson(result, LoginResult.class);
            setStatusAndBody(res, 200, body);
        } catch (DataAccessException e) {
            body = serializer.toJson(new Result(401, "Error: unauthorized"));
            setStatusAndBody(res, 401, body);
        }
        return body;
    }
}
