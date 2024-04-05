package server.handler;

import server.request.LogoutRequest;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    public static String safeHandleRequest(Request req, Response res) throws UnauthorizedException {
        users.logout(new LogoutRequest(req.headers("authorization")));
        return success(res, "");
    }
}
