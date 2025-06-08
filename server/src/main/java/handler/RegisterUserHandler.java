package handler;

import dataaccess.DataAccessException;
import datamodels.AuthData;
import datamodels.UserData;
import service.AlreadyTakenException;
import spark.Request;
import spark.Response;

public class RegisterUserHandler extends RequestHandler {
    public Object handle(Request request, Response response) {
        System.out.println("Registering user");
        UserData user = GSON.fromJson(request.body(), UserData.class);
        AuthData auth;

        if (user.username() == null || user.password() == null || user.email() == null ||
                user.username().isEmpty() || user.password().isEmpty() || user.email().isEmpty()
        ) {
            return error(response, 400, "Error: bad request");
        }
        try {
            auth = USER_SERVICE.registerUser(user);
        } catch (AlreadyTakenException e) {
            return error(response, 403, "Error: already taken");
        } catch (DataAccessException e) {
            return error(response, 500, "Error: " + e.getMessage());
        }

        response.status(200);
        return GSON.toJson(auth, AuthData.class);
    }
}
