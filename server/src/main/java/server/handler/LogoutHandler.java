package server.handler;

import dataAccess.DataAccessException;
import server.request.LogoutRequest;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    public static String handleRequest(Request req, Response res) {
        try {
            users.logout(new LogoutRequest(req.headers("authorization")));
            return success(res, "");
        } catch (DataAccessException e) {
            return unauthorized(res);
        }
    }
}
