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
        try {
            dev.clearDatabase();
            return serializer.toJson(new Result(200, ""));
        } catch (Exception e) {
            return serializer.toJson(new Result(501, "Error: " + e));
        }
    }

}