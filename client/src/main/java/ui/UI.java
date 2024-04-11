package ui;

import serverFacade.ServerFacade;

import java.util.ArrayList;
import java.util.Scanner;

public class UI {
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
    static final int[] difffff = {220, 50, 47};
    static final String ARROW = "\ue0b0";
    static final String BACK_ARROW = "\ue0b2";
    static final int cursorX = 5;
    static final int cursorY = 5;
    static ServerFacade facade = new ServerFacade(8080, "http://localhost:");

    protected static String setForeground(int[] color) {
        if (color == null || color.length != 3) return "";
        String r = Integer.toString(color[0]);
        String g = Integer.toString(color[1]);
        String b = Integer.toString(color[2]);
        return "\033[38;2;" + r + ";" + g + ";" + b + "m";
    }

    protected static String setBackground(int[] color) {
        if (color == null || color.length != 3) return "";
        String r = Integer.toString(color[0]);
        String g = Integer.toString(color[1]);
        String b = Integer.toString(color[2]);
        return "\033[48;2;" + r + ";" + g + ";" + b + "m";
    }

    protected static String padLeft(String text, int width, char c) {
        int currWidth = printLength(text);
        if (currWidth > width) return getPrintSub(0, width, text);
        return String.valueOf(c).repeat(Math.max(0, width - currWidth)) + text;
    }

    protected static String padRight(String text, int width, char c) {
        int currWidth = printLength(text);
        if (currWidth > width) return getPrintSub(0, width, text);
        return text + String.valueOf(c).repeat(Math.max(0, width - currWidth));
    }

    protected static String padCenter(String text, int width, char c) {
        int currWidth = printLength(text);
        if (currWidth > width) return getPrintSub(0, width, text);
        int padWidth = Math.max(0, width - currWidth);
        return String.valueOf(c).repeat(padWidth / 2) +
                text +
                String.valueOf(c).repeat((padWidth + 1) / 2);
    }

    protected static String setColor(int[] bg, int[] fg) {
        return setBackground(bg) + setForeground(fg);
    }


    protected enum HttpResponse {
        _200, _401, _403, _500
    }

    protected HttpResponse get() {
        return HttpResponse._401;
    }

    Scanner scanner = new Scanner(System.in);

    protected void start() {

    }

    protected static int printLength(String text) {
        boolean escape = false;
        char[] textArr = text.toCharArray();
        int cleanLen = 0;
        for (char c : textArr) {
            if (escape && c == 'm') {
                escape = false;
                continue;
            }
            if (!escape && c == '\u001b') {
                escape = true;
                continue;
            } else if (escape) continue;
            cleanLen++;
        }
        return cleanLen;
    }

    static String getPrintSub(int start, int length, String text) {
        StringBuilder cleanText = new StringBuilder();
        boolean escape = false;
        if (text == null) {
            return "";
        }
        char[] textArr = text.toCharArray();
        int cleanLen = 0;
        if (start >= textArr.length) return "";
        int startIndex = 0;
        while (cleanLen < start && startIndex < textArr.length) {
            char c = textArr[startIndex];
            startIndex++;
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
        }
        cleanLen = 0;
        for (int i = startIndex; cleanLen <= length && i < textArr.length; i++) {
            char c = textArr[i];
            if (escape && c == 'm') {
                escape = false;
                cleanText.append(c);
                continue;
            }
            if (!escape && c == '\u001b') {
                escape = true;
                cleanText.append(c);
                continue;
            } else if (escape) {
                cleanText.append(c);
                continue;
            }
            cleanLen++;
            cleanText.append(c);
        }
        return cleanText.toString();
    }

    protected boolean getInput(String[] input) {
        input = scanner.nextLine().split("\\s");
        return true;
    }

    public static ArrayList<String> wordWrap(String text, int maxWidth) {
        if (maxWidth <= 1) throw new RuntimeException("divide by zero");
        StringBuilder result = new StringBuilder();
        String[] paragraphs = text.split("(\\n)");
        ArrayList<String> lines = new ArrayList<>();
        for (String p : paragraphs) {
            String[] words = p.split("((?<=\\s))");
            StringBuilder currentLine = new StringBuilder();
            for (String word : words) {
                if (currentLine.length() + word.length() <= maxWidth) {
                    currentLine.append(word);
                } else if (currentLine.isEmpty()) {
                    while (word.length() > maxWidth) {
                        lines.add(word.substring(0, maxWidth));
                        word = word.substring(maxWidth);
                    }
                    lines.add(word);
                }
                else {
                    lines.add(currentLine.toString().trim());
                    currentLine = new StringBuilder(word);
                }
            }
            lines.add(currentLine.toString().trim());
        }
        return lines;
    }


}
