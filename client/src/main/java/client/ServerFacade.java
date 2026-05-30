package client;

import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.Authenticator.RequestorType;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Collection;
import java.util.ArrayList;

public class ServerFacade {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private String url;

    public ServerFacade(String url){
        this.url = url;
    }

    private HttpResponse<String> sendRequest(String requestType, String fullUrl, String[] header, String body) throws Exception {
        var requestBuilder = HttpRequest.newBuilder(URI.create(fullUrl)).method(requestType, requestBodyPublisher(body));
        if(!header[0].isEmpty()){
            requestBuilder.header(header[0], header[1]);
        }
        var request = requestBuilder.build();
        return httpClient.send(request, BodyHandlers.ofString());
    }

    private static BodyPublisher requestBodyPublisher(String body) throws IOException {
        if (body != null) {
            return BodyPublishers.ofString(body);
        } else {
            return BodyPublishers.noBody();
        }
    }

    public UserData register(UserData user) throws Exception {
        return gson.fromJson(sendRequest("POST", url + "/user", null, gson.toJson(user)).body(), UserData.class);
    }

    public AuthData login(UserData user) throws Exception {
        return gson.fromJson(sendRequest("POST", url + "/session", null, gson.toJson(user)).body(), AuthData.class);
    }

    public void logout(AuthData auth) throws Exception {
        sendRequest("DELETE", url + "/session", new String[]{"authorization", auth.authToken()}, null);
    }
    
    public int createGame(AuthData auth) throws Exception {
        String response = sendRequest("POST", url + "/game", new String[]{"authorization", auth.authToken()}, null).body();
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        return json.get("gameID").getAsInt();
    }

    public Collection<GameData> listGames(AuthData auth) throws Exception {
        String response = sendRequest("GET", url + "/game", new String[]{"authorization", auth.authToken()}, null).body();
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        Collection<GameData> games;
    }

    public void joinGame(AuthData auth, String playercolor, int gameID) throws Exception {}
}
