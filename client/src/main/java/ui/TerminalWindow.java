package ui;

import java.util.Arrays;

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
            putText(x, y - i, lines[i]);
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
            text = getPrintSub(0, x, currRow) + text;
        }
        text = getPrintSub(0, width, text);
        int problem = printLength(text);
        String rem = getPrintSub(problem, width - printLength(text), currRow);
        values[y] = text + rem;
    }

    protected void banner(String message) {
        banner(message, height - 2, PRIMARY);
    }


    protected void banner(String message, int y) {
        banner(message, y, PRIMARY);
    }

    protected void banner(String message, int y, int[] color) {
        String title = setColor(null, color) + BACK_ARROW + setColor(color, LIGHT_PIECE) + "   " + message + "   " + RESET_BG_COLOR + setColor(null, color) + ARROW + RESET_TEXT_COLOR;
        centerText(y, title);
    }
    public void topBar(int[] background, int[] forground, String fill) {
        values[0] = setColor(background, forground) + fill + RESET_BG_COLOR + RESET_TEXT_COLOR;
    }

    public void bar(int row, String fill) {
        values[row] = fill;
    }

    public void border(int[] color) {

    }
}
