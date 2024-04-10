package ui;

public class ErrorUi extends UI {
    TerminalWindow popUp;

    public ErrorUi() {
        popUp = new TerminalWindow(3, 3, Screen.terminalWidth - 6, Screen.terminalHeight - 6);
        Screen.addWindow(popUp);
    }

    public void displayError(int code, String message) {
        if (code == -1) {
            popUp.banner("Error", 1,  NEGATIVE);
        } else {
            popUp.banner("Error " + code, 2, NEGATIVE);
        }
        popUp.putText(1, (popUp.height - 1) / 2, message);
        Screen.prompt(null, "press enter to continue");
        if (!message.isEmpty()) {
            popUp.putText(2, 2, setColor(null, NEGATIVE) + message);
        }
        Screen.refresh();
        scanner.nextLine();
        Screen.removeWindow(popUp);
    }
}
