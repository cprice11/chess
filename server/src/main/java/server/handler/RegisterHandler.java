package server.handler;

import dataAccess.DataAccessException;
import server.request.RegisterRequest;
import server.result.RegisterResult;
import service.AlreadyTakenException;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        try {
            RegisterRequest parsedRequest = serializer.fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = users.register(parsedRequest);
            return success(res, serializer.toJson(result));
        } catch (AlreadyTakenException e) {
            return badRequest(res);
//            return alreadyTaken(res);
        }
    }
}
