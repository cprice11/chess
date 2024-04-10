package ui;

import static ui.EscapeSequences.*;

public class ErrorUi extends UI {
    TerminalWindow popUp;

    public ErrorUi() {
        popUp = new TerminalWindow(3, 3, Screen.terminalWidth - 6, Screen.terminalHeight - 6);
        Screen.addWindow(popUp);
    }

    public void displayError(int code, String message) {
        String title = (code == -1)? "ERROR" : "ERROR" + code;
        int leftWidth = ((popUp.width ) / 2 ) -12;
        int rightWidth = ((popUp.width + 2)/ 2 ) -12;
        String left = UI.setBackground(NEGATIVE) +
                SET_TEXT_COLOR_BLACK +
                padLeft(BACK_ARROW, leftWidth, ' ') +
                RESET_BG_COLOR + RESET_TEXT_COLOR;
        String middle = setColor(null, NEGATIVE) +
                BACK_ARROW +
                setColor(NEGATIVE, LIGHT_PIECE) +
                "   " + code + "   " +
                RESET_BG_COLOR +
                setColor(null, NEGATIVE) +
                ARROW +
                RESET_TEXT_COLOR;
        String right = setBackground(NEGATIVE) +
                SET_TEXT_COLOR_BLACK +
                padRight(ARROW, rightWidth, ' ') +
                RESET_BG_COLOR + RESET_TEXT_COLOR;
        String paddedRight = padLeft(right, popUp.width - printLength(left), ' ');
        popUp.bar(0,  left + middle + right);

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
