package ui;

import static ui.EscapeSequences.ERASE_SCREEN;

public class LoginUi extends UI {
    TerminalWindow window = new TerminalWindow(Screen.terminalWidth, Screen.terminalHeight);

    public LoginUi() {

    }

    public enum returnValues {
        REGISTERED, LOGGED_IN, QUIT
    }

    public void start() {
        Screen.clear();
        if (window.height > 25 && window.width > 100) window.banner("   ♟ Welcome to the 240 chess client ♟   ", 23);
        else window.banner("   ♟ Welcome to the 240 chess client ♟   ", window.height - 2);
        if (window.height > 20 && window.width> 68) {
            window.centerText(4, "         ⣠⠄     ");
            window.centerText(5, "     ⡠⣴⣆⣾⡟      ");
            window.centerText(6, "    ⢼⣷⣟⣟⣟⣶⣄     ");
            window.centerText(7, "   ⣼⣟⣟⣟⣟⣟⣟⣟⣷    ");
            window.centerText(8, "  ⢰⣾⣟⣟⣟⣟⣟⣟⣟⣟⡄   ");
            window.centerText(9, "  ⣚⣟⣟⣟⣟⣟⣟⣟⣟⣟⣟⣦  ");
            window.centerText(10, "  ⣟⣟⣟⣟⣟⣟⡜⠻⠿⢿⣟⣟⡇ ");
            window.centerText(11, "  ⣾⣟⣟⣟⣟⣟⣧   ⠻⠃  ");
            window.centerText(12, "  ⢻⣟⣟⣟⣟⣟⣟⣧⡀     ");
            window.centerText(13, "  ⢸⣟⣟⣟⣟⣟⣟⣟⣷⡄    ");
            window.centerText(14, "   ⣟⣟⣟⣟⣟⣟⣟⣟⣟⡀   ");
            window.centerText(15, "   ⠸⣟⣟⣟⣟⣟⣟⣟⣟⡇   ");
            window.centerText(16, "   ⣴⣟⣟⣟⣟⣟⣟⣟⣟⣷⡄  ");
            window.centerText(17, "   ⣠⣟⣟⣟⣟⣟⣟⣟⣟⣦⡀  ");
            window.centerText(18, " ⢠⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣄ ");
            window.centerText(19, " ⠸⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿ ");
        }
        int menu = (window.height + 4) / 2;
        window.putText(0, menu, "Press enter to quit, or type");
        window.putText(0, menu- 1, "'h' to see more information.");
        window.putText(0, menu -3, "1. Register");
        window.putText(0, menu-4, "2. Login");
        Screen.prompt(null, "Choose an option");
        Screen.refresh();
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
                Screen.clear();
        }
    }

    private void login() {
        Screen.clear();
        window.banner("login");
        Screen.prompt(null, "Username");
        Screen.refresh();
        String username = scanner.nextLine();
        window.putText(0,  4, Screen.prompt() + username);
        String userPrompt = Screen.prompt();
        Screen.prompt("Password", null);
        Screen.refresh();
        Screen.clear();
        window.banner("login");
        String password = scanner.nextLine();
        window.putText(0, 4, userPrompt + username);
        window.putText(0, 5, Screen.prompt() + password.replaceAll("\\S", "*"));
        Screen.prompt(null, "loading");
        Screen.refresh();
        authToken = facade.login(username, password);
        if (authToken != null) {
            SelectionUi selection = new SelectionUi();
            selection.mainMenu();
        } else {
            start();
        }

    }

    private void help() {
        Screen.clear();

        String helpText = """
                Hello and welcome to the CS 240 chess client.
                                
                This program lets you register, login, and play chess online.
                Registration requires a unique username, password and email.
                                
                press enter to return to the main screen.
                """;
        window.putBlock(3, 17, helpText);

        Screen.refresh();
        Screen.prompt("BOLD MESSAGE", "small message");
        scanner.nextLine();
        System.out.println(ERASE_SCREEN);
        start();
    }

    private void register() {
        Screen.clear();
        window.banner("register");
        Screen.prompt(null, "Username");
        Screen.refresh();
        String username = scanner.nextLine();
        window.putText(0, 4, Screen.prompt() + username);
        String userPrompt = Screen.prompt();
        Screen.prompt("Password", null);
        Screen.refresh();
        Screen.clear();
        window.banner("register");
        String password = scanner.nextLine();
        String passPrompt = Screen.prompt();
        window.putText(0, 4, userPrompt + username);
        window.putText(0, 5, Screen.prompt() + password.replaceAll("\\S", "*"));
        Screen.prompt(null, "Email");
        String emailPrompt = Screen.prompt();
        Screen.refresh();
        Screen.clear();
        window.banner("register");
        String email = scanner.nextLine();
        window.putText(0, 4, userPrompt + username);
        window.putText(0, 5, passPrompt + password.replaceAll("\\S", "*"));
        window.putText(0, 6, emailPrompt + email);
        Screen.prompt(null, "loading");
        Screen.refresh();
        authToken = facade.register(username, password, email);
        if (authToken != null) {
            SelectionUi selectionUi = new SelectionUi();
            selectionUi.mainMenu();
        } else {
            start();
        }
    }

}
