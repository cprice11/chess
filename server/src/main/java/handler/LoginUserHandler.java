package handler;

import dataModels.AuthData;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

public class LoginUserHandler extends RequestHandler{
    private record loginRequest(String username, String password){};
    private record loginResponse(String authToken){};
    public Object handle(Request request, Response response) {
        loginRequest r = gson.fromJson(request.body(), loginRequest.class);
        AuthData auth;
        try {
            auth = userService.loginUser(r.username(), r.password());
        } catch (DataAccessException e) {
            return error(response, 403, "Unauthorized");
        }
        if (auth == null) {
            return error(response, 401, "Some 401 error");
        }
        return gson.toJson(auth.authToken(), loginResponse.class);
    }
}
