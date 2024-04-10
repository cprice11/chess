package serverFacade;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameSummary;
import server.request.CreateGameRequest;
import server.request.JoinGameRequest;
import server.request.LoginRequest;
import server.request.RegisterRequest;
import server.result.CreateGameResult;
import server.result.ListGamesResult;
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

    public HttpURLConnection request(String method, String urlString) throws Exception {
        return request(method, urlString, null);
    }

    public HttpURLConnection request(String method, String urlString, String body) throws Exception {
        HttpURLConnection connection = getConnection(urlString);
        connection.setRequestMethod(method);
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
            HttpURLConnection connection = request("POST", urlPath + "/session", json);
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

    public int createGame(String authToken, String gameName) {
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        String json = serializer.toJson(createGameRequest);
        try {
            HttpURLConnection connection = request("POST", urlPath + "/game");
            connection.setDoOutput(true);
            connection.addRequestProperty("authorization", authToken);
            connection.addRequestProperty("Content-Type", "application/json");
            try (var outputStream = connection.getOutputStream()) {
                outputStream.write(json.getBytes());
            }
            int code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            try (InputStream respBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                HttpResponse response = new HttpResponse(code, message);
                CreateGameResult result = serializer.fromJson(inputStreamReader, CreateGameResult.class);
                if (response.status() != 200) {
                    handleError(response);
                    return -1;
                }
                return result.gameID();
            }
        } catch (Exception e) {
            System.out.println("AN EXCEPTION!!!: " + e.getMessage());
            return -1;
        }
    }

    public void joinGame(String authToken, int gameId, ChessGame.TeamColor color) {
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, color, gameId);
        String json = serializer.toJson(joinGameRequest);
        try {
            HttpURLConnection connection = request("PUT", urlPath + "/game");
            connection.setDoOutput(true);
            connection.addRequestProperty("authorization", authToken);
            connection.addRequestProperty("Content-Type", "application/json");
            try (var outputStream = connection.getOutputStream()) {
                outputStream.write(json.getBytes());
            }
            int code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            HttpResponse response = new HttpResponse(code, message);
            if (response.status() != 200) {
                handleError(response);
            }
        } catch (
                Exception e) {
            System.out.println("AN EXCEPTION!!!: " + e.getMessage());
        }

    }

    public Collection<GameSummary> listGames(String authToken) {
        try {
            HttpURLConnection connection = request("GET", urlPath + "/game");
            connection.setDoOutput(true);
            connection.addRequestProperty("authorization", authToken);
            int code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            try (InputStream respBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                HttpResponse response = new HttpResponse(code, message);
                ListGamesResult result = serializer.fromJson(inputStreamReader, ListGamesResult.class);
                if (response.status() != 200) {
                    handleError(response);
                    return null;
                }
                return result.games();
            }
        } catch (Exception e) {
            System.out.println("AN EXCEPTION!!!: " + e.getMessage());
            return null;
        }
    }

    public String register(String username, String password, String email) {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        String json = serializer.toJson(registerRequest);
        try {
            HttpURLConnection connection = request("POST", urlPath + "/user", json);
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
