package handler;

import spark.Request;
import spark.Response;

public class ClearHander extends RequestHandler {
    public Object handle(Request request, Response response) {
        System.out.println("Clearing database");
        DEV_SERVICE.clear();
        response.status(200);
        return GSON.toJson(new Object());
    }
}
