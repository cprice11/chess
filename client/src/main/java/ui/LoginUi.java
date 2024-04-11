package ui;

import java.util.ArrayList;

import static ui.EscapeSequences.ERASE_SCREEN;

public class LoginUi extends UI {
    TerminalWindow window;

    public LoginUi() {
        window = new TerminalWindow(Screen.terminalWidth, Screen.terminalHeight);
        Screen.addWindow(window);
    }

    public enum returnValues {
        REGISTERED, LOGGED_IN, QUIT
    }

    public void start() {
        Screen.addWindow(window);
        setLoginScreen();
        String[] input = null;
        while (true) {
            input = scanner.nextLine().split("\\s");
            String choice = input[0];
            switch (choice) {
                case "1":
                case "r":
                case "R":
                    register();
                    break;
                case "2":
                case "l":
                case "L":
                    login();
                    break;
                case "e":
                case "E":
                case "q":
                case "Q":
                case "":
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    help();
                    Screen.clear();
            }
            setLoginScreen();
        }
    }

    private void login() {
        window.clear();
        window.banner("login", 1);
        Screen.prompt(null, "Username");
        Screen.refresh();
        String username = scanner.nextLine();
        window.putText(0, (window.height / 2), Screen.prompt() + username);
        String userPrompt = Screen.prompt();
        Screen.prompt("Password", null);
        Screen.refresh();
        Screen.clear();
        window.banner("login");
        String password = scanner.nextLine();
        window.putText(0, (window.height / 2), userPrompt + username);
        window.putText(0, (window.height / 2) + 1, Screen.prompt() + password.replaceAll("\\S", "*"));
        Screen.prompt(null, "loading");
        Screen.refresh();
        authToken = facade.login(username, password);
        if (authToken != null) {
            SelectionUi selection = new SelectionUi();
            Screen.removeWindow(window);
            selection.mainMenu();
        }
    }

    private void setLoginScreen() {
        window.clear();
        if (window.height > 25 && window.width > 100) window.banner("   ♟ Welcome to the 240 chess client ♟   ", 23);
        else window.banner("   ♟ Welcome to the 240 chess client ♟   ", 1);
        if (window.height > 20 && window.width > 68) {
            Screen.clear();
            window.clear();
            window.banner("HOME", 1);
            Screen.prompt(null, "Choose an option", UI.SECONDARY, UI.DARK_2);
            window.putBlock(3, (window.height - 6 )/ 2, MENU_OPTIONS);
            int knightXpos = window.width / 2;
            int knightYpos = window.height / 2;
            if (window.width < 30 || window.height < 10 ) {

            } else if (window.width <60 || window.height < 20) {
                window.putBlock(knightXpos - 5, knightYpos - 4, PAWN_SMALL);
            } else if (window.width < 110 || window.height < 40) {
                window.putBlock(knightXpos - 10, knightYpos - 7, PAWN_MEDIUM);
            } else {
                window.putBlock(knightXpos - 22, knightYpos - 14, PAWN_LARGE);
            }
            Screen.prompt("please select an option", null);
            Screen.refresh();
        }
        int menu = (window.height - 5) / 2;

        Screen.refresh();
    }

    private void help() {
        TerminalWindow popUp = new TerminalWindow(window.width / 8, window.height / 8, window.width * 3 / 4, window.height * 3 / 4);
        String helpText = """
                Hello and welcome to the CS 240 chess client.
                                
                This program lets you register, login, and play chess online.
                Registration requires a unique username, password and email.
                                
                press enter to return to the main screen.
                """;
        ArrayList<String> wrapped = wordWrap(helpText, popUp.width - 6);
        if (wrapped.size() > window.height) {
            // pager(wrapped);
            return;
        }
        Screen.clear();
        popUp.border(UI.SECONDARY);
        int i = 2;
        for (String row : wrapped) {
            popUp.putText(3, i, row);
            i++;
        }
        Screen.prompt("press enter");
        Screen.addWindow(popUp);
        Screen.refresh();
        Screen.removeWindow(popUp);
        scanner.nextLine();
        Screen.refresh();
    }

    private void register() {
        window.clear();
        Screen.clear();
        window.banner("register");
        Screen.prompt(null, "Username");
        Screen.refresh();
        window.clear();
        String username = scanner.nextLine();
        window.putText(0, (window.height / 2), Screen.prompt() + username);
        String userPrompt = Screen.prompt();
        Screen.prompt("Password", null);
        window.banner("register");
        Screen.refresh();
        Screen.clear();
        window.clear();
        window.banner("register");
        String password = scanner.nextLine();
        String passPrompt = Screen.prompt();
        window.putText(0, (window.height / 2) + 1, userPrompt + username);
        window.putText(0, (window.height / 2) + 2, Screen.prompt() + password.replaceAll("\\S", "*"));
        Screen.prompt(null, "Email");
        String emailPrompt = Screen.prompt();
        Screen.refresh();
        Screen.clear();
        window.clear();
        window.banner("register");
        String email = scanner.nextLine();
        window.putText(0, (window.height / 2), userPrompt + username);
        window.putText(0, (window.height / 2) + 1, passPrompt + password.replaceAll("\\S", "*"));
        window.putText(0, (window.height / 2) + 2, emailPrompt + email);
        Screen.prompt(null, "loading");
        Screen.refresh();
        authToken = facade.register(username, password, email);
        if (authToken != null) {
            SelectionUi selectionUi = new SelectionUi();
            Screen.removeWindow(window);
            selectionUi.mainMenu();
        }
    }
    private static final String MENU_OPTIONS = """
        Press enter to quit, or type
        'h' to see more information.
        
        1. Register
        2. Login
        """;


    private static final String PAWN_SMALL =
            """
              ⣠⣤⣤⡀
             ⢰⣿⣿⣿⣿⡄
              ⠻⠿⠿⠟
              ⠚⣿⣿⠓
               ⣿⣿
              ⢸⣿⣿⡆
            ⢀⣴⣶⣶⣶⣶⣶⣄
            ⢨⣭⣭⣭⣭⣭⣭⡅
            """;
    private static final String PAWN_MEDIUM =
            """
                  ⢀⣀⣀
                ⣠⣾⣿⣿⣿⣿⣷⣄
               ⢸⣿⣿⣿⣿⣿⣿⣿⣿⡆
               ⢹⣿⣿⣿⣿⣿⣿⣿⣿⡇
               ⠈⢿⣿⣿⣿⣿⣿⣿⡟
                 ⣉⣉⣉⣉⣉⣁
                ⠚⢛⣛⣛⣛⣛⠛⠂
                 ⢸⣿⣿⣿⣿
                 ⢸⣿⣿⣿⣿
                 ⢸⣿⣿⣿⣿⡆
                ⢀⣿⣿⣿⣿⣿⣷
                ⣾⣿⣿⣿⣿⣿⣿⣧
              ⢀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣧⡀
             ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
           ⢀⣴⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣄
           ⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇
            """;
    private static final String PAWN_LARGE =
            """                     
                                     ⣀⣠⣤⣤⣤⣤⣄⡀
                                  ⢀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣤
                                 ⣰⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡄
                                ⢰⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡀
                                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇
                                ⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇
                                ⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿
                                 ⠘⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠁
                                   ⠙⠻⠿⠿⠿⠿⠿⠿⠿⠿⠟⠁
                                   ⣠⣶⣶⣶⣶⣶⣶⣶⣶⣶⣦⡀
                                  ⠼⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠦
                                    ⢰⣶⣶⣶⣶⣶⣶⣶⣶
                                    ⢸⣿⣿⣿⣿⣿⣿⣿⣿
                                    ⢸⣿⣿⣿⣿⣿⣿⣿⣿
                                    ⢸⣿⣿⣿⣿⣿⣿⣿⣿
                                    ⢸⣿⣿⣿⣿⣿⣿⣿⣿
                                    ⣾⣿⣿⣿⣿⣿⣿⣿⣿⡇
                                   ⢠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷
                                   ⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇
                                  ⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆
                                 ⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆
                               ⢀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀
                             ⢀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄
                            ⣤⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄
                         ⢀⣠⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣄
                        ⠐⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠛⠂
                    ⢀⣴⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣄
                    ⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇
                    ⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇
                    """;
}
