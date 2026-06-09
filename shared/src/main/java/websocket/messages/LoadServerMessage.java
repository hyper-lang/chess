package websocket.messages;

import chess.ChessGame;

public class LoadServerMessage extends ServerMessage {
    private ChessGame game;

    public LoadServerMessage(ServerMessageType type, ChessGame game){
        super(type);
        this.game = game;
    }

    public ChessGame getGame(){
        return game;
    }
}
