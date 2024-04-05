package server.handler;

import dataAccess.DataAccessException;
import server.request.RegisterRequest;
import server.result.RegisterResult;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        try {
            RegisterRequest parsedRequest = serializer.fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = users.register(parsedRequest);
            return success(res, serializer.toJson(result));
        } catch (DataAccessException e) {
            return unauthorized(res);
        }
    }
}
