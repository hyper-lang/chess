package server;

import model.*;
import dataaccess.*;
import service.*;
import io.javalin.*;
import io.javalin.http.Context;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;

public class Server {

    private final Javalin javalin;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

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
