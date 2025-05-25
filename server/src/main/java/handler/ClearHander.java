package handler;

import service.DevService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHander extends RequestHandler {
    public Object handle(Request request, Response response) {
        System.out.println("Clearing database");
        devService.clear();
        response.status(200);
        return gson.toJson(new Object());
    }
}
