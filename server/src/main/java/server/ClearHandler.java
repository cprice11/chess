package server;

import server.result.Result;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        try {
            dev.clearDatabase();
            return success(res, serializer.toJson(new Result("")));
        } catch (Exception e) {
            return failure(res, e.getMessage());
        }
    }
}