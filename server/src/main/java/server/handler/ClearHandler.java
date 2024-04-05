package server.handler;

import server.result.Result;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler{
    public static String safeHandleRequest(Request req, Response res) {
        dev.clearDatabase();
        return success(res, serializer.toJson(new Result("")));
    }
}