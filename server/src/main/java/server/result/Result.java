package server.result;

import com.google.gson.Gson;

public class Result {
    int status;
    String message;

    public Result(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
