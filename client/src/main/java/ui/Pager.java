package ui;

import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class Pager extends UI {
    TerminalWindow window;
    private int xMargin;
    private int yMargin;
    private String title;

    public Pager(TerminalWindow window, int xMargin, int yMargin) {
        this(window, xMargin, yMargin, "Reader");
    }

    public Pager(TerminalWindow window, int xMargin, int yMargin, String title) {
        this.window = window;
        this.xMargin = xMargin;
        this.yMargin = yMargin;
        this.title = title;
    }

    protected void chunkPages(String doc) {
        int top = 0;
        int scrollDist = window.height - 2 * yMargin;
        int bottom = scrollDist;
        int jumpDist = 3;
        boolean atBottom = false;
        ArrayList<String> rows = wordWrap(doc, window.width - (2 * xMargin));
        List<String> chunk = rows.subList(0, bottom);
        clean();
        window.putList(xMargin, yMargin, chunk);
        Screen.prompt(null, "press enter to scroll [q|x] to exit");
        Screen.refresh();
        String input = scanner.nextLine();
        while (input == null || input.isEmpty() || "xXqQ".indexOf(input.charAt(0)) == -1) {
            if (atBottom) {
                top = 0;
                bottom = scrollDist;
                atBottom = false;
            } else if (bottom + jumpDist > rows.size()) {
                atBottom = true;
                bottom = rows.size() - 1;
                top = bottom - scrollDist;
            } else {
                top += jumpDist;
                bottom += jumpDist;
            }
            chunk = rows.subList(top, bottom);
            clean();
            window.putList(xMargin, yMargin, chunk);
            Screen.refresh();
            input = scanner.nextLine();
        }
    }

    private void clean() {
        window.clear();
        window.border(UI.SECONDARY);
        int titleWidth = 8 + title.length();
        int leftWidth = (window.width - titleWidth) / 2;
        int rightWidth = (window.width - titleWidth + 1) / 2;
        String left = UI.setBackground(UI.SECONDARY) +
                SET_TEXT_COLOR_BLACK +
                padLeft(BACK_ARROW, leftWidth, ' ') +
                RESET_BG_COLOR + RESET_TEXT_COLOR;
        String middle = setColor(null, UI.SECONDARY) +
                BACK_ARROW +
                setColor(UI.SECONDARY, LIGHT_PIECE) +
                "   " + title + "   " +
                RESET_BG_COLOR +
                setColor(null, UI.SECONDARY) +
                ARROW +
                RESET_TEXT_COLOR;
        String right = setBackground(UI.SECONDARY) +
                SET_TEXT_COLOR_BLACK +
                padRight(ARROW, rightWidth, ' ') +
                RESET_BG_COLOR + RESET_TEXT_COLOR;
        window.bar(0, left + middle + right);
        Screen.prompt(null, "press enter to scroll [q|x] to exit");
        Screen.refresh();
    }
}

