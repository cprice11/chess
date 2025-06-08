package ui;

import chess.ChessColor;
import chess.ChessGame;
import client.ResponseException;
import datamodels.GameSummary;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;

public class PostLogin implements Client {
    private final ServerFacade server;
    private final Repl repl;
    private final ChessColor color = new ChessColor();
    private final Hashtable<Integer, Integer> gameTable = new Hashtable<>();
    private final String help = color.secondaryText() +
            "Enter one of the following commands:\n" +
            " - " + color.lightText() + "help\n" + color.ternaryText() +
            " - " + color.lightText() + "list\n" + color.ternaryText() +
            " - " + color.lightText() + "create\n" + color.ternaryText() +
            " - " + color.lightText() + "join\n" + color.ternaryText() +
            " - " + color.lightText() + "observe\n" + color.ternaryText() +
            " - " + color.errorText() + "quit\n" + color.ternaryText();

    public PostLogin(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        String returnVal = "";
        switch (cmd) {
            case "q", "quit" -> returnVal = "preLogin";
            case "c", "create" -> create(params);
            case "l", "list" -> list();
            case "j", "join" -> returnVal = join(params);
            case "o", "observe" -> returnVal = observe(params);
            default -> System.out.println(help());
        };
        return returnVal;
    }

    private void create(String[] params) {
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
        }
    }

    private void list() {
        try {
            ArrayList<GameSummary> games = new ArrayList<>(server.listGames(repl.getAuthToken()));
            gameTable.clear();
            ArrayList<GameSummary> easyIds = new ArrayList<>();
            for (int i = 0; i < games.size(); i++) {
                GameSummary game = games.get(i);
                easyIds.add(new GameSummary(i, game.blackUsername(), game.whiteUsername(), game.gameName()));
                gameTable.put(i, game.gameID());
            }
            printAsTable(easyIds);
        } catch (ResponseException e) {
            System.out.println(color.errorText() + e.getMessage());
        }
    }

    private void printAsTable(ArrayList<GameSummary> games) {
        ChessColor tableColor = new ChessColor().secondaryText();
        ChessColor tableContentsColor = new ChessColor().lightText();
        ArrayList<String> rows = new ArrayList<>();
        if (games.isEmpty()) {
            rows.add(color.secondaryText() + " ╭────────────────╮");
            rows.add(color.secondaryText() + " │ " + color.lightText() + "Games          " + color.secondaryText() + "│");
            rows.add(color.secondaryText() + " ├────────────────┤");
            rows.add(color.secondaryText() + " │ " + color.lightText() + "No games found " + color.secondaryText() + "│");
            rows.add(color.secondaryText() + " ╰────────────────╯" + color.getResetString());
            rows.forEach(System.out::println);
            return;
        }
        int longestID = 1;
        int longestGame = 1;
        int longestBlack = 1;
        int longestWhite = 1;
        for (GameSummary game : games) {
            int nextID = String.valueOf(game.gameID()).length();
            int nextGame = game.gameName() == null ? 0 : game.gameName().length();
            int nextBlack = game.blackUsername() == null ? 0 : game.blackUsername().length();
            int nextWhite = game.whiteUsername() == null ? 0 : game.whiteUsername().length();
            longestID = Math.max(nextID, longestID);
            longestGame = Math.max(nextGame, longestGame);
            longestBlack = Math.max(nextBlack, longestBlack);
            longestWhite = Math.max(nextWhite, longestWhite);
        }
        StringBuilder topRow = new StringBuilder();
        int contentWidth = longestID + longestGame + longestBlack + longestWhite + 11;
        topRow.append(tableColor).append(" ╭");
        topRow.append("─".repeat(Math.max(0, contentWidth)));
        topRow.append("╮");
        rows.add(topRow.toString());
        StringBuilder title = new StringBuilder();
        title.append(tableColor).append(" │");
        title.append(tableContentsColor).append(String.format("%-" + contentWidth + "s", " Games "));
        title.append(tableColor).append("│");
        rows.add(title.toString());
        StringBuilder spacerRow = new StringBuilder();
        spacerRow.append(tableColor);
        spacerRow.append(" ├─");
        spacerRow.append(String.format("%-" + longestID + "s", "")).append("─┬─");
        spacerRow.append(String.format("%-" + longestGame + "s", "")).append("─┬─");
        spacerRow.append(String.format("%-" + longestBlack + "s", "")).append("─┬─");
        spacerRow.append(String.format("%-" + longestWhite + "s", "")).append("─┤");
        rows.add(spacerRow.toString());
        for (GameSummary game : games) {
            String id = String.valueOf(game.gameID()).formatted("%" + longestID + "s");
            String name = String.format("%-" + longestGame + "s", game.gameName());
            String blackName = game.blackUsername() == null ? "-" : game.blackUsername();
            String whiteName = game.whiteUsername() == null ? "-" : game.whiteUsername();
            String black = String.format("%-" + longestBlack + "s", blackName);
            String white = String.format("%-" + longestWhite + "s", whiteName);
            StringBuilder gameRow = new StringBuilder();
            gameRow.append(tableColor + " │ ");
            gameRow.append(tableContentsColor + id + tableColor + " │ ");
            gameRow.append(tableContentsColor + name + tableColor + " │ ");
            gameRow.append(tableContentsColor + black + tableColor + " │ ");
            gameRow.append(tableContentsColor + white + tableColor + " │ ");
            rows.add(gameRow.toString());
        }
        rows.forEach((String row) -> System.out.println(row));
    }

    private String join(String[] params) {
        int gameIndex;
        String teamColor;
        if (params.length == 2) {
            gameIndex = Integer.parseInt(params[0]);
            teamColor = params[1];
        } else {
            gameIndex = Integer.parseInt(getLine("Game number"));
            teamColor = getLine("Team Color (w/b)");
        }
        teamColor = teamColor.toLowerCase();
        ChessGame.TeamColor team;
        if (teamColor.equals("w") || teamColor.equals("white")) {
            team = ChessGame.TeamColor.WHITE;
        } else if (teamColor.equals("b") || teamColor.equals("black")) {
            team = ChessGame.TeamColor.BLACK;
        } else {
            System.out.println(color.errorText() + "Cannot parse requested team: " + teamColor);
            return "";
        }
        Integer gameID = gameTable.get(gameIndex);
        if (gameID == null) {
            System.out.println(color.errorText() + "Game " + gameIndex + " does not exist");
            return "";
        }
        try {
            server.joinGame(repl.getAuthToken(), gameID, team);
            System.out.println("Joined game");
            repl.setCurrentGame(gameID);
            return "gamePlay";
        } catch (ResponseException e) {
            System.out.println(color.errorText() + e.getMessage());
            return "";
        }
    }

    private String observe(String[] params) {
        int gameIndex;
        if (params.length == 1) {
            gameIndex = Integer.parseInt(params[0]);
        } else {
            gameIndex = Integer.parseInt(getLine("Game number"));
        }
        Integer gameID = gameTable.get(gameIndex);
        if (gameID == null) {
            System.out.println(color.errorText() + "Game " + gameIndex + " does not exist");
            return "";
        }
        System.out.println("Observe game");
        repl.setCurrentGame(gameID);
        return "gamePlay";
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
