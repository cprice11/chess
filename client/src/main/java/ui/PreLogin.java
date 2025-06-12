package ui;

import chess.ChessColor;
import datamodels.UserData;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

import java.io.Console;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class PreLogin extends Client {
    ServerFacade server;
    Repl repl;
    ChessColor color = new ChessColor();

    public PreLogin(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
        help = color.secondaryText() + "The following commands are available:\n" +
                commandString("help, h", "Prints this message") + "\n" +
                commandString("login, l", new String[]{"Login to the chess server",
                        "optionally takes " + color.secondaryText() + "<username>" + color.lightText() +
                                " and " + color.secondaryText() + "<password>"}) + "\n" +
                commandString("register, r", new String[]{"Creates a new user",
                        "optionally takes " + color.secondaryText() + "<username>" + color.lightText() +
                                ", " + color.secondaryText() + "<password>" + color.lightText() +
                                " and " + color.secondaryText() + "<email>"}) + "\n" +
                commandString("quit, q", "Quit the program", color.errorText().toString());
        startMessage = color.secondaryText() +
                "Welcome to the C S 240 Chess client\n" +
                color.lightText() + "\tPlease enter a command";
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch (cmd) {
            case "q", "quit" -> {
                return "quit";
            }
            case "l", "login" -> login(params);
            case "r", "register" -> register(params);
            default -> printHelp();
        }
        if (repl.getAuthToken() == null) {
            return "";
        }
        return "postLogin";
    }

    private void login(String[] params) {
        String username;
        String password;
        if (params.length == 2) {
            username = params[0];
            password = params[1];
        } else if (params.length == 1) {
            username = params[0];
            System.out.println(color.primaryText() + "\tLogging in as '" + username + "'");
            password = getPassword(null);
        } else {
            if (params.length > 2) {
                System.out.println(color.primaryText() + "\tCouldn't understand login parameters, using manual entry.");
            }
            username = getLine("Username");
            password = getPassword(null);
        }
        try {
            System.out.println("logging in as '" + username + "'");
            repl.setAuthToken(server.loginUser(username, password));
            repl.setUsername(username);
        } catch (ResponseException e) {
            error(e.getMessage());
        }
    }

    private void register(String[] params) {
        String username;
        String password;
        String email;
        if (params.length == 3) {
            username = params[0];
            password = params[1];
            email = params[2];
        } else {
            warning("Couldn't understand register parameters. Using manual entry.");
            username = getLine("Username");
            password = getPassword(null);
            String confirmPassword = getPassword("Confirm password");
            if (!Objects.equals(confirmPassword, password)) {
                warning("Passwords do not match; please try again");
                return;
            }
            email = getLine("E-mail");
        }
        try {
            String authToken = server.registerUser(new UserData(username, password, email));
            repl.setAuthToken(authToken);
            repl.setUsername(username);
            System.out.println("logging in as '" + username + "'");
            System.out.println("logging in as '" + repl.getUsername() + "'");

        } catch (ResponseException e) {
            if (e.statusCode() == 403) {
                warning("Username is already taken; please try again");
            } else {
                error(e.getMessage());
            }
        }
    }

    private String getPassword(String prompt) {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        String password;
        prompt = prompt == null ? "Password" : prompt;
        prompt = color.ternaryText() + prompt + ": " + color.getResetString();
        if (console == null) {
            System.out.println(color.primaryText() + "\tPassword will be shown when not run from jar");
            System.out.print(prompt);
            password = scanner.nextLine();
        } else {
            System.out.println(color.primaryText() + "Password will be hidden");
            System.out.print(prompt);
            password = new String(console.readPassword());
        }
        return password;
    }
}
