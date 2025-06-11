package ui;

import chess.ChessColor;
import chess.ChessGame;
import datamodels.GameSummary;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;

public class PostLogin implements Client {
    private final ServerFacade server;
    private final Repl repl;
    private final ChessColor color = new ChessColor();
    private final Hashtable<Integer, Integer> gameTable = new Hashtable<>();
    private int longestID = 2;
    private int longestGame = 9;
    private int longestBlack = 14;
    private int longestWhite = 14;
    private final String tableColor = new ChessColor().secondaryText().toString();
    private final String tableContentsColor = new ChessColor().lightText().toString();
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
        }
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
        ArrayList<String> rows = new ArrayList<>();
        if (games.isEmpty()) {
            rows.add(formatRow("", "╭", '─', 16, "╮", ""));
            rows.add(formatRow("Games", "│", ' ', 16, "│", tableContentsColor));
            rows.add(formatRow("", "│", '─', 16, "│", ""));
            rows.add(formatRow("No games", "│", ' ', 16, "│", color.errorText().toString()));
            rows.add(formatRow("", "╰", '─', 16, "╯" + color.getResetString(), ""));
            rows.forEach(System.out::println);
            return;
        }
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
        int contentWidth = longestID + longestGame + longestBlack + longestWhite + 11;
        rows.add(formatRow("", "", "", "", "╭", "─", "╮", '─', true));
        rows.add(formatRow("Games", "│", ' ', contentWidth, "│", tableContentsColor));
        String spacerRow = formatRow("", "", "", "", "├", "┬", "┤", '─', true);
        rows.add(spacerRow);
        rows.add(formatRow("ID", "Game Name", "Black Username", "White Username"));
        rows.add(spacerRow.replace('┬', '┼'));
        for (GameSummary game : games) {
            rows.add(formatRow(String.valueOf(game.gameID()), game.gameName(), game.blackUsername(), game.whiteUsername()));
        }
        rows.add(formatRow("", "", "", "", "└", "┴", "┘", '─', true));
        rows.forEach(System.out::println);
    }

    private String formatRow(String onlyEntry, String left, char fill, int width, String right, String color) {
        onlyEntry = onlyEntry == null ? "" : " " + onlyEntry.strip() + " ";
        return " " + tableColor + left +
                color + String.format("%-" + width + "s", onlyEntry).replace(' ', fill) +
                tableColor + right;
    }

    private String formatRow(String id, String name, String black, String white) {
        return formatRow(id, name, black, white, "|", "|", "|", ' ', false);
    }

    private String formatRow(String id, String name, String black, String white,
                             String leftBorder, String middleBorder, String rightBorder, char fillChar, boolean monoColor) {
        String contentColor = monoColor ? tableColor : tableContentsColor;
        id = id == null ? "" : id;
        name = name == null ? "" : name;
        black = black == null ? "-" : black;
        white = white == null ? "-" : white;
        id = " " + id.strip() + " ";
        name = " " + name.strip() + " ";
        black = " " + black.strip() + " ";
        white = " " + white.strip() + " ";
        return " " + tableColor + leftBorder + contentColor +
                String.format("%-" + (longestID + 2) + "s", id).replace(' ', fillChar) +
                tableColor + middleBorder + contentColor +
                String.format("%-" + (longestGame + 2) + "s", name).replace(' ', fillChar) +
                tableColor + middleBorder + contentColor +
                String.format("%-" + (longestBlack + 2) + "s", black).replace(' ', fillChar) +
                tableColor + middleBorder + contentColor +
                String.format("%-" + (longestWhite + 2) + "s", white).replace(' ', fillChar) +
                tableColor + rightBorder;
    }

    private String join(String[] params) {
        int gameIndex;
        String teamColor;
        if (params.length == 2) {
            try {
              gameIndex = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                error("Cannot understand game number");
                return "";
            }
            teamColor = params[1];
        } else {
            try {
                gameIndex = Integer.parseInt(getLine("Game number"));
            } catch (NumberFormatException e) {
                error("Cannot understand game number");
                return "";
            }
            teamColor = getLine("Team Color (w/b)");
        }
        teamColor = teamColor.toLowerCase();
        ChessGame.TeamColor team;
        if (teamColor.equals("w") || teamColor.equals("white")) {
            team = ChessGame.TeamColor.WHITE;
        } else if (teamColor.equals("b") || teamColor.equals("black")) {
            team = ChessGame.TeamColor.BLACK;
        } else {
            error("Cannot parse requested team: " + teamColor);
            return "";
        }
        Integer gameID = gameTable.get(gameIndex);
        if (gameID == null) {
            error("Game " + gameIndex + " does not exist");
            return "";
        }
        try {
            server.joinGame(repl.getAuthToken(), gameID, team);
            System.out.println("Joined game");
            repl.setCurrentGameID(gameID);
            if (team == ChessGame.TeamColor.WHITE) {
                repl.printer.fromWhite();
            } else {
                repl.printer.fromBlack();
            }
            return "gamePlay";
        } catch (ResponseException e) {
            error(e.getMessage());
            return "";
        }
    }

    private String observe(String[] params) {
        int gameIndex;
        try {
            if (params.length == 1) {
                gameIndex = Integer.parseInt(params[0]);
            } else {
                gameIndex = Integer.parseInt(getLine("Game number"));
            }
        } catch (NumberFormatException e) {
            error("Cannot understand game number");
            return "";
        }
        Integer gameID = gameTable.get(gameIndex);
        if (gameID == null) {
            error("Game " + gameIndex + " does not exist");
            return "";
        }
        System.out.println("Observe game");
        repl.setCurrentGameID(gameID);
        return "gamePlay";
    }

    public String help() {
        color.secondaryText();
        return color + help + color.getResetString();
    }

    private void error(String message) {
        System.out.println(color.errorText().toString() + message + color.getResetString());
    }

    private String getLine(String prompt) {
        prompt = prompt == null ? "> " : prompt + ": ";
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + prompt + EscapeSequences.RESET_TEXT_COLOR);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
