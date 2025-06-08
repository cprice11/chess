package ui;

import chess.ChessColor;
import client.ResponseException;
import datamodels.UserData;
import server.ServerFacade;

import java.io.Console;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class PreLogin implements Client {
    ServerFacade server;
    Repl repl;
    ChessColor color = new ChessColor();
    private final String help = color.secondaryText() +
            "Enter one of the following commands:\n" +
            " - " + color.lightText() + "help\n" + color.secondaryText() +
            " - " + color.lightText() + "login\n" + color.secondaryText() +
            " - " + color.lightText() + "register\n" + color.secondaryText() +
            " - " + color.errorText() + "quit\n" + color.secondaryText();

    public PreLogin(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
//        try {
//            server.clear();
//        } catch (ResponseException e) {
//            throw new RuntimeException(e.getMessage());
//        }
        this.repl = repl;
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "q", "quit" -> "quit";
            case "l", "login" -> login(params);
            case "r", "register" -> register(params);
            default -> help();
        };
    }

    public String help() {
        color.secondaryText();
        return color + help + color.getResetString();
    }

    private String login(String[] params) {
        String username;
        String password;
        if (params.length == 2) {
            username = params[0];
            password = params[1];
        } else {
            username = getLine("Username");
            password = getPassword(null);
        }
        try {
            repl.setAuthToken(server.loginUser(username, password));
            return "postLogin";
        } catch (ResponseException e) {
            System.out.println(color.errorText().toString() + e.StatusCode());
            System.out.println(color.errorText() + e.getMessage() + color.getResetString());
        }
        return "";
    }

    private String register(String[] params) {
        String username;
        String password;
        String email;
        if (params.length == 3) {
            username = params[0];
            password = params[1];
            email = params[2];
        } else {
            username = getLine("Username");
            password = getPassword(null);
            String confirmPassword = getPassword("Confirm password");
            if (!Objects.equals(confirmPassword, password)) {
                System.out.println("Passwords do not match");
                return null;
            }
            email = getLine("E-mail");
        }
        try {
            String authToken = server.registerUser(new UserData(username, password, email));
            repl.setAuthToken(authToken);
            return "postLogin";
        } catch (ResponseException e) {
            if (e.StatusCode() == 403) {
                System.out.println(color.errorText() + "Username is already taken.");
            } else {
                System.out.println(color.errorText().toString() + e.StatusCode());
                System.out.println(color.errorText().toString() + e.getMessage() + color.getResetString());
            }
        }
        return "";
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + ">>> " + EscapeSequences.RESET_TEXT_COLOR);
    }

    private String getPassword(String prompt) {
        prompt = prompt == null ? "Password" : prompt;
        System.out.print(color.ternaryText() + prompt + ": " + color.getResetString());
        //The password should be hidden when compiled to jar.
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        String password = "";
        if (console == null) {
            password = scanner.nextLine();
        } else {
            password = new String(console.readPassword());
        }
        return password;
    }

    private String getLine(String prompt) {
        prompt = prompt == null ? "> " : prompt + ": ";
        System.out.print(color.ternaryText() + prompt + color.getResetString());
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
