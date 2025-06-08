package ui;

import chess.ChessColor;
import client.ResponseException;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PostLogin implements Client {
    ServerFacade server;
    Repl repl;
    ChessColor color = new ChessColor();
    private final String help = color.secondaryText() +
            "Enter one of the following commands:\n" +
            " - " + color.lightText() + "help\n" + color.secondaryText() +
            " - " + color.lightText() + "login\n" + color.secondaryText() +
            " - " + color.lightText() + "register\n" + color.secondaryText() +
            " - " + color.errorText() + "quit\n" + color.secondaryText();

    public PostLogin(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "q", "quit" -> "quit";
            case "c", "create" -> create(params);
            case "l", "list" -> list();
            case "p", "play" -> play(params);
            case "o", "observe" -> observe(params);
            default -> help();
        };
    }

    private String create(String[] params) {
        String gameName;
        if (params.length == 1) {
            gameName = params[0];
        } else {
            gameName = getLine("Game name");
        }
        try {
            repl.setCreatedGame(server.createGame(gameName, repl.getAuthToken()));
            System.out.println("Created game: " + gameName);
        } catch (ResponseException e) {
            System.out.println(color.errorText() + e.getMessage());
            return "";
        }
    }

    private String list() {
        return "";
    }

    private String play(String[] params) {
        return "";
    }

    private String observe(String[] params) {
        return "";
    }

    public String help() {
        color.secondaryText();
        return color + help + color.getResetString();
    }

    private String getLine(String prompt) {
        prompt = prompt == null ? "> " : prompt + ": ";
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + prompt + EscapeSequences.RESET_TEXT_COLOR);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
