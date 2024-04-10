package ui;

import chess.ChessGame;
import model.GameSummary;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionUi extends UI {
    private static boolean first = true;
    TerminalWindow window = new TerminalWindow(Screen.terminalWidth, Screen.terminalHeight - 1);
    Screen screen = new Screen();

    private static final String HELP_INTRO =
            """
            MENU MANUAL
                From this menu you can lookup starting or ongoing games as well as create a
                game or log out. All commands are case insensitive but should be separated 
                by whitespace.
            """;
    private static final String HELP_HELP = """
            HELP
                [H|h]
                    To see this help menu enter 'h' on the navigation screen.
                    
                -[COMMAND]
                        To see explanations of an individual command try entering
                        'h [COMMAND]' to see more details (e.g. 'h join').
            """;
    private static final String HELP_SEARCH = """
            SEARCH
                [S|s]
                    Brings up the search bar and shows a list of random games
                
                -u [user]
                      Brings up games where the given user is participating
                
                -g [gameName]
                      Brings up games with the given name.
                      (e.g. 's -g "Friday morning match" ')
            """;
    private static final String HELP_CREATE = """
               CREATE
                [C|c]
                    Will create a new game on the server. Options exist for specifying 
                    parameters.
                    
                    -
                        Entering 'c' alone will prompt for a name to give the game.
            
                    - [gameName] [color]
                        will attempt to create a game and join as the specified color
                        (w)hite, (b)lack, (e)ither, (o)bserver, or (a)lso to join as both 
                        white and black.
            
                    - [gameName]
                        If no color is specified the program will only create a game and will
                        not join it.
            """;
    private static final String HELP_JOIN = """
            JOIN
                [J|j]
                    Attempts To join a game. Either GameID, color, or both can be specified.
                    Entering 'j' alone will prompt for the gameID and color preference
                
                -[color]
                    will attempt to join an existing game as the specified color
                    (w)hite, (b)lack, (e)ither, (o)bserver, or (a)ny
                    'e' will only attempt join a game as a player while 'a' will join as an
                    observer if the game is full.
                
                -[gameID] -[color]
                   If a gameID is specified the program will attempt to join that game as the
                   given color.
                
                -[gameID]
                   If only gameID is specified it will default to the 'a' option and join or
                   observe if the game is full.
            """;
    private static final String HELP_LIST = """
            LIST
                [L|l]
                    brings a list of all games on the server.
                        
                -j
                    Lists games that are available to join.
                        
                -o
                    Lists games that are available to observers
            """;
    private static final String HELP_QUIT = """
            QUIT
                [Q|q]
                    Will attempt to log the user out and return to the login screen.
            """;

    private static final String ALL_HELP = HELP_INTRO + HELP_HELP + HELP_SEARCH + HELP_LIST + HELP_JOIN + HELP_CREATE + HELP_QUIT;

    public void mainMenu() {
        mainMenuLayout();
        String[] input = scanner.nextLine().split("\\s");
        while(getInput(input)) {
            String command = input[0];
            if (command.isEmpty()) command = "*";
            switch (command.charAt(0)) {
                case '1', 'l', 'L':
                    list(input);
                    break;
                case '2', 's', 'S':
                    search(input);
                    break;
                case '3', 'j', 'J':
                    join(input);
                    break;
                case '4', 'c', 'C':
                    create(input);
                    break;
                case '6', 'q', 'Q', 'x', 'X', 'e', 'E':
                    quit();
                    break;
                case '5', 'h', 'H':
                default:
                    help(input);
            }
            mainMenuLayout();
        }
    }

    private boolean getInput(String[] input) {
        input = scanner.nextLine().split("\\s");
        return true;
    }

    private void mainMenuLayout() {
        Screen.clear();
        window.banner("HOME", 1);
        window.putBlock(3, 3, """
                1 (L)ist games
                2 (S)earch games
                3 (J)oin games
                4 (C)reate game
                5 (H)elp
                6 (Q)uit;
                """);
        screen.prompt("please select an option", null);
        Screen.refresh();
    }

    public void help(String[] input) {
        String requestedDoc;
        Screen.clear();
        Screen.prompt("press enter to exit");
        window.banner("HELP", 1, NEGATIVE);
        if (input.length < 2) {
            requestedDoc = ALL_HELP;
        }
        else {
            requestedDoc = switch (Character.toUpperCase(input[1].charAt(0))) {
                case 'J' -> HELP_JOIN;
                case 'C' -> HELP_CREATE;
                case 'L' -> HELP_LIST;
                case 'S' -> HELP_SEARCH;
                case 'Q', 'X', 'E' -> HELP_QUIT;
                default -> HELP_HELP;
            };
        }
        if (countLines(requestedDoc) > window.height - 4) {
//            chunkPages(requestedDoc);
            mainMenu();
        }
        window.putBlock(0, 2, requestedDoc);
        Screen.refresh();
        Screen.prompt("Press enter to return to menu");
        scanner.nextLine();
    }

    private void search(String[] input) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    private void list(String[] input) {
        Screen.clear();
        ArrayList<GameSummary> sums = new ArrayList<>(facade.listGames(authToken));
        if (input.length > 1) {
            char c = firstAlphabetical(input[1]);
            if (c == 'j') {
                sums.removeIf(g -> g.whiteUsername() != null && g.blackUsername() != null);
            } else if (c == 'o') {
                // this is identical for now
            }
        }
        window.banner("Games");
        int numEntries = window.height / 2;
        numEntries = Math.min(numEntries, sums.size());
        window.banner("Showing " + numEntries + "/" + sums.size() + "Games");
        if (sums.isEmpty()) {
            window.centerText((window.height + 4) / 2, "It doesn't seem like there's any games here.");
            window.centerText((window.height + 2) / 2, "Return to the main menu to start a new one by pressing enter.");
            Screen.refresh();
            scanner.nextLine();
            mainMenu();
        }
        for (int i = 0; i < numEntries; i++ ) {
            GameSummary sum = sums.get(i);
            String white = (sum.whiteUsername() == null)? "-" : sum.whiteUsername();
            String black = (sum.blackUsername() == null)? "-" : sum.blackUsername();
            String name = (sum.gameName() == null)? "-" : sum.gameName();
            int id = sum.gameID();
            window.putText(1, (2 * i), Integer.toString(i));
            window.putText(3, (2 * i), white);
            window.putText(23, (2 * i), black);
            window.putText(43, (2 * i), name);
            window.putText(63, (2 * i), Integer.toString(id));
        }
        Screen.refresh();
        scanner.nextLine();
    }
    private void join(String[] input) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    private void create(String[] input) {
        String gameName = null;
        ChessGame.TeamColor color = null;
        boolean tryBoth = false;
        boolean observer = false;
        if (input.length > 1) {
            gameName = input[1];
            if (input.length == 3) {
                char c = firstAlphabetical(input[1]);
                switch (Character.toUpperCase(c)) {
                    case 'B':
                        color = ChessGame.TeamColor.BLACK;
                        break;
                    case 'W':
                        color = ChessGame.TeamColor.BLACK;
                        break;
                    case 'E':
                        tryBoth = true;
                        break;
                    case 'O':
                        color = null;
                        observer = true;
                        break;
                    case 'A':
                        tryBoth = true;
                        observer = true;
                        break;
                }
            }
        }
        gameName = (gameName == null)? createGamePrompt() : gameName;
        if (gameName == null) {
            ErrorUi err = new ErrorUi();
            err.displayError(0, "Game name cannot be empty");
            return;
        }
        int gameID = facade.createGame(authToken, gameName);
        Screen.prompt(gameName, Integer.toString(gameID));
        Screen.refresh();
        scanner.nextLine();
    }

    public String createGamePrompt() {
        Screen.clear();
        window.banner("CREATE GAME");
        screen.prompt("Enter a name for this match", null);
        Screen.refresh();
        return scanner.nextLine();
    }
    private void quit() {
        facade.logout(authToken);
        authToken = null;
        System.exit(0);
    }

    public static char firstAlphabetical(String str) {
        for (char ch : str.toCharArray()) {
            if (Character.isLetter(ch)) {
                return ch;
            }
        }
        // If no alphabetical character found, return a default value, such as '\0' (null character)
        return '\0';
    }

    public static int countLines(String str) {
        int newLineCount = 1;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                newLineCount++;
            }
        }
        return newLineCount;
    }
}