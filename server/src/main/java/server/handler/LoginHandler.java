package server.handler;

import dataAccess.DataAccessException;
import server.request.LoginRequest;
import server.result.LoginResult;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    public static String safeHandleRequest(Request req, Response res) throws UnauthorizedException {
        LoginRequest parsedRequest = serializer.fromJson(req.body(), LoginRequest.class);
        LoginResult result = users.login(parsedRequest);
        return success(res, serializer.toJson(result, LoginResult.class));
    }
}
