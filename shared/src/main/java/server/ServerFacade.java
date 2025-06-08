package server;

import chess.ChessGame;
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

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void clear() {
        makeRequest("DELETE", "/db", null, null, null);
    }

    public String registerUser(UserData user) {
        RegisterResult result = makeRequest("POST", "/user", user, null, RegisterResult.class);
        return result.authToken();
    }

    public String loginUser(String username, String password) {
        record loginRequest(String username, String password) {
        }
        loginRequest request = new loginRequest(username, password);
        LoginResult result = makeRequest("POST", "/session", request, null, LoginResult.class);
        return result.authToken();
    }

    public void logoutUser(String authToken) {
        Hashtable<String, String> headers = new Hashtable<>();
        headers.put("authorization", authToken);
        makeRequest("DELETE", "/session", authToken, headers, LogoutResult.class);
    }

    public Collection<GameSummary> listGames(String authToken) {
        Hashtable<String, String> headers = new Hashtable<>();
        headers.put("authorization", authToken);
        record ListGamesResult(Collection<GameSummary> games) {
        }
        ;
        ListGamesResult result = makeRequest("GET", "/game", null, headers, ListGamesResult.class);
        Collection<GameSummary> games = result.games();
        return games;
    }

    public int createGame(String gameName, String authToken) {
        record CreateGameRequest(String gameName) {
        }
        Hashtable<String, String> headers = new Hashtable<>();
        headers.put("authorization", authToken);
        CreateGameRequest request = new CreateGameRequest(gameName);
        CreateGameResult result = makeRequest("GET", "/game", request, headers, CreateGameResult.class);
        return result.id();
    }

    public JoinGameResult joinGame(String authToken, int gameID, ChessGame.TeamColor color) {
        record JoinGameRequest(String authToken, int gameID, ChessGame.TeamColor color) {
        }
        ;
        JoinGameRequest request = new JoinGameRequest(authToken, gameID, color);
        return makeRequest("PUT", "/game", request, null, JoinGameResult.class);

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
                    throw new RuntimeException(respErr.toString());
//                    throw ResponseException.fromJson(respErr);
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