package server;

import model.*;
import dataaccess.*;
import service.*;
import io.javalin.*;
import io.javalin.http.Context;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import chess.ChessGame;
import io.javalin.json.JsonMapper;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;

import org.jetbrains.annotations.NotNull;

import websocket.commands.*;
import websocket.messages.*;

public class Server {

    private final Javalin javalin;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    private Map<Integer, GameSession> sessions;

    public Server() {
        try{
            userDAO = new DatabaseUserDAO();
            authDAO = new DatabaseAuthDAO();
            gameDAO = new DatabaseGameDAO();
        } catch(DataAccessException e){
            throw new RuntimeException(e);
        }

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(authDAO, gameDAO);
        clearService = new ClearService(userDAO, authDAO, gameDAO);

        sessions = new HashMap<>();

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        JsonMapper gsonMapper = new JsonMapper() {
            @NotNull
            @Override
            public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                return gson.toJson(obj);
            }

            @NotNull
            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                return gson.fromJson(json, targetType);
            }
        };

        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(gsonMapper);
        });

        javalin.post("/user", this::register);
        javalin.delete("/db", this::clear);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);

        javalin.ws("/ws", ws -> {
            ws.onConnect(ctx -> System.out.println("Connected"));
            ws.onMessage(this::onMessage);
            ws.onClose(ctx -> handleDisconnect(ctx));
            ws.onError(ctx -> handleDisconnect(ctx));
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public void register(Context ctx) {
        try {
            UserRequest req = ctx.bodyAsClass(UserRequest.class);
            AuthData auth = userService.register(new UserData(req.username, req.password, req.email));
            ctx.status(200);
            ctx.json(auth);
        } catch (DataAccessException e) {
            handleError(e, ctx);
        }
    }

    public void clear(Context ctx) {
        try {
            clearService.clear();
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(500);
            ctx.json(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    public void login(Context ctx) {
        try {
            UserData user = ctx.bodyAsClass(UserData.class);
            AuthData auth = userService.login(user);
            ctx.status(200);
            ctx.json(auth);
        } catch (DataAccessException e) {
            handleError(e, ctx);
        } catch (Exception e) {
            ctx.status(400);
            ctx.json(Map.of("message", "Error: bad request"));
        }
    }

    public void logout(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
            ctx.status(200);
        } catch (DataAccessException e) {
            handleError(e, ctx);
        }
    }

    public void listGames(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            Collection<GameData> games = gameService.listGames(authToken);
            ctx.status(200);
            ctx.json(Map.of("games", games));
        } catch (DataAccessException e) {
            handleError(e, ctx);
        }
    }

    public void createGame(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            CreateGameRequest req = ctx.bodyAsClass(CreateGameRequest.class);
            int gameID = gameService.createGame(authToken, req.gameName);
            ctx.status(200);
            ctx.json(Map.of("gameID", gameID));
        } catch (DataAccessException e) {
            handleError(e, ctx);
        }
    }

    public void joinGame(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            JoinGameRequest req = ctx.bodyAsClass(JoinGameRequest.class);
            if (req.gameID == null) {
                throw new DataAccessException("bad request");
            }
            gameService.joinGame(authToken, req.gameID, req.playerColor);
            ctx.status(200);
            ctx.json(Map.of());
        } catch (DataAccessException e) {
            handleError(e, ctx);
        } catch (Exception e) {
            ctx.status(400);
            ctx.json(Map.of("message", "Error: bad request"));
        }
    }

    private void handleError(DataAccessException e, Context ctx) {
        String msg = e.getMessage();
        switch (msg) {
            case "bad request" -> ctx.status(400);
            case "unauthorized" -> ctx.status(401);
            case "already taken" -> ctx.status(403);
            case "game not found" -> ctx.status(404);
            default -> ctx.status(500);
        }
        ctx.json(Map.of("message", "Error: " + msg));
    }

    private void onMessage(WsMessageContext ctx) throws Exception {
        Gson wsGson = new Gson();
        UserGameCommand gameCommand = wsGson.fromJson(ctx.message(), UserGameCommand.class);

        switch(gameCommand.getCommandType()){
            case CONNECT -> connect(ctx, gameCommand);
            case MAKE_MOVE -> {
                MoveUserGameCommand moveGameCommand = wsGson.fromJson(ctx.message(), MoveUserGameCommand.class);
                makeMove(ctx, moveGameCommand);
            }
            case LEAVE -> leave(ctx, gameCommand);
            case RESIGN -> resign(ctx, gameCommand);
        }
    }

    private void connect(WsMessageContext ctx, UserGameCommand gameCommand) throws Exception {
        GameSession session = sessions.computeIfAbsent(gameCommand.getGameID(), id -> new GameSession());
        GameData gameData = gameDAO.getGame(gameCommand.getGameID());
        Gson gson = new Gson();

        if(gameData == null){
            safeSend(ctx, gson.toJson(new ErrorServerMessage("Error: invalid gameID")));
            return;
        }

        if(authDAO.getAuth(gameCommand.getAuthToken()) == null){
            safeSend(ctx, gson.toJson(new ErrorServerMessage("Error: bad auth")));
            return;
        }

        String currentUsername = authDAO.getAuth(gameCommand.getAuthToken()).username();

        if(currentUsername == null){
            safeSend(ctx, gson.toJson(new ErrorServerMessage("Error: unauthorized")));
            return;
        }

        if(gameData.whiteUsername() != null && gameData.whiteUsername().equals(currentUsername)){
            session.setWhite(ctx);
        }else if(gameData.blackUsername() != null && gameData.blackUsername().equals(currentUsername)){
            session.setBlack(ctx);
        }else{
            session.addObservers(ctx);
        }

        safeSend(ctx, gson.toJson(new LoadServerMessage(gameData.game())));

        NotificationServerMessage notificationServerMessage = new NotificationServerMessage(currentUsername + " joined the game!");
        broadcastExcept(gameCommand.getGameID(), ctx, notificationServerMessage);
    }

    private void makeMove(WsMessageContext ctx, MoveUserGameCommand gameCommand) throws Exception {
        Gson gson = new Gson();
        String username = authDAO.getAuth(gameCommand.getAuthToken()).username();
        GameSession session = sessions.get(gameCommand.getGameID());
        GameData gameData = gameDAO.getGame(gameCommand.getGameID());
        //Technically an observer could craft makeMove socket commands, change the line below to prevent that
        ChessGame.TeamColor color = gameData.whiteUsername().equals(username) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        
        if(gameData.game().getTeamTurn() != color){
            safeSend(ctx, gson.toJson(new ErrorServerMessage("Error: not your turn")));
            return;
        }

        try{
            gameData.game().makeMove(gameCommand.getMove());
        } catch(Exception e){
            safeSend(ctx, gson.toJson(new ErrorServerMessage("Error: invalid move")));
            return;
        }

        GameData updated = new GameData(gameData.gameID(), gameData.whiteUsername(),
                                        gameData.blackUsername(), gameData.gameName(), gameData.game());
        session.setGame(updated.game());

        gameDAO.updateGame(gameCommand.getGameID(), updated);

        broadcastExcept(gameCommand.getGameID(), null, new LoadServerMessage(updated.game()));

        //check for check, checkmate, and stalemate still needs implementation
    }

    private void leave(WsMessageContext ctx, UserGameCommand gameCommand) throws Exception {
        GameSession gameSession = sessions.get(gameCommand.getGameID());
        String username = authDAO.getAuth(gameCommand.getAuthToken()).username();
        GameData gameData = gameDAO.getGame(gameCommand.getGameID());

        if(gameData.whiteUsername().equals(username)){
            gameSession.setWhite(null);
        } else if(gameData.blackUsername().equals(username)){
            gameSession.setBlack(null);
        } else{
            gameSession.removeObserver(ctx);
        }

        broadcastExcept(gameCommand.getGameID(), ctx, new NotificationServerMessage(username + " left the game."));
    }

    private void resign(WsMessageContext ctx, UserGameCommand gameCommand) throws Exception {
        Gson gson = new Gson();
        GameSession session = sessions.get(gameCommand.getGameID());
        String username = authDAO.getAuth(gameCommand.getAuthToken()).username();
        GameData gameData = gameDAO.getGame(gameCommand.getGameID());

        if(gameData.game().getIsOver() == true){
            safeSend(ctx, gson.toJson(new ErrorServerMessage("Error: game already over")));
            return;
        }

        session.getGame().setIsOver(true);
        gameDAO.updateGame(gameCommand.getGameID(), new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), session.getGame()));

        broadcastExcept(gameCommand.getGameID(), null, new NotificationServerMessage(username + " resigned. Game over!"));
    }

    private void broadcastExcept(int gameID, WsMessageContext except, ServerMessage message){
        GameSession session = sessions.get(gameID);
        Gson gson = new Gson();
        if(session.getWhite() != null && session.getWhite() != except){
            safeSend(session.getWhite(), gson.toJson(message));
        }
        if(session.getBlack() != null && session.getBlack() != except){
            safeSend(session.getBlack(), gson.toJson(message));
        }

        for(WsMessageContext ctx : session.getObservers()){
            if(ctx != except){
                safeSend(ctx, gson.toJson(message));
            }
        }
    }

    private void handleDisconnect(WsContext ctx) {
        for (GameSession session : sessions.values()) {
            session.removeIfPresent(ctx);
        }
    }

    private void safeSend(WsContext ctx, String msg) {
        if (ctx == null){
            return;
        }

        try {
            ctx.send(msg);
        } catch (Exception e) {
            handleDisconnect(ctx);
        }
    }

    public static class UserRequest {
        public String username;
        public String password;
        public String email;
    }

    public static class CreateGameRequest {
        public String gameName;
    }

    public static class JoinGameRequest {
        public Integer gameID;
        public String playerColor;
    }
}
