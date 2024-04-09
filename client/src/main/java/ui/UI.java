package ui;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class UI {
    static final int TERMINAL_WIDTH = 150;
    static final int TERMINAL_HEIGHT = 23;
    static int promptY = 5;
    protected static String promptMessage;
    protected static String promptMessage2;

    static enum color {
        BG_DARKEST,
        BG_DARKER,
        BG_DARK,
        FG_DARKER,
        FG_DARK,
        FG_LIGHT,
        FG_LIGHTER,
        BG_LIGHT,
        BG_LIGHTER,
        BG_LIGHTEST,
        PRIMARY,
        SECONDARY,
        TERNARY,
        NEGATIVE,
    }

    static final int[] BG_DARK2 = {0, 43, 54};
    static final int[] BG_DARK = {7, 54, 66};
    static final int[] FG_DARK = {101, 123, 131};
    static final int[] FG_LIGHT = {147, 161, 161};
    static final int[] BG_LIGHT = {238, 232, 213};
    static final int[] BG_LIGHT2 = {253, 246, 227};

    static final int[] LIGHT_PIECE = {255, 255, 255};
    static final int[] DARK_PIECE = {255, 255, 255};
    static final int[] LIGHT_SQUARE = {235, 247, 254};
    static final int[] DARK_SQUARE = {6, 70, 107};
    static final int[] PRIMARY = {181, 137, 0};
    static final int[] SECONDARY = {133, 153, 0};
    static final int[] TERNARY = {42, 161, 152};
    static final int[] NEGATIVE = {220, 50, 47};
    static final String ARROW = "\ue0b0";
    static final String BACK_ARROW = "\ue0b2";
    static final int cursorX = 5;
    static final int cursorY = 5;
    static String[] screen = new String[TERMINAL_HEIGHT];

    public static void prompt(String message) {
        promptMessage2 = message;
    }

    public static void prompt(String highlightMessage, String message) {
        promptMessage = highlightMessage;
        promptMessage2 = message;
    }

    public static void prompt(String highlightMessage, String message, int y) {
        promptY = y;
        promptMessage = highlightMessage;
        promptMessage2 = message;
    }

    public static String prompt() {
        StringBuilder s = new StringBuilder();
        if (promptMessage == null && promptMessage2 == null) {
            s.append(setColor(null, BG_DARK));
            s.append(ARROW);
            s.append(setColor(null, FG_LIGHT));
        }
        if (promptMessage != null) {
            s.append(setColor(SECONDARY, LIGHT_PIECE));
            s.append("  ");
            s.append(promptMessage);
            s.append("  ");
            if (promptMessage2 == null) {
                s.append(RESET_BG_COLOR);
                s.append(setColor(null, SECONDARY));
                s.append(ARROW);
                s.append(RESET_BG_COLOR);
                s.append(setForeground(FG_LIGHT));
            } else {
                s.append(setColor(BG_DARK, SECONDARY));
                s.append(ARROW);
                s.append(setColor(BG_DARK, FG_DARK));
            }
        }
        if (promptMessage2 != null) {
            s.append(setColor(BG_DARK, FG_DARK));
            s.append(" ");
            s.append(promptMessage2);
            s.append(" ");
            s.append(RESET_BG_COLOR);
            s.append(setColor(null, BG_DARK));
            s.append(ARROW);
            s.append(RESET_TEXT_COLOR);
        }
        s.append(setColor(null, BG_LIGHT));
        s.append(" ");
        return s.toString();
    }


    public static String setForeground(int[] color) {
        if (color == null || color.length != 3) return "";
        String r = Integer.toString(color[0]);
        String g = Integer.toString(color[1]);
        String b = Integer.toString(color[2]);
        return "\033[38;2;" + r + ";" + g + ";" + b + "m";
    }

    public static String setBackground(int[] color) {
        if (color == null || color.length != 3) return "";
        String r = Integer.toString(color[0]);
        String g = Integer.toString(color[1]);
        String b = Integer.toString(color[2]);
        return "\033[48;2;" + r + ";" + g + ";" + b + "m";
    }

    public static String setColor(int[] bg, int[] fg) {
        return setBackground(bg) + setForeground(fg);
    }

    public static void banner(String message) {
        banner(message, 2);
    }

    public static void banner(String message, int y) {
        String title = setColor(BG_DARK2, PRIMARY) +
                BACK_ARROW +
                setColor(PRIMARY, LIGHT_PIECE) +
                "   " +
                message +
                "   " +
                RESET_BG_COLOR +
                setColor(null, PRIMARY) +
                ARROW + RESET_TEXT_COLOR;
        centerText(TERMINAL_HEIGHT - y, title);
    }

    public enum HttpResponse {
        _200,
        _401,
        _403,
        _500
    }

    protected HttpResponse get() {
        return HttpResponse._401;
    }

    Scanner scanner = new Scanner(System.in);

    public void start() {

    }

    public static void updateScreen() {
        System.out.print(ERASE_SCREEN);
        System.out.print(moveCursorToLocation(0, 0));
        for (int i = TERMINAL_HEIGHT; i-- > promptY + 1; ) {
            String row = (screen[i] == null) ? setColor(null, FG_LIGHT) + " " : screen[i];
            System.out.println(String.format("%-" + TERMINAL_WIDTH + "s", setColor(null, FG_LIGHT) + row));
        }
        System.out.print(prompt());
    }

    public static void putBlock(int x, int y, String block) {
        var lines = block.split("\n");
        for (int i = 0; i < lines.length; i++) {
            putText(x, y - i, lines[i]);
        }
    }

    public static void putText(int x, int y, String text) {
        if ((x > TERMINAL_WIDTH || y > TERMINAL_HEIGHT) || (x < 0 || y < 0)) return;
        if (printLength(text) + x > TERMINAL_WIDTH) return;
        String currRow = (screen[y] == null) ? " " : screen[y];
        if (x > 0) {
            int preLen = Math.min(x, currRow.length());
            String pre = String.format("%-" + x + "s", getPrintSub(0, preLen, currRow));
            text = pre + text;
        }
        text = text + getPrintSub(printLength(text), TERMINAL_WIDTH, currRow);
        screen[y] = text;
    }

    public static int printLength(String text) {
        StringBuilder cleanText = new StringBuilder();
        boolean escape = false;
        char[] textArr = text.toCharArray();
        int lastPrint = 0;
        int cleanLen = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = textArr[i];
            if (escape && c == 'm') {
                escape = false;
                cleanText.append(" ");
                continue;
            }
            if (!escape && c == '\u001b') {
                escape = true;
                cleanText.append(" ");
                continue;
            } else if (escape) {
                cleanText.append(" ");
                continue;
            }
            cleanText.append(c);
            cleanLen++;
            lastPrint = (Character.isWhitespace(c)) ? lastPrint : i;
        }
        return cleanLen;
    }
    private static String getPrintSub(int start, int length, String text) {
        StringBuilder cleanText = new StringBuilder();
        boolean escape = false;
        char[] textArr = text.toCharArray();
        int lastPrint = 0;
        int cleanLen = 0;
        int i;
        if (start > textArr.length) return "";
        for (i = start; i < text.length() && cleanLen < length; i++) {
            char c = textArr[i];
            if (escape && c == 'm') {
                escape = false;
                continue;
            }
            if (!escape && c == '\u001b') {
                escape = true;
                continue;
            } else if (escape) {
                continue;
            }
            cleanLen++;
            lastPrint = (Character.isWhitespace(c)) ? lastPrint : i;
        }
        if (lastPrint == 0) return "";
        return text.substring(start, lastPrint + 1);
    }

    public static void putText(String text) {
        putText(cursorX, cursorY, text);
    }

    public static void centerText(int y, String text) {
        int len = printLength(text);
        putText(
                ((TERMINAL_WIDTH - len) / 2),
                y,
                text
        );
    }

    public static void eraseScreen() {
        Arrays.fill(screen, "");
    }

}
