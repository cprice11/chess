package ui;

import static ui.EscapeSequences.ERASE_SCREEN;

public class LoginUi extends UI {

    public LoginUi() {

    }

    public enum returnValues {
        REGISTERED, LOGGED_IN, QUIT
    }

    public void start() {
        eraseScreen();
        getDimeensions();
        if (terminalHeight > 25 && terminalWidth > 100) banner("   ♟ Welcome to the 240 chess client ♟   ", 23);
        else banner("   ♟ Welcome to the 240 chess client ♟   ", terminalHeight - 2);
        if (terminalHeight > 20 && terminalWidth > 68) {
            centerText(terminalHeight - 4, "         ⣠⠄     ");
            centerText(terminalHeight - 5, "     ⡠⣴⣆⣾⡟      ");
            centerText(terminalHeight - 6, "    ⢼⣷⣟⣟⣟⣶⣄     ");
            centerText(terminalHeight - 7, "   ⣼⣟⣟⣟⣟⣟⣟⣟⣷    ");
            centerText(terminalHeight - 8, "  ⢰⣾⣟⣟⣟⣟⣟⣟⣟⣟⡄   ");
            centerText(terminalHeight - 9, "  ⣚⣟⣟⣟⣟⣟⣟⣟⣟⣟⣟⣦  ");
            centerText(terminalHeight - 10, "  ⣟⣟⣟⣟⣟⣟⡜⠻⠿⢿⣟⣟⡇ ");
            centerText(terminalHeight - 11, "  ⣾⣟⣟⣟⣟⣟⣧   ⠻⠃  ");
            centerText(terminalHeight - 12, "  ⢻⣟⣟⣟⣟⣟⣟⣧⡀     ");
            centerText(terminalHeight - 13, "  ⢸⣟⣟⣟⣟⣟⣟⣟⣷⡄    ");
            centerText(terminalHeight - 14, "   ⣟⣟⣟⣟⣟⣟⣟⣟⣟⡀   ");
            centerText(terminalHeight - 15, "   ⠸⣟⣟⣟⣟⣟⣟⣟⣟⡇   ");
            centerText(terminalHeight - 16, "   ⣴⣟⣟⣟⣟⣟⣟⣟⣟⣷⡄  ");
            centerText(terminalHeight - 17, "   ⣠⣟⣟⣟⣟⣟⣟⣟⣟⣦⡀  ");
            centerText(terminalHeight - 18, " ⢠⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣄ ");
            centerText(terminalHeight - 19, " ⠸⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿ ");
        }
        int menu = (terminalHeight + 4) / 2;
        putText(0, menu, "Press enter to quit, or type");
        putText(0, menu- 1, "'h' to see more information.");
        putText(0, menu -3, "1. Register");
        putText(0, menu-4, "2. Login");
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

    private void login() {
        eraseScreen();
        banner("login");
        prompt(null, "Username", terminalHeight - 4);
        updateScreen();
        String username = scanner.nextLine();
        putText(0, terminalHeight - 4, prompt() + username);
        String userPrompt = prompt();
        prompt("Password", null, terminalHeight - 5);
        updateScreen();
        eraseScreen();
        banner("login");
        String password = scanner.nextLine();
        putText(0, terminalHeight - 4, userPrompt + username);
        putText(0, terminalHeight - 5, prompt() + password.replaceAll("\\S", "*"));
        prompt(null, "loading", terminalHeight - 7);
        updateScreen();
        authToken = facade.login(username, password);
        if (authToken != null) {
            SelectionUi selection = new SelectionUi();
            selection.mainMenu();
        } else {
            start();
        }

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
        start();
    }

    private void register() {
        eraseScreen();
        banner("register");
        prompt(null, "Username", 1);
        updateScreen();
        String username = scanner.nextLine();
        putText(0, terminalHeight - 4, prompt() + username);
        String userPrompt = prompt();
        prompt("Password", null, 1);
        updateScreen();
        eraseScreen();
        banner("register");
        String password = scanner.nextLine();
        String passPrompt = prompt();
        putText(0, terminalHeight - 4, userPrompt + username);
        putText(0, terminalHeight - 5, prompt() + password.replaceAll("\\S", "*"));
        prompt(null, "Email", 1);
        String emailPrompt = prompt();
        updateScreen();
        eraseScreen();
        banner("register");
        String email = scanner.nextLine();
        putText(0, terminalHeight - 4, userPrompt + username);
        putText(0, terminalHeight - 5, passPrompt + password.replaceAll("\\S", "*"));
        putText(0, terminalHeight - 6, emailPrompt + email);
        prompt(null, "loading", 1);
        updateScreen();
        authToken = facade.register(username, password, email);
        if (authToken != null) {
            SelectionUi selectionUi = new SelectionUi();
            selectionUi.mainMenu();
        } else {
            start();
        }
    }

}
