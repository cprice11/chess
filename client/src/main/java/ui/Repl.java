package ui;

import chess.ChessColor;
import chess.ChessGame;

import java.util.Scanner;

public class Repl {
    private final Client preLoginClient;
    private final Client postLoginClient;
    private final Client gamePlayclient;
    private String authToken;
    private ChessColor color = new ChessColor();
    private int currentGameID;
    ChessGame currentGame = new ChessGame();
    ConsolePrinter printer = new ConsolePrinter(currentGame);

    public ChessGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(ChessGame currentGame) {
        this.currentGame = currentGame;
    }

    public int getCurrentGameID() {
        return currentGameID;
    }

    public void setCurrentGameID(int currentGameID) {
        this.currentGameID = currentGameID;
    }

    private int createdGame;

    public Repl(String serverUrl) {
        preLoginClient = new PreLogin(serverUrl, this);
        postLoginClient = new PostLogin(serverUrl, this);
        gamePlayclient = new GamePlay(serverUrl, this);
    }

    public void run() {
        Client currentClient = preLoginClient;
        System.out.print(currentClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = currentClient.eval(line);
                Client newClient = switch (result) {
                    case "preLogin" -> preLoginClient;
                    case "postLogin" -> postLoginClient;
                    case "gamePlay" -> gamePlayclient;
                    default -> currentClient;
                };
                if (newClient != currentClient) {
                    currentClient = newClient;
                    System.out.println(currentClient.help());
                    if (newClient == gamePlayclient) {
                        printer.print();
                    }
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getCreatedGame() {
        return createdGame;
    }

    public void setCreatedGame(int createdGame) {
        this.createdGame = createdGame;
    }

    private void printPrompt() {
        System.out.print(color.secondaryText() + ">>> " + color.getResetString());
    }
}
