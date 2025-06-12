package ui;

import chess.ChessColor;
import chess.ChessGame;
import datamodels.GameSummary;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class PostLogin extends Client {
    private final Hashtable<Integer, Integer> gameTable = new Hashtable<>();
    private int longestID = 2;
    private int longestGame = 9;
    private int longestBlack = 14;
    private int longestWhite = 14;
    private final String tableColor = color.secondaryText().toString();
    private final String tableContentsColor = color.lightText().toString();

    public PostLogin(ServerFacade server, Repl repl) {
        this.server = server;
        this.repl = repl;
        help = color.secondaryText() +
                "The following commands are available:\n" +
                commandString("help, h", "Show this help message\n") +
                commandString("list, l", "List games on the server\n") +
                commandString("create, c", new String[]{"Create a new game",
                        "optionally takes " + color.secondaryText() + "<gameName>\n"}) +
                commandString("join", new String[]{"Join a game",
                        "optionally takes " + color.secondaryText() + "<gameID>" + color.lightText() + " and " +
                                color.secondaryText() + "<preferredColor>" + color.lightText() + " (white/black)\n"}) +
                commandString("observe", new String[]{"Join a game as an observer",
                        "optionally takes " + color.secondaryText() + "<gameID>\n"}) +
                commandString("logout", "Logout from the current session\n") +
                commandString("quit", "Quit the program", color.errorText().toString());
        startMessage = color.secondaryText() + "C S 240 Chess client";
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch (cmd) {
            case "q", "quit", "exit" -> {
                logout();
                return "quit";
            }
            case "c", "create" -> create(params);
            case "l", "list", "games" -> list();
            case "logout" -> {
                logout();
                return "preLogin";
            }
            case "j", "join" -> {
                if (join(params)) {
                    return "gamePlay";
                }
            }
            case "o", "observe" -> {
                if (observe(params)) {
                    return "gamePlay";
                }
            }
            default -> printHelp();
        }
        return "";
    }

    public void handleServerMessage(String message) {
        error("Received server message");
    }

    @Override
    public void printStartMessage() {
        list();
        System.out.println(startMessage);
        System.out.println("User: " + color.primaryText() + this.repl.getUsername() +
                color.lightText() + "\n\tPlease enter a command");
    }

    private void logout() {
        try {
            server.logoutUser(repl.getAuthToken());
            System.out.println("Logging out");
            repl.setAuthToken(null);
        } catch (ResponseException ignored) {
        }
    }

    private void create(String[] params) {
        String gameName;
        if (params.length == 1) {
            gameName = params[0];
        } else {
            gameName = getLine("Game name");
        }
        try {
            server.createGame(gameName, repl.getAuthToken());
            System.out.println("Created game: " + gameName);
            list();
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

    private boolean join(String[] params) {
        int gameIndex;
        String teamColor;
        try {
            if (params.length == 2) {
                gameIndex = Integer.parseInt(params[0]);
                teamColor = params[1];
            } else {
                gameIndex = Integer.parseInt(getLine("Game number"));
                teamColor = getLine("Team Color (w/b)");
            }
        } catch (NumberFormatException e) {
            error("Cannot understand game number");
            return false;
        }
        teamColor = teamColor.toLowerCase();
        ChessGame.TeamColor team;
        if (teamColor.equals("w") || teamColor.equals("white")) {
            team = ChessGame.TeamColor.WHITE;
        } else if (teamColor.equals("b") || teamColor.equals("black")) {
            team = ChessGame.TeamColor.BLACK;
        } else {
            error("Cannot parse requested team: " + teamColor);
            return false;
        }
        Integer gameID = gameTable.get(gameIndex);
        if (gameID == null) {
            error("Game " + gameIndex + " does not exist");
            return false;
        }
        try {
            server.joinGame(repl.getAuthToken(), gameID, team);
            System.out.println("Joining game " + gameID);
            repl.setCurrentGameID(gameID);
            server.sendConnect(repl.getAuthToken(), gameID);
            if (team == ChessGame.TeamColor.WHITE) {
                repl.printer.fromWhite();
            } else {
                repl.printer.fromBlack();
            }
            return true;
        } catch (Exception e) {
            error(e.getMessage());
            return false;
        }
    }

    private boolean observe(String[] params) {
        int gameIndex;
        try {
            if (params.length == 1) {
                gameIndex = Integer.parseInt(params[0]);
            } else {
                gameIndex = Integer.parseInt(getLine("Game number"));
            }
        } catch (NumberFormatException e) {
            error("Cannot understand game number");
            return false;
        }
        Integer gameID = gameTable.get(gameIndex);
        if (gameID == null) {
            error("Game " + gameIndex + " does not exist");
            return false;
        }

        try {
            server.sendConnect(repl.getAuthToken(), gameID);
        } catch (IOException e) {
            error(e.getMessage());
        }
        repl.setCurrentGameID(gameID);
        return true;
    }

    private void printAsTable(ArrayList<GameSummary> games) {
        ArrayList<String> rows = new ArrayList<>();
        if (games.isEmpty()) {
            rows.add(formatRow("", "╭", '─', 16, "╮", ""));
            rows.add(formatRow("Games", "│", ' ', 16, "│", tableContentsColor));
            rows.add(formatRow("", "│", '─', 16, "│", ""));
            rows.add(formatRow("No games", "│", ' ', 16, "│", color.errorText().toString()));
            rows.add(formatRow("", "╰", '─', 16, "╯" + ChessColor.RESET, ""));
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
        id = id == null ? "" : " " + id.strip() + " ";
        name = name == null ? "" : " " + name.strip() + " ";
        black = black == null ? " - " : " " + black.strip() + " ";
        white = white == null ? " - " : " " + white.strip() + " ";
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
}
