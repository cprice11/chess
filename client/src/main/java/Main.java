import ui.LoginUi;
import ui.SelectionUi;

public class Main {
    public static void main(String[] args) {
        SelectionUi selectionUi = new SelectionUi();
        LoginUi login = new LoginUi();
        login.start();
    }
}
