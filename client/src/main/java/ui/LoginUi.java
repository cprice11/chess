package ui;

import static ui.EscapeSequences.*;
import serverFacade.ServerFacade;

public class LoginUi extends UI {

    public LoginUi() {

    }
    public enum returnValues {
        REGISTERED, LOGGED_IN, QUIT
    }

    @Override
    public void start() {
        boolean loggedIn = false;
        String authToken = null;
        while (!loggedIn) {
            if (TERMINAL_HEIGHT > 25 && TERMINAL_WIDTH > 100) banner("   ♟ Welcome to the 240 chess client ♟   ", 23);
            else banner("   ♟ Welcome to the 240 chess client ♟   ");
            if (TERMINAL_HEIGHT > 20 && TERMINAL_WIDTH > 68) {
                centerText(TERMINAL_HEIGHT - 4, "         ⣠⠄     ");
                centerText(TERMINAL_HEIGHT - 5, "     ⡠⣴⣆⣾⡟      ");
                centerText(TERMINAL_HEIGHT - 6, "    ⢼⣷⣟⣟⣟⣶⣄     ");
                centerText(TERMINAL_HEIGHT - 7, "   ⣼⣟⣟⣟⣟⣟⣟⣟⣷    ");
                centerText(TERMINAL_HEIGHT - 8, "  ⢰⣾⣟⣟⣟⣟⣟⣟⣟⣟⡄   ");
                centerText(TERMINAL_HEIGHT - 9, "  ⣚⣟⣟⣟⣟⣟⣟⣟⣟⣟⣟⣦  ");
                centerText(TERMINAL_HEIGHT - 10, "  ⣟⣟⣟⣟⣟⣟⡜⠻⠿⢿⣟⣟⡇ ");
                centerText(TERMINAL_HEIGHT - 11, "  ⣾⣟⣟⣟⣟⣟⣧   ⠻⠃  ");
                centerText(TERMINAL_HEIGHT - 12, "  ⢻⣟⣟⣟⣟⣟⣟⣧⡀     ");
                centerText(TERMINAL_HEIGHT - 13, "  ⢸⣟⣟⣟⣟⣟⣟⣟⣷⡄    ");
                centerText(TERMINAL_HEIGHT - 14, "   ⣟⣟⣟⣟⣟⣟⣟⣟⣟⡀   ");
                centerText(TERMINAL_HEIGHT - 15, "   ⠸⣟⣟⣟⣟⣟⣟⣟⣟⡇   ");
                centerText(TERMINAL_HEIGHT - 16, "   ⣴⣟⣟⣟⣟⣟⣟⣟⣟⣷⡄  ");
                centerText(TERMINAL_HEIGHT - 17, "   ⣠⣟⣟⣟⣟⣟⣟⣟⣟⣦⡀  ");
                centerText(TERMINAL_HEIGHT - 18, " ⢠⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣄ ");
                centerText(TERMINAL_HEIGHT - 19, " ⠸⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿ ");
            }
            putText(0, 9, "Press enter to quit, or type");
            putText(0, 8, "'h' to see more information.");
            putText(0, 5, "1. Register");
            putText(0, 4, "2. Login");
            prompt(null, "Choose an option", 1);
            updateScreen();
            String choice = scanner.nextLine();
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
                    eraseScreen();
            }
        }

    }

    private void login() {
        eraseScreen();
        banner("login");
        prompt(null, "Username",  TERMINAL_HEIGHT - 4);
        updateScreen();
        String username = scanner.nextLine();
        putText(0, TERMINAL_HEIGHT - 4,  prompt() + username);
        String userPrompt = prompt();
        prompt( "Password", null, TERMINAL_HEIGHT - 5);
        updateScreen();
        eraseScreen();
        banner("login");
        String password = scanner.nextLine();
        putText(0, TERMINAL_HEIGHT - 4,  userPrompt + username);
        putText(0, TERMINAL_HEIGHT - 5,  prompt() + password.replaceAll("\\S","*"));
        prompt(null, "loading", TERMINAL_HEIGHT - 7);
        updateScreen();
        //ServerFacade.login(username, password);
        System.out.println(username);
        System.out.println(password);
    }

    private void help() {
        eraseScreen();

        String helpText = """
                Hello and welcome to the CS 240 chess client.
                                
                This program lets you register, login, and play chess online.
                Registration requires a unique username, password and email.
                                
                press enter to return to the main screen.
                """;
        putBlock(3, 17, helpText);

        updateScreen();
        prompt("BOLD MESSAGE", "small message");
        scanner.nextLine();
        System.out.println(ERASE_SCREEN);
    }

    private void register() {
        eraseScreen();
        banner("register");
        prompt(null, "Username",  TERMINAL_HEIGHT - 4);
        updateScreen();
        String username = scanner.nextLine();
        putText(0, TERMINAL_HEIGHT - 4,  prompt() + username);
        String userPrompt = prompt();
        prompt( "Password", null, TERMINAL_HEIGHT - 5);
        updateScreen();
        eraseScreen();
        banner("register");
        String password = scanner.nextLine();
        String passPrompt = prompt();
        putText(0, TERMINAL_HEIGHT - 4,  userPrompt + username);
        putText(0, TERMINAL_HEIGHT - 5,  prompt() + password.replaceAll("\\S","*"));
        prompt(null, "Email", TERMINAL_HEIGHT - 6);
        updateScreen();
        eraseScreen();
        banner("register");
        String email = scanner.nextLine();
        putText(0, TERMINAL_HEIGHT - 4,  userPrompt + username);
        putText(0, TERMINAL_HEIGHT - 5,  passPrompt + password.replaceAll("\\S","*"));
        prompt(null, "loading", TERMINAL_HEIGHT - 6);
        updateScreen();
        //ServerFacade.register(username, password, email);
        System.out.println(username);
        System.out.println(password);
        System.out.println(email);
    }

}
