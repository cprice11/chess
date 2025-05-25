package handler;

import dataModels.AuthData;
import dataModels.UserData;
import dataaccess.DataAccessException;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LogoutUserHandler extends RequestHandler{
    private record LogoutRequest(String authToken){};
    public Object handle(Request request, Response response) {
        System.out.println("Logging out user");
        LogoutRequest logout = gson.fromJson(request.headers().toString(), LogoutRequest.class);

        if (logout == null) {
            return error(response, 400, "Error: bad request");
        }
        try {
            userService.logoutUser(logout.authToken);
        } catch (UnauthorizedException e) {
            return error(response, 401, "Error: Unauthorized");
        } catch (Exception e) {
            return error(response, 500, e.getMessage());
        }

        response.status(200);
        return gson.toJson(new Object());
    }
}
