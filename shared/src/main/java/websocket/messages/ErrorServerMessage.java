package websocket.messages;

public class ErrorServerMessage extends ServerMessage {
    private String errorMessage;

    public ErrorServerMessage(String message){
        super(ServerMessageType.ERROR);
        errorMessage = message;
    }

    public String getMessage(){
        return errorMessage;
    }
}
