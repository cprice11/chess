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
        TerminalWindow w = new TerminalWindow(9, 3,30,10);
        w.putText(1, 1, "Hello world");
        screen.addWindow(w);
        new ErrorUi().displayError(401, "NOT FOUND");
        Screen.refresh();

    }
}
