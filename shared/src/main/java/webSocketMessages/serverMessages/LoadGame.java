package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage{
    String game;
    public LoadGame(String game) {
        super(ServerMessage.ServerMessageType.LOAD_GAME);
        this.game = game;
    }
}
