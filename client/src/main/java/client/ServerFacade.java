package client;

import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;

public class ServerFacade {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private String url;

    public ServerFacade(String url){
        this.url = url;
    }

    private HttpResponse<String> sendRequest(String requestType, String fullUrl, String[] header, String body) throws Exception {
        var requestBuilder = HttpRequest.newBuilder(URI.create(fullUrl)).method(requestType, requestBodyPublisher(body));
        if(header != null && header.length >= 2){
            requestBuilder.header(header[0], header[1]);
        }
        var request = requestBuilder.build();
        return HTTP_CLIENT.send(request, BodyHandlers.ofString());
    }

    private static BodyPublisher requestBodyPublisher(String body) throws IOException {
        if (body != null) {
            return BodyPublishers.ofString(body);
        } else {
            return BodyPublishers.noBody();
        }
    }

    public AuthData register(UserData user) throws Exception {
        var response = sendRequest("POST", url + "/user", null, gson.toJson(user));
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
        return gson.fromJson(response.body(), AuthData.class);
    }

    public AuthData login(UserData user) throws Exception {
        var response = sendRequest("POST", url + "/session", null, gson.toJson(user));

        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }

        return gson.fromJson(response.body(), AuthData.class);
    }

    public void logout(AuthData auth) throws Exception {
        var response = sendRequest("DELETE", url + "/session", new String[]{"authorization", auth.authToken()}, null);

        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }
    
    public int createGame(AuthData auth, String gameName) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("gameName", gameName);

        var response = sendRequest("POST", url + "/game", new String[]{"authorization", auth.authToken()}, body.toString());

        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        return json.get("gameID").getAsInt();
    }

    public Collection<GameData> listGames(AuthData auth) throws Exception {
        var response = sendRequest("GET", url + "/game", new String[]{"authorization", auth.authToken()}, null);

        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray gamesJson = json.getAsJsonArray("games");

        Collection<GameData> games = new ArrayList<>();
        for (JsonElement i : gamesJson) {
            games.add(gson.fromJson(i, GameData.class));
        }

        return games;
    }

    public void joinGame(AuthData auth, String playercolor, int gameID) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("playerColor", playercolor);
        body.addProperty("gameID", gameID);

        var response = sendRequest("PUT", url + "/game", new String[]{"authorization", auth.authToken()}, body.toString());

        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    public void clear() throws Exception {
        sendRequest("DELETE", url + "/db", null, null);
    }
}
