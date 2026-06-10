package websocket.messages;

public class ErrorServerMessage extends ServerMessage {
    private String message;

    public ErrorServerMessage(String message){
        super(ServerMessageType.ERROR);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
