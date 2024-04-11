package ui;

import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class ErrorUi extends UI {
    TerminalWindow popUp;

    public ErrorUi() {
        popUp = new TerminalWindow(Screen.terminalWidth / 4, Screen.terminalHeight / 4, Screen.terminalWidth / 2, Screen.terminalHeight / 2);
        Screen.addWindow(popUp);
    }

    public void displayError(String message) {
        displayError(-1, message);
    }
    public void displayError(int code, String message) {
        popUp.border(UI.color);
        String title = (code == -1)? "ERROR" : "ERROR " + code;
        int titleWidth = 8 + title.length();
        int leftWidth = (popUp.width - titleWidth) / 2;
        int rightWidth = (popUp.width - titleWidth + 1)/ 2;
        String left = UI.setBackground(color) +
                SET_TEXT_COLOR_BLACK +
                padLeft(BACK_ARROW, leftWidth, ' ') +
                RESET_BG_COLOR + RESET_TEXT_COLOR;
        String middle = setColor(null, color) +
                BACK_ARROW +
                setColor(color, LIGHT_PIECE) +
                "   " + title + "   " +
                RESET_BG_COLOR +
                setColor(null, color) +
                ARROW +
                RESET_TEXT_COLOR;
        String right = setBackground(color) +
                SET_TEXT_COLOR_BLACK +
                padRight(ARROW, rightWidth, ' ') +
                RESET_BG_COLOR + RESET_TEXT_COLOR;
        popUp.bar(0,  left + middle + right);
        Screen.prompt(null, "press enter to continue");
        if (!message.isEmpty()) {
            ArrayList<String> lines = wordWrap(message, popUp.width - 6);
            if (lines.size() > popUp.height - 3) {
                // pager
            }
            else {
                int y = 2;
                for (String s : lines) {
                    popUp.putText(3, y++, s);
                }
            }
        }
        Screen.refresh();
        scanner.nextLine();
        Screen.removeWindow(popUp);
        Screen.refresh();
    }
}
