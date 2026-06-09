package client;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import websocket.commands.*;
import websocket.messages.ServerMessage;

import static websocket.messages.ServerMessage.ServerMessageType.ERROR;
import static websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION;

import java.net.URI;

import com.google.gson.Gson;

public class GameplayClient {
    public Session session;

    public GameplayClient(String url) throws Exception {
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String rawMessage) {
                Gson gson = new Gson();
                ServerMessage serverMessage = gson.fromJson(rawMessage, ServerMessage.class);
                System.out.println(serverMessage.getServerMessageType());
            }
        });
    }

    public void send(UserGameCommand gameCommand) throws Exception {
        Gson gson = new Gson();
        session.getBasicRemote().sendText(gson.toJson(gameCommand));
    }

    public void onOpen(Session session, EndpointConfig endpointConfig){}

    public void message(ServerMessage serverMessage){
        switch(serverMessage.getServerMessageType()){
            case LOAD_GAME -> ;
            case ERROR -> ;
            case NOTIFICATION -> ;
        }
    }
}
