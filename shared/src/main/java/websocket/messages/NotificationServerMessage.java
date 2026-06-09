package websocket.messages;

public class NotificationServerMessage extends ServerMessage {
    private String message;

    public NotificationServerMessage(ServerMEssageType type, String message){
        super(type);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
