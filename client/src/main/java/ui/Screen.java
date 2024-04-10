package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class Screen extends UI {
    static int terminalWidth = 150;
    static int terminalHeight = 23;
    private static int[] promptPrimary = PRIMARY;
    private static int[] promptSecondary = DARK_2;
    protected static String promptMessage;
    protected static String promptMessage2;

    static String[] values = new String[terminalHeight - 1]; // values[0] is top row
    // to leave room for prompt if we don't have cursor control

    private static final ArrayList<TerminalWindow> windows = new ArrayList<>();

    public Screen() {
        getDimensions();
    }

    public static void addWindow(TerminalWindow window) {
        windows.add(window);
    }

    public static void removeWindow(TerminalWindow window) {
        windows.remove(window);
    }

    public static void refresh() {
        clear();
        windows.forEach(Screen::paintValues);
        System.out.print(ERASE_SCREEN);
        System.out.print(moveCursorToLocation(0, terminalHeight));
        for (String row : values) {
            System.out.println(row);
        }
        System.out.print(prompt());
    }

    private static void paintValues(TerminalWindow window) {
        String[] windowValues = window.getValues();
        if (windowValues.length == 0) return;
        int xStart = Math.max(window.xStart, 0);
        int xEnd = Math.min(xStart + window.width, terminalWidth);
        int yStart = Math.max(window.yStart, 0);
        int yEnd = Math.min(yStart + window.height, terminalHeight);
        int effectiveWidth = xEnd - xStart;

        for (int y = 0; y < yEnd - yStart; y++) {
            String pre = "";
            String post = "";
            if (xStart > 0) pre = getPrintSub(0, xStart, values[y + yStart]);
            if (terminalWidth - xEnd > 1)
                post = getPrintSub(xEnd, terminalWidth - xEnd, values[y + yStart]);
            values[y + yStart] = pre + padRight(windowValues[y], effectiveWidth, ' ') + post;
        }
    }


    public static void prompt(String message, String message2, int[] mainColor, int[] minorColor) {
        promptMessage = message;
        promptMessage2 = message2;
        promptPrimary = mainColor;
        promptSecondary = minorColor;
    }

    public static void prompt(String message, String message2) {
        promptMessage = message;
        promptMessage2 = message2;
    }

    public static void prompt(String message) {
        promptMessage = message;
        promptMessage2 = null;
    }

    public static String prompt() {
        StringBuilder s = new StringBuilder();
        if (promptMessage == null && promptMessage2 == null) {
            s.append(setColor(null, promptSecondary));
            s.append(ARROW);
            s.append(setColor(null, LIGHT_0));
        }
        if (promptMessage != null) {
            s.append(setColor(promptPrimary, LIGHT_PIECE));
            s.append("  ");
            s.append(promptMessage);
            s.append("  ");
            if (promptMessage2 == null) {
                s.append(RESET_BG_COLOR);
                s.append(setColor(null, promptPrimary));
                s.append(ARROW);
                s.append(RESET_BG_COLOR);
                s.append(setForeground(LIGHT_0));
            } else {
                s.append(setColor(promptSecondary, promptPrimary));
                s.append(ARROW);
                s.append(setColor(promptSecondary, DARK_0));
            }
        }
        if (promptMessage2 != null) {
            s.append(setColor(promptSecondary, DARK_0));
            s.append(" ");
            s.append(promptMessage2);
            s.append(" ");
            s.append(RESET_BG_COLOR);
            s.append(setColor(null, promptSecondary));
            s.append(ARROW);
            s.append(RESET_TEXT_COLOR);
        }
        s.append(setColor(null, LIGHT_1));
        s.append(" ");
        return s.toString();
    }

    public static void clear() {
        Arrays.fill(values, padRight("", terminalWidth, ' '));
    }


    public static void getDimensions() {
        try {
            String[] command = {"/bin/sh", "-c", "tput cols && tput lines"};
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            int width = Integer.parseInt(reader.readLine());
            int height = Integer.parseInt(reader.readLine());

            terminalWidth = width;
            terminalHeight = height;
            values = new String[height];
        } catch (IOException | NumberFormatException e) {
            System.out.println("Unable to get window size. using default values");
        }
    }
}
