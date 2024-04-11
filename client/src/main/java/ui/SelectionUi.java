package ui;

import chess.ChessGame;
import chess.GameState;
import model.GameSummary;

import java.util.ArrayList;
import java.util.Random;

public class SelectionUi extends UI {
    private boolean loop = true;
    private static boolean first = true;
    TerminalWindow window = new TerminalWindow(Screen.terminalWidth, Screen.terminalHeight - 1);

    public void mainMenu() {
        Screen.addWindow(window);
        mainMenuLayout();
        while(loop) {
            parseInput();
            mainMenuLayout();
        }
    }

    private void parseInput() {
        String[] input = scanner.nextLine().split("\\s");
        String command = input[0];
        if (command.isEmpty()) command = "*";
        switch (command.charAt(0)) {
            case '1':
            case 'l':
            case 'L':
                list(input);
                break;
            case '2':
            case 's':
            case 'S':
                search(input);
                break;
            case '3':
            case 'j':
            case 'J':
                join(input);
                break;
            case '4':
            case 'c':
            case 'C':
                create(input);
                break;
            case '6', 'q', 'Q', 'x', 'X', 'e', 'E':
                loop = false;
                quit();
                break;
            case '5', 'h', 'H':
            default:
                help(input);
        }
    }

    private void mainMenuLayout() {
        Screen.clear();
        window.clear();
        window.banner("HOME", 1);
        window.putBlock(3, (window.height - 6 )/ 2, MENU_OPTIONS);
            int knightXpos = window.width / 2;
            int knightYpos = window.height / 2;
        if (window.width < 30 || window.height < 10 ) {

        } else if (window.width <60 || window.height < 20) {
            window.putBlock(knightXpos - 5, knightYpos - 4, KNIGHT_SMALL);
        } else if (window.width < 110 || window.height < 40) {
            window.putBlock(knightXpos - 10, knightYpos - 6, KNIGHT_MEDIUM);
        } else {
            window.putBlock(knightXpos - 22, knightYpos - 17, KNIGHT_LARGE);
        }
        Screen.prompt("please select an option", null);
        Screen.refresh();
    }

    public void help(String[] input) {
        String requestedDoc;
        Screen.clear();
        Screen.prompt("press enter to exit");
        TerminalWindow popUp = new TerminalWindow(window.width, window.height);
        Screen.addWindow(popUp);
        popUp.border(UI.SECONDARY);
        popUp.banner("MAN PAGE", 1, UI.SECONDARY);
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
        if (countLines(requestedDoc) > popUp.height - 4) {
            new Pager(popUp, 3, 22, "MAN PAGE").chunkPages(requestedDoc);
            Screen.removeWindow(popUp);
            mainMenu();
        }
        popUp.putBlock(0, 2, requestedDoc);
        Screen.refresh();
        Screen.prompt("Press enter to return to menu");
        Screen.removeWindow(popUp);
        scanner.nextLine();
    }

    private void search(String[] input) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    private void list(String[] input) {
        window.clear();
        ArrayList<GameSummary> sums = new ArrayList<>(facade.listGames(authToken));
        if (input.length > 1) {
            char c = firstAlphabetical(input[1]);
            if (c == 'j') {
                sums.removeIf(g -> g.whiteUsername() != null && g.blackUsername() != null);
            } else if (c == 'o') {
                // this is identical for now
            }
        }
        int numEntries = window.height / 2;
        numEntries = Math.min(numEntries, sums.size());
        window.banner("Showing " + numEntries + "/" + sums.size() + " Games");
        if (sums.isEmpty()) {
            window.centerText((window.height + 4) / 2, "It doesn't seem like there's any games here.");
            window.centerText((window.height + 2) / 2, "Return to the main menu to start a new one by pressing enter.");
            Screen.refresh();
            parseInput();
        }
        int numX = Math.max(2, (window.width - 120) / 2);
        int entryWidth = (window.width - numX - 2) / 4;
        int gameX = numX + 4;
        int whiteX = gameX + entryWidth;
        int blackX = whiteX + entryWidth;
        int idX = blackX + entryWidth;
        window.putText(gameX, 4, "game name" );
        window.putText(whiteX, 4, "white" );
        window.putText(blackX, 4, "black" );
        window.putText(idX, 4, "ID" );
        for (int i = 0; i < numEntries; i++ ) {
            GameSummary sum = sums.get(i);
            addGameRow(i + 6, i, sum);
        }
        Screen.refresh();
        parseInput();
    }

    private void addGameRow(int y, int selectionId, GameSummary summary) {
        int numX = Math.max(2, (window.width - 120) / 2);
        int entryWidth = (window.width - numX - 2) / 4;
        int gameX = numX + 4;
        int whiteX = gameX + entryWidth;
        int blackX = whiteX + entryWidth;
        int idX = blackX + entryWidth;
        String name = (summary.gameName() == null)? "-" : summary.gameName();
        String white = (summary.whiteUsername() == null)? "-" : summary.whiteUsername();
        String black = (summary.blackUsername() == null)? "-" : summary.blackUsername();
        String id = Integer.toString(summary.gameID());
        if (name.length() > entryWidth) name = name.substring(0, entryWidth - 3) + "...";
        if (white.length() > entryWidth) name = name.substring(0, entryWidth - 3) + "...";
        if (black.length() > entryWidth) name = name.substring(0, entryWidth - 3) + "...";
        if (name.length() > entryWidth) name = name.substring(0, entryWidth - 3) + "...";
        window.putText(numX, y, padLeft(Integer.toString(selectionId), 2, ' '));
        window.putText(gameX, y, padRight(name, entryWidth - 2, ' '));
        window.putText(whiteX, y, padRight(white, entryWidth - 2, ' '));
        window.putText(blackX, y, padRight(black, entryWidth - 2, ' '));
        window.putText(idX, y, padRight(id, entryWidth - 2, ' '));
    }
    private void join(String[] input) {
        char first = '\0';
        char second = '\0';
        if (input.length >= 2) {
            first = input[1].charAt(0);
        }
        if (input.length > 2) {
            second = input[2].charAt(0);
        }
        char colorPref =  (first != '\0' && "WBEOA".indexOf(first) != -1)? first : 'X';
        colorPref =  (second != '\0' && "WBEOA".indexOf(second) != -1)? second : colorPref;
        int gameId = (Character.isDigit(first) && Integer.parseInt(input[1]) != -1)? Integer.parseInt(input[1]) : -1;
        gameId = (Character.isDigit(second) && Integer.parseInt(input[2]) != -1)? Integer.parseInt(input[2]) : gameId;

        if (gameId == -1) {
            Screen.prompt("Please enter the ID of the game you would like to join");
            Screen.refresh();
            gameId = scanner.nextInt();
        }

        if (colorPref == '\0') {
            Screen.prompt("Would you like to play as black, or white?");
            Screen.refresh();
            colorPref = scanner.nextLine().charAt(0);
        }
        if ("WBEOA".indexOf(Character.toUpperCase(colorPref)) == -1 ){
            new ErrorUi().displayError("Unable to parse input");
            return;
        }

        ChessGame.TeamColor color = null;
        boolean observe = false;
        switch (Character.toUpperCase(colorPref)) {
            case 'W':
                color = ChessGame.TeamColor.WHITE;
                break;
            case 'B':
                color = ChessGame.TeamColor.BLACK;
                break;
            case 'E':
                color = getColor(false, gameId);
                break;
            case 'A':
                color = getColor(true, gameId);
                observe = true;
                break;
        };
        if (color == null && !observe) new ErrorUi().displayError("Failed to join as color");
        if (facade.joinGame(authToken, gameId, color)) new GamePlayUI(gameId);
        return;
    }

//    private ChessGame.TeamColor getGameId(boolean observe, int gameID) {
//        if (new Random(gameID).nextBoolean()) {
//            if (game.whiteUsername() == null) return ChessGame.TeamColor.WHITE;
//            else if (game.blackUsername() == null) return ChessGame.TeamColor.BLACK;
//        } else {
//            if (game.blackUsername() == null) return ChessGame.TeamColor.BLACK;
//            else if (game.whiteUsername() == null) return ChessGame.TeamColor.WHITE;
//        }
//        if (color == null && observe) return color;
//        new ErrorUi().displayError("Unable to join game. Please try a different match.");
//        return null;
//    }

    private ChessGame.TeamColor getColor(boolean observe, int gameID) {
        ChessGame.TeamColor color = null;
        GameSummary game = new GameSummary(0, null, null, null); // facade.getgameSummary();
        if (new Random(gameID).nextBoolean()) {
            if (game.whiteUsername() == null) return ChessGame.TeamColor.WHITE;
            else if (game.blackUsername() == null) return ChessGame.TeamColor.BLACK;
        } else {
            if (game.blackUsername() == null) return ChessGame.TeamColor.BLACK;
            else if (game.whiteUsername() == null) return ChessGame.TeamColor.WHITE;
        }
        if (color == null && observe) return color;
        new ErrorUi().displayError("Unable to join game. Please try a different match.");
        return null;
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
        if (gameName == null || gameName.isEmpty()) {
            ErrorUi err = new ErrorUi();
            err.displayError("Game name cannot be empty");
            return;
        }
        int gameID = facade.createGame(authToken, gameName);
        Screen.prompt(gameName, Integer.toString(gameID));
        Screen.refresh();
        parseInput();
    }

    public String createGamePrompt() {
        Screen.clear();
        window.banner("CREATE GAME");
        Screen.prompt("Enter a name for this match", null);
        Screen.refresh();
        return scanner.nextLine();
    }

    private void quit() {
        facade.logout(authToken);
        authToken = null;
        System.exit(0);
    }

    public static char firstAlphabetical(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                return c;
            }
        }
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


    private static final String KNIGHT_SMALL = """
                ⡀⣠⠄
              ⣴⣿⣿⣿⣦⡀
             ⣰⣿⣿⣿⣿⣿⣷⣄
             ⣽⣿⣿⣿⣌⠉⠙⠋
             ⠸⣿⣿⣿⣿⣷⡀
              ⣻⣿⣿⣿⣿⣷⡀
              ⠹⠿⠿⠿⠿⠯
             ⠛⠛⠛⠛⠛⠛⠛⠛⠂""";

    private static final String KNIGHT_MEDIUM = """
                  ⣀⣤⣄⣴⡾⠏
                ⣴⡷⣟⣯⡿⣷⣯⣄⡀
              ⣠⣿⢾⣿⣻⣟⣿⣻⣾⣟⣿⢷
             ⢠⣶⣟⣿⣽⣯⣿⣽⢿⡾⣯⣿⣻⣧⡀
             ⣬⣷⣿⣻⡾⣷⢿⣾⢿⣻⣯⣿⣽⣾⢿⣶⡀
             ⢿⢷⣿⣽⣟⣿⣻⣽⡆⠉⠓⠛⠚⢟⣿⠹⠇
             ⢿⣟⣷⢿⣽⣯⣿⣽⢿⣦    ⠉⠁
             ⢸⣿⣽⢿⡷⣿⢾⣻⣿⣽⢷⣄
              ⢷⣿⣻⣟⣿⣻⡿⣾⣯⣿⣻⣷⡀
              ⠈⢯⣿⣽⣯⣿⣻⣽⣾⣯⡿⣾⢷
              ⢠⣬⣿⢾⡷⣟⣯⣿⡾⣷⡿⣟⣿⣤
              ⠘⠛⣚⣛⣛⣛⣛⣓⣛⣛⣙⣛⡋⠛
              ⣀⣴⣟⣯⣿⣯⡿⣯⣿⣯⣿⣻⣷⣤⡀
            ⢠⣶⡶⣶⡶⣶⡶⣶⣶⡶⣶⡶⣶⡶⣶⡶⣶⣶
            ⠘⠓⠛⠛⠛⠋⠛⠛⠚⠛⠋⠛⠋⠛⠋⠛⠛⠚⠁""";
    private static final String KNIGHT_LARGE =
            """          
                                    ⢀⣀⣤⣤⠖
                             ⢀⣀⡀ ⢀⣤⣾⣿⣿⣿⠋
                         ⣀⣤⣶⣿⣿⣿⠇⣴⣿⣿⣿⣿⡿⠁
                      ⢀⣴⣦⡸⣿⣿⣿⣿⣿⣾⣿⣿⣿⣿⠿⠁
                    ⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣤⣀
                  ⢀⣀⣙⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⣀⡀
                 ⢠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇
                ⠠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
               ⢠⣤⣬⣽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄
               ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣀
              ⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣤⡀
              ⣀⣭⣽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀
             ⢰⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠘⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷
             ⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡆ ⠉⠛⠻⠿⠿⠿⠿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⡿
             ⠈⣉⣭⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄         ⠙⢿⣿⣿⣿⡄⠉⠉⠁
             ⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣄          ⠙⠿⠟⠋
              ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀
              ⢹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀
              ⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄
               ⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄
               ⠈⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⡀
                ⠸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡄
                 ⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄
                  ⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷
                   ⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡆
                 ⢀⣤⣤⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣀
                 ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇
                 ⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃
                    ⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀
                    ⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡀
                 ⢀⣠⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣄
                ⠐⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠂
            ⢀⣴⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣄
            ⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇
            ⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇
            """;


    private static final String MENU_OPTIONS = """
                1 (L)ist games
                2 (S)earch games
                3 (J)oin games
                4 (C)reate game
                5 (H)elp
                6 (Q)uit;
                """;

    private static final String HELP_INTRO =
            """
            MENU MANUAL
                From this menu you can lookup starting or ongoing games as well as create a game or log out. All commands are case insensitive but should be separated by whitespace.
            """;
    private static final String HELP_HELP = """
            HELP
                [H|h]
                
                To see this help menu enter 'h' on the navigation screen.
                    
                -[COMMAND]
                
                To see explanations of an individual command try entering 'h [COMMAND]' to see more details (e.g. 'h join').
            """;
    private static final String HELP_SEARCH = """
            SEARCH
                [S|s]
                
                Brings up the search bar and shows a list of random games
                
                -u [user]
                  
                Brings up games where the given user is participating
                
                -g [gameName]
                
                Brings up games with the given name. (e.g. 's -g "Friday morning match" ')
            """;
    private static final String HELP_CREATE = """
            CREATE
                [C|c]
                
                Will create a new game on the server. Options exist for specifying parameters.
                    
                -
                
                Entering 'c' alone will prompt for a name to give the game.
        
                - [gameName] [color]
                
                will attempt to create a game and join as the specified color (w)hite, (b)lack, (e)ither, (o)bserver, or (a)lso to join as both white and black.
        
                - [gameName]
                
                If no color is specified the program will only create a game and will not join it.
            """;
    private static final String HELP_JOIN = """
            JOIN
                [J|j]
                
                Attempts To join a game. Either GameID, color, or both can be specified. Entering 'j' alone will prompt for the gameID and color preference
                
                -[color]
                
                will attempt to join an existing game as the specified color (w)hite, (b)lack, (e)ither, (o)bserver, or (a)ny 'e' will only attempt join a game as a player while 'a' will join as an observer if the game is full.
                
                -[gameID] -[color]
                
                If a gameID is specified the program will attempt to join that game as the given color.
                
                -[gameID]
                
                If only gameID is specified it will default to the 'a' option and join or observe if the game is full.
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

    private static final String ALL_HELP = HELP_INTRO + "\n\n" + HELP_HELP +"\n\n" +  HELP_SEARCH +"\n\n" +  HELP_LIST +"\n\n" +  HELP_JOIN +"\n\n" +  HELP_CREATE +"\n\n" +  HELP_QUIT;
}