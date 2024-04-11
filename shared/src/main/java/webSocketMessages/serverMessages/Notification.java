package webSocketMessages.serverMessages;

public class Notification extends ServerMessage{
    String message;
    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}
