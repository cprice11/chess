package ui;

import serverFacade.ServerFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class UI {
    static int terminalWidth = 150;
    static int terminalHeight = 23;
    static int topPad = 3;
    // window padding is the way to madness.
    protected static int WINDOW_HEIGHT = terminalHeight - 5;
    protected static int TOP_OF_WINDOW = terminalHeight - 5;
    protected static int TITLE = 2;
    static int promptY = 5;
    protected static String promptMessage;
    protected static String promptMessage2;
    protected static String authToken;

    static final int[] DARK_3 = {0, 43, 54};
    static final int[] DARK_2 = {7, 54, 66};
    static final int[] DARK_1 = {88, 110, 117};
    static final int[] DARK_0 = {101, 123, 131};
    static final int[] LIGHT_0 = {147, 161, 161};
    static final int[] LIGHT_1 = {238, 232, 213};
    static final int[] LIGHT_2 = {253, 246, 227};
    static final int[] LIGHT_3 = {253, 246, 227};

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
    static String[] screen = new String[terminalHeight];

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
            s.append(setColor(null, DARK_2));
            s.append(ARROW);
            s.append(setColor(null, LIGHT_0));
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
                s.append(setForeground(LIGHT_0));
            } else {
                s.append(setColor(DARK_2, SECONDARY));
                s.append(ARROW);
                s.append(setColor(DARK_2, DARK_0));
            }
        }
        if (promptMessage2 != null) {
            s.append(setColor(DARK_2, DARK_0));
            s.append(" ");
            s.append(promptMessage2);
            s.append(" ");
            s.append(RESET_BG_COLOR);
            s.append(setColor(null, DARK_2));
            s.append(ARROW);
            s.append(RESET_TEXT_COLOR);
        }
        s.append(setColor(null, LIGHT_1));
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
        banner(message, 2, PRIMARY);
    }

    public static void banner(String message, int y) {
        banner(message, y, PRIMARY);
    }

    public static void banner(String message, int y, int[] color) {
        String title = setColor(null, color) + BACK_ARROW + setColor(color, LIGHT_PIECE) + "   " + message + "   " + RESET_BG_COLOR + setColor(null, color) + ARROW + RESET_TEXT_COLOR;
        centerText(terminalHeight - y, title);
    }


    public enum HttpResponse {
        _200, _401, _403, _500
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
        for (int i = 0; i < terminalHeight && i < terminalHeight - promptY && i < screen.length; i++) {
            String row = (screen[terminalHeight - (i + 1) ] == null) ? setColor(null, LIGHT_0) + " " : screen[terminalHeight - (i + 1)];
            System.out.printf("%-" + terminalWidth + "s%n", setColor(null, LIGHT_0) + row);
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
        if ((x > terminalWidth || y > terminalHeight) || (x < 0 || y < 0)) return;
        if (printLength(text) + x > terminalWidth) return;
        String currRow = (screen[y] == null) ? " " : screen[y];
        if (x > 0) {
            int preLen = Math.min(x, currRow.length());
            String pre = String.format("%-" + x + "s", getPrintSub(0, preLen, currRow));
            text = pre + text;
        }
        text = text + getPrintSub(printLength(text), terminalWidth, currRow);
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
        putText(((terminalWidth - len) / 2), y, text);
    }

    public static void eraseScreen() {
        Arrays.fill(screen, "");
    }


    public static void getDimeensions() {
        try {
            String[] command = {"/bin/sh", "-c", "tput cols && tput lines"};
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            int width = Integer.parseInt(reader.readLine());
            int height = Integer.parseInt(reader.readLine());

            terminalWidth = width;
            terminalHeight = height;
            screen = new String[height];
        } catch (IOException | NumberFormatException e) {
            System.out.println("Unable to get window size. using default values");
        }
    }

}
