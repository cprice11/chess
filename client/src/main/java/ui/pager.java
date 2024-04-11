package ui;

import java.util.Arrays;

public class pager extends UI{
    Screen screen;
    TerminalWindow window;

    protected void chunkPages(String doc) {
        int top = 0;
        int bottom = window.height;
        int jumpDist = 3;
        boolean atBottom = false;
        String[] rows = doc.split("\\n", 300);
        String chunk = String.join("\n", Arrays.copyOfRange(rows, top, bottom));
        Screen.prompt(null, "press enter to scroll [q|x] to exit");
        window.putBlock(0, 0, chunk);
        Screen.refresh();
        String input = screen.scanner.nextLine();
        while (input == null || input.isEmpty() || "xXqQ".indexOf(input.charAt(0)) == -1 ) {
            if (atBottom) {
                top = 0;
                bottom = window.height;
                chunk = String.join("\n", Arrays.copyOfRange(rows, top, bottom));
                atBottom = false;
            } else if (bottom + jumpDist > rows.length) {
                chunk = String.join("\n", Arrays.copyOfRange(rows, rows.length - window.height, rows.length));
                atBottom = true;
            } else {
                top += jumpDist;
                bottom += jumpDist;
                chunk = String.join("\n", Arrays.copyOfRange(rows, top, bottom));
            }
            Screen.clear();
            window.banner("HELP",1, color);
            Screen.prompt(null, "press enter to scroll [q|x] to exit");
            window.putBlock(0, 0, chunk);
            Screen.refresh();
            input = scanner.nextLine();
        }
    }
}
