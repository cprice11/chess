package server.handler;

import dataAccess.DataAccessException;
import server.request.InvalidRequestException;
import server.request.RegisterRequest;
import server.result.RegisterResult;
import service.AlreadyTakenException;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        try {
            RegisterRequest parsedRequest = serializer.fromJson(req.body(), RegisterRequest.class);
            if (parsedRequest.password() == null) throw new InvalidRequestException("No password");
            RegisterResult result = users.register(parsedRequest);
            return success(res, serializer.toJson(result));
        } catch (AlreadyTakenException e) {
            return alreadyTaken(res);
        } catch (InvalidRequestException e) {
            return badRequest(res);
        }
    }
}
