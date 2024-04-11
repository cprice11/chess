package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.RESET_BG_COLOR;
import static ui.EscapeSequences.RESET_TEXT_COLOR;


public class TerminalWindow extends UI {
    public int xStart = 0;
    public int yStart = 0;
    public int width;
    public int height;
    private Screen screen;
    private String[] values;

    public TerminalWindow(int width, int height) {
        this(0, 0, width, height);
    }
    public TerminalWindow(int xStart, int yStart, int width, int height) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.width = width;
        this.height = height;
        values = new String[height];
        Arrays.fill(values, padRight("", width, ' '));
    }

    public String[] getValues() {
        return values;
    }

    public void putBlock(int x, int y, String block) {
        var lines = block.split("\n");
        for (int i = 0; i < lines.length; i++) {
            putText(x, y + i, lines[i]);
        }
    }

    public void putList(int x, int y, List<String> rows) {
        for (int i = 0; i < rows.size(); i++) {
            putText(x, y + i, rows.get(i));
        }
    }
    public void centerText(int y, String text) {
        int len = printLength(text);
        putText(((width - len) / 2), y, text);
    }

    public void putText(int x, int y, String text) {
        if ((x > width || y > height) || (x < 0 || y < 0)) return;
        String currRow = (values[y] == null) ? " " : values[y];
        if (x > 0) {
            text = padRight(getPrintSub(0, x, currRow), x, ' ') + text;
        }
        text = getPrintSub(0, width, text);
        int problem = printLength(text);
        String rem = getPrintSub(problem, width - printLength(text), currRow);
        values[y] = text + rem;
    }

    protected void banner(String message) {
        banner(message, 1, PRIMARY);
    }


    protected void banner(String message, int y) {
        banner(message, y, PRIMARY);
    }

    protected void banner(String message, int y, int[] color) {
        String title = setColor(null, color) + BACK_ARROW + setColor(color, LIGHT_PIECE) + "   " + message + "   " + RESET_BG_COLOR + setColor(null, color) + ARROW + RESET_TEXT_COLOR;
        centerText(y, title);
    }

    public void bar(int row, String fill) {
        values[row] = fill;
    }

    public void border(int[] color) {
        for (int i = 1; i < values.length - 1; i++) {
            values[i] = padRight(setForeground(color) + "█" + RESET_TEXT_COLOR, width - 1,' ') + setForeground(color) + "█" + RESET_TEXT_COLOR;
        }
        values[0] = padRight(setForeground(color) + "█", width - 1,'▀') + "█" + RESET_TEXT_COLOR;
        values[values.length - 1] = padRight(setForeground(color) + "█", width - 1,'▄') + "█" + RESET_TEXT_COLOR;
    }

    public void clear() {
        Arrays.fill(values, " ");
    }
}
