package client;

import jakarta.websocket.*;
import chess.*;
import ui.EscapeSequences;
import websocket.commands.*;
import websocket.messages.*;

import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@ClientEndpoint
public class GameplayClient {
    public Session session;
    private ChessGame game;
    private boolean isWhite;

    public GameplayClient(String url, boolean isWhite) throws Exception{
        this.isWhite = isWhite;
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
    }

    public void send(UserGameCommand gameCommand) throws Exception{
        try{
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String json = gson.toJson(gameCommand);
            session.getBasicRemote().sendText(json);
        }catch(Exception e){
            throw e;
        }
    }

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
    }

    @OnMessage
    public void onMessage(String rawMessage){
        try{
            Gson gson = new Gson();
            ServerMessage serverMessage = gson.fromJson(rawMessage, ServerMessage.class);
            message(serverMessage, rawMessage);
        }catch(Exception e){
        }
    }

    public void message(ServerMessage serverMessage, String rawMessage){
        try{
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            switch(serverMessage.getServerMessageType()){
                case LOAD_GAME:
                    LoadServerMessage loadServerMessage = gson.fromJson(rawMessage, LoadServerMessage.class);
                    loadGame(loadServerMessage);
                    break;
                case ERROR:
                    ErrorServerMessage errorServerMessage = gson.fromJson(rawMessage, ErrorServerMessage.class);
                    error(errorServerMessage);
                    break;
                case NOTIFICATION:
                    NotificationServerMessage notificationServerMessage = gson.fromJson(rawMessage,
                            NotificationServerMessage.class);
                    notification(notificationServerMessage);
                    break;
            }
        }catch(Exception e){
        }
    }

    public void loadGame(LoadServerMessage serverMessage){
        this.game = serverMessage.getGame();
        PrintBoard.printBoard(game.getBoard(), isWhite);
    }

    public void error(ErrorServerMessage serverMessage){
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_RED + serverMessage.getMessage() + EscapeSequences.RESET_TEXT_COLOR);
    }

    public void notification(NotificationServerMessage serverMessage){
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_YELLOW + serverMessage.getMessage() + EscapeSequences.RESET_TEXT_COLOR);
    }

    public ChessGame getGame(){
        return game;
    }

    public ChessBoard getBoard(){
        return game != null ? game.getBoard() : null;
    }
}
