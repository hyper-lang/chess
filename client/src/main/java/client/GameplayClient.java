package client;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import websocket.commands.*;
import websocket.messages.*;

import java.net.URI;

import com.google.gson.Gson;

public class GameplayClient extends Endpoint {
    public Session session;

    public GameplayClient(String url) throws Exception {
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);
    }

    public void send(UserGameCommand gameCommand) throws Exception {
        Gson gson = new Gson();
        session.getBasicRemote().sendText(gson.toJson(gameCommand));
    }

    public void onOpen(Session session, EndpointConfig endpointConfig){
        this.session = session;

        session.addMessageHandler((MessageHandler.Whole<String>) rawMessage -> {
            Gson gson = new Gson();
            ServerMessage serverMessage = gson.fromJson(rawMessage, ServerMessage.class);
            message(serverMessage, rawMessage);
        });
    }

    public void message(ServerMessage serverMessage, String rawMessage){
        System.out.println("WS RECEIVED: " + rawMessage);
        Gson gson = new Gson();
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
                NotificationServerMessage notificationServerMessage = gson.fromJson(rawMessage, NotificationServerMessage.class);
                notification(notificationServerMessage);
                break;
            default:
                System.out.println("defaulted");
                break;
        }
    }

    public void loadGame(LoadServerMessage serverMessage){
        PrintBoard.printBoard(serverMessage.getGame().getBoard(), false);
    }

    public void error(ErrorServerMessage serverMessage){
        System.out.println(serverMessage.getMessage());
    }

    public void notification(NotificationServerMessage serverMessage){
        System.out.println(serverMessage.getMessage());
    }
}
