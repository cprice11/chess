import server.Server;
import serverFacade.ServerFacade;
import ui.*;

public class Main {
    public static void main(String[] args) {
//        Server server = new Server();
//        server.run(8080);
//        ServerFacade facade = new ServerFacade(8080, "https://localhost:");
//        SelectionUi selectionUi = new SelectionUi();
//        LoginUi loginUi = new LoginUi();
//        loginUi.start();
        Screen screen = new Screen();
        TerminalWindow w = new TerminalWindow(0, 0, Screen.terminalWidth,Screen.terminalHeight - 1);
        w.putText(1, 1, "Hello world");
        w.bar(0, "01234567890123456789012345678901234567890");
        screen.addWindow(w);
        Screen.refresh();
        new ErrorUi().displayError(401, "NOT FOUND");
    }
}
