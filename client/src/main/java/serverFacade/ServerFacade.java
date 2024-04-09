package serverFacade;

import com.google.gson.Gson;
import model.GameSummary;
import model.UserData;
import server.request.RegisterRequest;
import server.result.RegisterResult;
import server.result.Result;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {
    protected static String url;
    protected static int port;
    protected static String urlPath;
    protected static Gson serializer = new Gson();

    public ServerFacade(int portNum, String urlString) {
        port = portNum;
        url = urlString;
        urlPath = urlString + portNum;
    }

    public HttpResponse getRequest(String urlString) throws Exception {
        return request("GET", urlString, null, null);
    }

    public HttpResponse postRequest(String urlString) throws Exception {
        return request("POST", urlString, null, null);
    }

    public HttpResponse deleteRequest(String urlString) throws Exception {
        return request("DELETE", urlString, null, null);
    }

    public HttpResponse request(String method, String urlString) throws Exception {
        return request(method, urlString, null, null);
    }

    public HttpResponse request(String method, String urlString, String header) throws Exception {
        return request(method, urlString, header, null);
    }

    public HttpResponse request(String method, String urlString, String header, String body) throws Exception {
        HttpURLConnection connection = getConnection(urlString);
        connection.setRequestMethod(method);
        if (header != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty("Content-Type", "application/json");
            try (var outputStream = connection.getOutputStream()) {
                outputStream.write(header.getBytes());
            }
        }
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty("Content-Type", "application/json");
            try (var outputStream = connection.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
        connection.connect();
        int code = connection.getResponseCode();
        String message = connection.getResponseMessage();
        try (InputStream respBody = connection.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return new HttpResponse(
                    code,
                    message,
                    inputStreamReader.toString());
        }
    }

    private static HttpURLConnection getConnection(String uri) throws Exception {
        URI formattedUri = new URI(uri);
        HttpURLConnection connection = (HttpURLConnection) formattedUri.toURL().openConnection();
        connection.setReadTimeout(5000);
        return connection;
    }

    public String login(String username, String password) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    public Collection<GameSummary> listGames(String authToken) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    public String register(String username, String password, String email) {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        String json = serializer.toJson(registerRequest);
        try {
            HttpResponse response = postRequest(urlPath + "/user");
            if (response.status() != 200) {
                handleError(response);
                return null;
            }
            RegisterResult result = serializer.fromJson(response.Body(), RegisterResult.class);
            return result.authToken();
        } catch (Exception e) {
            System.out.println("AN EXCEPTION!!!: " + e.getMessage());
            return null;
        }
    }

    public String handleError(HttpResponse response) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }
}
