package handler;

import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LogoutUserHandler extends RequestHandler{
    public Object handle(Request request, Response response) {
        System.out.println("Logging out user");
        String authToken = request.headers("authorization");

        if (authToken == null) {
            return error(response, 400, "Error: bad request");
        }
        try {
            USER_SERVICE.logoutUser(authToken);
        } catch (UnauthorizedException e) {
            return error(response, 401, "Error: Unauthorized");
        } catch (Exception e) {
            return error(response, 500, "Error: " + e.getMessage());
        }

        response.status(200);
        return GSON.toJson(new Object());
    }
}
