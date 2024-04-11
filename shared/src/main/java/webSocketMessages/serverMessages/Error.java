package webSocketMessages.serverMessages;

public class Error extends ServerMessage{
    int code;
    String message;
    public Error(String message, int code) {
        super(ServerMessageType.ERROR);
        this.message = message;
        this.code = code;
    }
}
