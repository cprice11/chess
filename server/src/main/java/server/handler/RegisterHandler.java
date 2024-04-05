package server.handler;

import server.request.InvalidRequestException;
import server.request.RegisterRequest;
import server.result.RegisterResult;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    public static String handleRequest(Request req, Response res) {
        try {
            RegisterRequest parsedRequest = serializer.fromJson(req.body(), RegisterRequest.class);
            if (parsedRequest.password() == null) throw new InvalidRequestException("No password");
            RegisterResult result = users.register(parsedRequest);
            return success(res, serializer.toJson(result));
        } catch (Exception e) {
            return catchExceptions(res, e);
        }
    }
}
