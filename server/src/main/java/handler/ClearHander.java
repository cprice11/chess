package handler;

import spark.Request;
import spark.Response;

public class ClearHander extends RequestHandler {
    public Object handle(Request request, Response response) {
        System.out.println("Clearing database");
        devService.clear();
        response.status(200);
        return gson.toJson(new Object());
    }
}
