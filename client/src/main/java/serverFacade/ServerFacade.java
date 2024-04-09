package serverFacade;

import com.google.gson.Gson;
import model.GameSummary;
import server.request.LoginRequest;
import server.request.RegisterRequest;
import server.result.LoginResult;
import server.result.RegisterResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
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

    public HttpURLConnection getRequest(String urlString) throws Exception {
        return request("GET", urlString, null, null);
    }

    public HttpURLConnection postRequest(String urlString) throws Exception {
        return request("POST", urlString, null, null);
    }

    public HttpURLConnection deleteRequest(String urlString) throws Exception {
        return request("DELETE", urlString, null, null);
    }

    public HttpURLConnection request(String method, String urlString) throws Exception {
        return request(method, urlString, null, null);
    }

    public HttpURLConnection request(String method, String urlString, String header) throws Exception {
        return request(method, urlString, header, null);
    }

    public HttpURLConnection request(String method, String urlString, String header, String body) throws Exception {
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
        return connection;
    }

    private static HttpURLConnection getConnection(String uri) throws Exception {
        URI formattedUri = new URI(uri);
        HttpURLConnection connection = (HttpURLConnection) formattedUri.toURL().openConnection();
        connection.setReadTimeout(5000);
        return connection;
    }

    public String login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String json = serializer.toJson(loginRequest);
        try {
            HttpURLConnection connection = request("POST", urlPath + "/session", null, json);
            int code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            try (InputStream respBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                HttpResponse response = new HttpResponse(code, message);
                LoginResult result = serializer.fromJson(inputStreamReader, LoginResult.class);
                if (response.status() != 200) {
                    handleError(response);
                    return null;
                }
                return result.authToken();
            }
        } catch (Exception e) {
            System.out.println("AN EXCEPTION!!!: " + e.getMessage());
            return null;
        }
    }

    public void logout(String authToken) {
        try {
            HttpURLConnection connection = request("DELETE", urlPath + "/session");
            connection.setDoOutput(true);
            connection.addRequestProperty("authorization", authToken);
            int code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            HttpResponse response = new HttpResponse(code, message);
            if (response.status() != 200) {
                handleError(response);
            }
        } catch (Exception e) {
            System.out.println("AN EXCEPTION!!!: " + e.getMessage());
        }
    }

    public Collection<GameSummary> listGames(String authToken) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    public String register(String username, String password, String email) {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        String json = serializer.toJson(registerRequest);
        try {
            HttpURLConnection connection = request("POST", urlPath + "/user", null, json);
            int code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            RegisterResult body;
            try (InputStream respBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                HttpResponse response = new HttpResponse(code, message);
                RegisterResult result = serializer.fromJson(inputStreamReader, RegisterResult.class);
                if (response.status() != 200) {
                    handleError(response);
                    return null;
                }
                return result.authToken();
            }
        } catch (Exception e) {
            System.out.println("AN EXCEPTION!!!: " + e.getMessage());
            return null;
        }
    }

    public String handleError(HttpResponse response) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }
}
