import server.Server;
import serverFacade.ServerFacade;
import ui.LoginUi;
import ui.SelectionUi;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
        ServerFacade facade = new ServerFacade(8080, "https://localhost:");
        SelectionUi selectionUi = new SelectionUi();
        LoginUi loginUi = new LoginUi();
        loginUi.start();
    }
}
