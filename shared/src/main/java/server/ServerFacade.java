package server;

import chess.ChessGame;
import com.google.gson.Gson;
import datamodels.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String registerUser(UserData user) {
        RegisterResult result = makeRequest("POST", "/user", user, RegisterResult.class);
        return result.authToken();
    }

    public LoginResult loginUser(String username, String password) {
        record loginRequest(String username, String password) {
        }
        ;
        loginRequest request = new loginRequest(username, password);
        return makeRequest("POST", "/session", request, LoginResult.class);
    }

    public LogoutResult logoutUser(String authToken) {
        return makeRequest("DELETE", "/session", authToken, LogoutResult.class);
    }

    public ListGamesResult listGames(String authToken) {
        return makeRequest("GET", "/game", authToken, ListGamesResult.class);
    }

    public CreateGameResult createGame(String gameName, String authToken) {
        record CreateGameRequest(String gameName, String authToken) {
        }
        ;
        CreateGameRequest request = new CreateGameRequest(gameName, authToken);
        return makeRequest("GET", "/game", request, CreateGameResult.class);
    }

    public JoinGameResult joinGame(String authToken, int gameID, ChessGame.TeamColor color) {
        record JoinGameRequest(String authToken, int gameID, ChessGame.TeamColor color) {
        }
        ;
        JoinGameRequest request = new JoinGameRequest(authToken, gameID, color);
        return makeRequest("PUT", "/game", request, JoinGameResult.class);

    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw new RuntimeException("Not implemented");
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