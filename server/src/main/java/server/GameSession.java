package server;

import java.util.ArrayList;
import java.util.Collection;

import chess.ChessGame;
import io.javalin.websocket.WsMessageContext;

public class GameSession {
    private ChessGame game;
    private WsMessageContext white;
    private WsMessageContext black;
    private Collection<WsMessageContext> observers;

    public GameSession(){
        observers = new ArrayList<>();
    }

    public void setGame(ChessGame game){
        this.game = game;
    }

    public void setWhite(WsMessageContext ctx){
        white = ctx;
    }

    public void setBlack(WsMessageContext ctx){
        black = ctx;
    }

    public void addObservers(WsMessageContext ctx){
        observers.add(ctx);
    }

    public void removeObserver(WsMessageContext ctx){
        observers.remove(ctx);
    }

    public ChessGame getGame(){
        return game;
    }

    public WsMessageContext getWhite(){
        return white;
    }

    public WsMessageContext getBlack(){
        return black;
    }

    public Collection<WsMessageContext> getObservers(){
        return observers;
    }
}
