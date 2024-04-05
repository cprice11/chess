package server;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import server.result.Result;
import service.DevService;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler{
    public static String handleRequest(Request req, Response res) {
        String body;
        try {
            dev.clearDatabase();
            body = serializer.toJson("");
            setStatusAndBody(res, 200, body);
        } catch (Exception e) {
            body = serializer.toJson("Error " + e);
            setStatusAndBody(res, 500, body);
        }
        return body;
    }
}