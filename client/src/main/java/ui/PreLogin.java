package ui;

import chess.ChessColor;
import datamodels.UserData;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

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
        System.out.println(color + help + color.getResetString());
        return "";
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
            error(e.getMessage());
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
                return "";
            }
            email = getLine("E-mail");
        }
        try {
            String authToken = server.registerUser(new UserData(username, password, email));
            repl.setAuthToken(authToken);
            return "postLogin";
        } catch (ResponseException e) {
            if (e.statusCode() == 403) {
                error("Username is already taken.");
            } else {
                error(e.getMessage());
            }
        }
        return "";
    }

    private String getPassword(String prompt) {
        prompt = prompt == null ? "Password" : prompt;
        System.out.print(color.ternaryText() + prompt + ": " + color.getResetString());
        //The password should be hidden when compiled to jar.
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        String password;
        if (console == null) {
            password = scanner.nextLine();
        } else {
            password = new String(console.readPassword());
        }
        return password;
    }

    private void error(String message) {
        System.out.println(color.errorText().toString() + message + color.getResetString());
    }

    private String getLine(String prompt) {
        prompt = prompt == null ? "> " : prompt + ": ";
        System.out.print(color.ternaryText() + prompt + color.getResetString());
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
