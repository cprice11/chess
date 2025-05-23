package handler;

import service.DevService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHander implements Route {
    DevService devService;
    public ClearHander(DevService service) {
        this.devService = service;
    }

    public Object handle(Request request, Response response) {
        System.out.println("Clearing database");
        devService.clear();
        response.body("{'somekey':'someValue'}");
        return response;
    }
}
