package ui;

public class ErrorUi extends UI {
    public void loginError(String message) {
        banner("Error", 2, NEGATIVE);
        prompt(null, "press enter to continue");
        if (!message.isEmpty()) {
            putText(2, TOP_OF_WINDOW - 2, setColor(null, NEGATIVE) + message);
        }
        scanner.nextLine();
    }

    public void displayError(int code, String message) {
        banner("Error " + code, 2, NEGATIVE);
        prompt(null, "press enter to continue");
        if (!message.isEmpty()) {
            putText(2, TOP_OF_WINDOW - 2, setColor(null, NEGATIVE) + message);
        }
        scanner.nextLine();
    }
}
