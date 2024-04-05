package server;

import dataAccess.DataAccessException;
import server.request.LoginRequest;
import server.result.LoginResult;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    public static String handleRequest(Request req, Response res) {
        try {
            LoginRequest parsedRequest = serializer.fromJson(req.body(), LoginRequest.class);
            LoginResult result = users.login(parsedRequest);
            return success(res, serializer.toJson(result, LoginResult.class));
        } catch (DataAccessException e) {
            return unauthorized(res);
        }
    }
}
