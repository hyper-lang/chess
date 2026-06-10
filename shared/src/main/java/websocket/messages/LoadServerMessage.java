package websocket.messages;

import chess.ChessGame;

public class LoadServerMessage extends ServerMessage {
    private ChessGame game;

    public LoadServerMessage(ChessGame game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame(){
        return game;
    }
}
