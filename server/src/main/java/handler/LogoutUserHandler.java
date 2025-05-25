package handler;

import spark.Request;
import spark.Response;

public class LogoutUserHandler extends RequestHandler{
    public Object handle(Request request, Response response) {
        System.out.println("Not implemented");
        return response;
    }
}
