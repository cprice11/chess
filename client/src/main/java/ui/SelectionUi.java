package ui;

import java.util.Arrays;

public class SelectionUi extends UI {
    private static boolean first = true;

    private static final String HELP_INTRO =
            """
            MENU MANUAL
                From this menu you can lookup starting or ongoing games as well as create a
                game or log out. All commands are case insensitive but should be separated by
                whitespace.
            """;
    private static final String HELP_HELP = """
            HELP"
                [H|h]
                    To see this help menu enter 'h' on the navigation screen.
                    
                -[COMMAND]"
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

    private static final String ALL_HELP = HELP_INTRO + HELP_HELP + HELP_SEARCH + HELP_JOIN + HELP_CREATE + HELP_QUIT;

    public void mainMenu() {
        banner("HOME");
        putBlock(0, TERMINAL_HEIGHT - 4, """
                - (L)ist games
                - (J)oin games
                - (C)reate game
                - (H)elp
                - (Q)uit;
                """);
        prompt(null, "please select an option");
        updateScreen();
        String[] input = scanner.nextLine().split("\\s");
        switch (input[0].charAt(0)) {
            case 's', 'S':
                search();
                break;
            case 'l', 'L':
                list();
                break;
            case 'j', 'J':
                join();
                break;
            case 'c', 'C':
                create();
                break;
            case 'q', 'Q', 'x', 'X', 'e', 'E':
                quit();
            case 'h', 'H':
            default:
                help(input);
        }

    }

    public void help(String[] input) {
        String requestedDoc;
        eraseScreen();
        banner("HELP");
        if (input.length < 2) {
            requestedDoc = ALL_HELP;
        }
        else {
            switch (Character.toUpperCase(input[0].charAt(0))) {
                case 'H', 'M':
                    requestedDoc = switch (Character.toUpperCase(input[1].charAt(0))){
                        case 'J' -> HELP_JOIN;
                        case 'C' -> HELP_CREATE;
                        case 'L' -> HELP_LIST;
                        case 'S' -> HELP_SEARCH;
                        case 'Q', 'X', 'E' -> HELP_QUIT;
                        default -> HELP_HELP;
                    };
                default:
                    requestedDoc = HELP_HELP;
                }
        }
        if (countLines(requestedDoc) > TERMINAL_HEIGHT - 4) {
            chunkPages(requestedDoc);
            return;
        }
        putBlock(1, 4, requestedDoc);
        updateScreen();
    }

    private void chunkPages(String doc) {
        int window = TERMINAL_HEIGHT - 4;
        int top = 0;
        int jumpDist = 3;
        String[] rows = doc.split("\\n");
        String chunk = String.join("\n", Arrays.copyOfRange(rows, top, window));
        putBlock(0, TERMINAL_HEIGHT - 4, chunk);
        prompt(null, "press enter to scroll [q|x] to exit", 0);
        while ("xXqQ".indexOf(scanner.nextLine().charAt(0)) != -1 ) {
            if (window + jumpDist > top += 6;
        }

    }

    private void search() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    private void list() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }
    private void join() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    private void create() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }
    private void quit() {
        throw new RuntimeException("NOT IMPLEMENTED");
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