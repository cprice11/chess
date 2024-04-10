package ui;

import model.GameSummary;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionUi extends UI {
    private static boolean first = true;

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
                    Lists games that are available to observer
            """;
    private static final String HELP_QUIT = """
            QUIT
                [Q|q]
                    Will attempt to log the user out and return to the login screen.
            """;

    private static final String ALL_HELP = HELP_INTRO + HELP_HELP + HELP_SEARCH + HELP_LIST + HELP_JOIN + HELP_CREATE + HELP_QUIT;

    public void mainMenu() {
        eraseScreen();
        banner("HOME", terminalHeight - 2);
        putBlock(3, TOP_OF_WINDOW - 3, """
                1 (L)ist games
                2 (S)earch games
                3 (J)oin games
                4 (C)reate game
                5 (H)elp
                6 (Q)uit;
                """);
        updateScreen();
        var a = screen;
        prompt(null, "please select an option", 1);
        updateScreen();
        updateScreen();
        String[] input = scanner.nextLine().split("\\s");
        String command = input[0];
        if (command.isEmpty()) command = "*";
        switch (command.charAt(0)) {
            case '1', 'l', 'L':
                list();
                break;
            case '2', 's', 'S':
                search();
                break;
            case '3', 'j', 'J':
                join();
                break;
            case '4', 'c', 'C':
                create();
                break;
            case '6','q', 'Q', 'x', 'X', 'e', 'E':
                quit();
                break;
            case '5', 'h', 'H':
            default:
                help(input);
        }

    }

    public void help(String[] input) {
        String requestedDoc;
        eraseScreen();
        prompt(null, "press enter to exit");
        banner("HELP", terminalHeight - 2, NEGATIVE);
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
        if (countLines(requestedDoc) > terminalHeight - 4) {
            chunkPages(requestedDoc);
            mainMenu();
        }
        putBlock(0, TOP_OF_WINDOW, requestedDoc);
        updateScreen();
        scanner.nextLine();
        mainMenu();
    }

    private void chunkPages(String doc) {
        int top = 0;
        int bottom = WINDOW_HEIGHT;
        int jumpDist = 3;
        boolean atBottom = false;
        String[] rows = doc.split("\\n", 300);
        String chunk = String.join("\n", Arrays.copyOfRange(rows, top, bottom));
        prompt(null, "press enter to scroll [q|x] to exit", 0);
        putBlock(0, TOP_OF_WINDOW, chunk);
        updateScreen();
        String input = scanner.nextLine();
        while (input == null || input.isEmpty() || "xXqQ".indexOf(input.charAt(0)) == -1 ) {
            if (atBottom) {
                top = 0;
                bottom = WINDOW_HEIGHT;
                chunk = String.join("\n", Arrays.copyOfRange(rows, top, bottom));
                atBottom = false;
            } else if (bottom + jumpDist > rows.length) {
                chunk = String.join("\n", Arrays.copyOfRange(rows, rows.length - WINDOW_HEIGHT, rows.length));
                atBottom = true;
            } else {
                top += jumpDist;
                bottom += jumpDist;
                chunk = String.join("\n", Arrays.copyOfRange(rows, top, bottom));
            }
            eraseScreen();
            banner("HELP", TITLE, NEGATIVE);
            prompt(null, "press enter to scroll [q|x] to exit", 0);
            putBlock(0, TOP_OF_WINDOW, chunk);
            updateScreen();
            input = scanner.nextLine();
        }
    }

    private void search() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    private void list() {
        ArrayList<GameSummary> sums = new ArrayList<>(facade.listGames(authToken));
        int numEntries = WINDOW_HEIGHT / 2;
        banner("Showing " + numEntries + "/" + sums.size() + "Games");
        for (int i = 0; i < WINDOW_HEIGHT / 2; i++ ) {
            GameSummary sum = sums.get(i);
            String white = (sum.whiteUsername() == null)? "-" : sum.whiteUsername();
            String black = (sum.blackUsername() == null)? "-" : sum.blackUsername();
            String name = (sum.gameName() == null)? "-" : sum.gameName();
            int id = sum.gameID();
            putText(0, TOP_OF_WINDOW - (2 * i), Integer.toString(i)+ white + black + name + id);
        }
    }
    private void join() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    private void create() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }
    private void quit() {
        facade.logout(authToken);
        authToken = null;
        System.exit(0);
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