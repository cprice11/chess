import ui.LoginUi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static ui.EscapeSequences.*;

public class Client {

    private static final String BASE_URL = "lcalhost:8736";

    public static void ClientUi(String[] args) {
        LoginUi login = new LoginUi();
        login.start();
    }

    private void register() {
        System.out.print(ERASE_SCREEN);
        // Implement registration logic
    }

    private String login() {
        System.out.print(ERASE_SCREEN);
        // Implement login logic
        return "auth_token"; // Replace with actual auth token
    }

    private void selectGame(String authToken) {
        // Implement logic to fetch available games and select one
    }

    private static void startGameplay() {
        // Implement chess gameplay logic
    }

    // Method to make HTTP GET request
    private static String sendGet(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
