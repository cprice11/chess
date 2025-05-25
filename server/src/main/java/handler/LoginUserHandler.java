package handler;

import dataModels.AuthData;
import dataaccess.DataAccessException;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LoginUserHandler extends RequestHandler{
    private record loginRequest(String username, String password){};
    private record loginResponse(String authToken){};
    public Object handle(Request request, Response response) {
        loginRequest r = gson.fromJson(request.body(), loginRequest.class);
        AuthData auth;

        if (r.username() == null || r.password() == null) {
            return error(response, 400, "Error: bad request");
        }
        try {
            auth = userService.loginUser(r.username(), r.password());
        } catch (UnauthorizedException e ) {
            return error(response, 401, "Error: unauthorized");
        } catch (Exception e) {
            return error(response, 500, "Error: " + e.getMessage());
        }

        response.status(200);
        return gson.toJson(auth.authToken(), loginResponse.class);
    }
}
