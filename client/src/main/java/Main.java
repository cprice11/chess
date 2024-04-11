import server.Server;
import serverFacade.ServerFacade;
import ui.*;

public class Main extends UI{
    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
        ServerFacade facade = new ServerFacade(8080, "http://localhost:");
        SelectionUi selectionUi = new SelectionUi();
        LoginUi loginUi = new LoginUi();
        loginUi.start();
    }
}
