package handler;

import dataModels.AuthData;
import dataModels.UserData;
import dataaccess.DataAccessException;
import service.AlreadyTakenException;
import spark.Request;
import spark.Response;

public class RegisterUserHandler extends RequestHandler {
    public Object handle(Request request, Response response) {
        System.out.println("Registering user");
        UserData user = gson.fromJson(request.body(), UserData.class);
        AuthData auth;

        if (user.username() == null || user.password() == null || user.email() == null) {
            return error(response, 400, "Error: bad request");
        }
        try {
            auth = userService.registerUser(user);
        } catch (AlreadyTakenException e) {
            return error(response, 403, "Error: already taken");
        } catch (Exception e) {
            return error(response, 500, "Error: " + e.getMessage());
        }

        response.status(200);
        return gson.toJson(auth, AuthData.class);
    }
}
