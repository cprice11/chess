package handler;

import dataModels.AuthData;
import dataModels.UserData;
import dataaccess.DataAccessException;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LogoutUserHandler extends RequestHandler{
    public Object handle(Request request, Response response) {
        System.out.println("Logging out user");
        AuthHeader authHeader = gson.fromJson(request.headers().toString(), AuthHeader.class);

        if (authHeader == null) {
            return error(response, 400, "Error: bad request");
        }
        try {
            userService.logoutUser(authHeader.authToken());
        } catch (UnauthorizedException e) {
            return error(response, 401, "Error: Unauthorized");
        } catch (Exception e) {
            return error(response, 500, e.getMessage());
        }

        response.status(200);
        return gson.toJson(new Object());
    }
}
