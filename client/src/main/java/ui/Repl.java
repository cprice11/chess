package ui;

import java.util.Scanner;

public class Repl {
    private final Client preLoginClient;
    private final Client postLoginClient;
    private final Client gamePlayclient;

    public Repl(String serverUrl) {
        preLoginClient = new PreLogin(serverUrl, this);
        postLoginClient = new PostLogin(serverUrl, this);
        gamePlayclient = new GamePlay(serverUrl, this);
    }

    public void run() {
        Client currentClient = preLoginClient;
        System.out.println("\uD83D\uDC36 Chess client");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = currentClient.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result + EscapeSequences.RESET_TEXT_COLOR);
                currentClient = switch (result) {
                    case "preLogin" -> preLoginClient;
                    case "postLogin" -> postLoginClient;
                    case "gamePlay" -> gamePlayclient;
                    default -> currentClient;
                };

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + ">>> " + EscapeSequences.RESET_TEXT_COLOR);
    }
}
