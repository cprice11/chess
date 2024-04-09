package serverFacade;

import server.result.Result;

import java.io.InputStreamReader;

public record HttpResponse (int status, String message){
}
