package handler;

import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

public class ClearHandler extends RequestHandler {
    public Object handle(Request request, Response response) {
        System.out.println("Clearing database");
        try {
            DEV_SERVICE.clear();
        } catch (DataAccessException e) {
            return error(response, 500, "Error: " + e.getMessage());
        }
        response.status(200);
        return GSON.toJson(new Object());
    }
}
