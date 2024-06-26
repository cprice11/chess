package server.handler;

import server.result.Result;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler {

    public static String handleRequest(Request req, Response res) {
        try {
            initialize();
            dev.clearDatabase();
            return success(res, serializer.toJson(new Result("")));
        } catch (Exception e) {
            return catchExceptions(res, e);
        }
    }
}