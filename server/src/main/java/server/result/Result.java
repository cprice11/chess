package server.result;

import com.google.gson.Gson;

public class Result {
    int code;
    String message;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
