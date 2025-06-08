package server;

import chess.ChessGame;
import client.ResponseException;
import com.google.gson.Gson;
import datamodels.GameSummary;
import datamodels.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Hashtable;

public class ServerFacade {

    private final String serverUrl;
    private Hashtable<String, String> auth = new Hashtable<>();

    private record RegisterRequest(String username, String password, String email) {
    }

    private record RegisterResult(String authToken) {
    }

    private record LoginRequest(String username, String password) {
    }

    private record LoginResult(String authToken) {
    }

    private record LogoutRequest(String authToken) {
    }

    //    private record LogoutResult(String authToken){}
//    private record ListGamesRequest(){}
    private record ListGamesResult(Collection<GameSummary> games) {
    }

    private record CreateGameRequest(String gameName) {
    }

    private record CreateGameResult(int id) {
    }

    private record JoinGameRequest(int gameID, ChessGame.TeamColor playerColor) {
    }

    //    private record JoinGameResult(int id){}
    private record ObserveGameRequest(int gameID) {
    }

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void clear() throws ResponseException {
        makeRequest("DELETE", "/db", null, null, null);
    }

    public String registerUser(UserData user) throws ResponseException {
        RegisterResult result = makeRequest("POST", "/user", user, null, RegisterResult.class);
        return result.authToken();
    }

    public String loginUser(String username, String password) throws ResponseException {
        LoginRequest request = new LoginRequest(username, password);
        LoginResult result = makeRequest("POST", "/session", request, null, LoginResult.class);
        return result.authToken();
    }

    public void logoutUser(String authToken) throws ResponseException {
        authToken = authToken == null ? "" : authToken;
        auth.put("authorization", authToken);
        makeRequest("DELETE", "/session", authToken, auth, null);
    }

    public Collection<GameSummary> listGames(String authToken) throws ResponseException {
        authToken = authToken == null ? "" : authToken;
        auth.put("authorization", authToken);
        ListGamesResult result = makeRequest("GET", "/game", null, auth, ListGamesResult.class);
        return result.games();
    }

    public int createGame(String gameName, String authToken) throws ResponseException {
        authToken = authToken == null ? "" : authToken;
        auth.put("authorization", authToken);
        CreateGameRequest request = new CreateGameRequest(gameName);
        CreateGameResult result = makeRequest("GET", "/game", request, auth, CreateGameResult.class);
        return result.id();
    }

    public void joinGame(String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException {
        authToken = authToken == null ? "" : authToken;
        auth.put("authorization", authToken);
        JoinGameRequest request = new JoinGameRequest(gameID, color);
        makeRequest("PUT", "/game", request, auth, null);
    }

    private <T> T makeRequest(
            String method, String path, Object request, Hashtable<String, String> headers, Class<T> responseClass
    ) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            headers = headers == null ? new Hashtable<>() : headers;
            headers.forEach(http::setRequestProperty);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeHeader(HttpURLConnection http) {
        String header = http.getHeaderField("authorization");
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(status, respErr);
                }
            }
            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}