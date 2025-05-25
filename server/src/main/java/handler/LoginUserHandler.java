package handler;

import datamodels.AuthData;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LoginUserHandler extends RequestHandler{
    private record LoginRequest(String username, String password) {
    }
    public Object handle(Request request, Response response) {
        LoginRequest r = GSON.fromJson(request.body(), LoginRequest.class);
        AuthData auth;

        if (r.username() == null || r.password() == null) {
            return error(response, 400, "Error: bad request");
        }
        try {
            auth = USER_SERVICE.loginUser(r.username(), r.password());
        } catch (UnauthorizedException e ) {
            return error(response, 401, "Error: unauthorized");
        } catch (Exception e) {
            return error(response, 500, "Error: " + e.getMessage());
        }

        response.status(200);
        return GSON.toJson(auth, AuthData.class);
    }
}
