package server;

import java.util.ArrayList;
import java.util.Collection;

import chess.ChessGame;
import io.javalin.websocket.WsContext;

public class GameSession {
    private ChessGame game;
    private WsContext white;
    private WsContext black;
    private Collection<WsContext> observers;

    public GameSession(){
        observers = new ArrayList<>();
    }

    public void setGame(ChessGame game){
        this.game = game;
    }

    public void setWhite(WsContext ctx){
        white = ctx;
    }

    public void setBlack(WsContext ctx){
        black = ctx;
    }

    public void addObservers(WsContext ctx){
        observers.add(ctx);
    }

    public void removeObserver(WsContext ctx){
        observers.remove(ctx);
    }

    public ChessGame getGame(){
        return game;
    }

    public WsContext getWhite(){
        return white;
    }

    public WsContext getBlack(){
        return black;
    }

    public Collection<WsContext> getObservers(){
        return observers;
    }

    public void removeIfPresent(WsContext ctx) {
        if (white == ctx){
            white = null;
        }
        if (black == ctx){
            black = null;
        }
        observers.remove(ctx);
    }
}
