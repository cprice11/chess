package ui;

import chess.ChessColor;
import chess.ChessGame;
import serverfacade.ServerFacade;

import javax.websocket.MessageHandler;
import java.util.Scanner;

public class Repl {
    private final Client preLoginClient;
    private final Client postLoginClient;
    private final Client gamePlayclient;
    private Client currentClient;

    private String username = "";
    private String authToken;
    private final ChessColor color = new ChessColor();
    private int currentGameID;
    ChessGame currentGame = new ChessGame();
    ConsolePrinter printer = new ConsolePrinter(currentGame);

    public Repl(String serverUrl) {
        ServerFacade server;
        MessageHandler messageHandler = new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                handleServerMessage(message);
            }
        };
        server = new ServerFacade(serverUrl, messageHandler);
        preLoginClient = new PreLogin(server, this);
        postLoginClient = new PostLogin(server, this);
        gamePlayclient = new GamePlay(server, this);
    }

    public void run() {
        currentClient = preLoginClient;
        currentClient.printStartMessage();
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
                    currentClient.printStartMessage();
                    if (newClient == gamePlayclient) {
                        printer.print();
                    }
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println("Quitting...");
    }

    private void handleServerMessage(String message) {
        currentClient.handleServerMessage(message);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCurrentGameID() {
        return currentGameID;
    }

    public void setCurrentGameID(int currentGameID) {
        this.currentGameID = currentGameID;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void printPrompt() {
        System.out.print(color.secondaryText() + ">>> " + ChessColor.RESET);
    }
}
