package server;

import model.*;
import dataaccess.*;
import service.*;
import io.javalin.*;
import io.javalin.http.Context;

import java.util.Collection;
import java.util.Map;

public class Server {

    private final Javalin javalin;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(authDAO, gameDAO);
        clearService = new ClearService(userDAO, authDAO, gameDAO);

        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
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

    public void register(Context ctx){
        try {
            UserRequest req = ctx.bodyAsClass(UserRequest.class);
            AuthData auth = userService.register(new UserData(req.username, req.password, req.email));
            ctx.status(200);
            ctx.json(auth);
        } catch (DataAccessException e) {
            String msg = e.getMessage();
            if ("already taken".equals(msg)) {
                ctx.status(403);
            } else if ("bad request".equals(msg)) {
                ctx.status(400);
            } else {
                ctx.status(500);
            }
            ctx.json(Map.of("message", "Error: " + msg));
        }
    }

    public void clear(Context ctx){
      try{
            clearService.clear();
            ctx.status(200);
        } catch (Exception e){
            ctx.status(400);
            ctx.json(Map.of("message", "Error"));
        }
    }

    public void login(Context ctx){
        try{
            UserData user = ctx.bodyAsClass(UserData.class);
            AuthData auth = userService.login(user);
            ctx.status(200);
            ctx.json(auth);
        } catch (Exception e){
            ctx.status(400);
            ctx.json(Map.of("message", "Error"));
        }
    }

    public void logout(Context ctx){
        try {
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
            ctx.status(200);
        } catch (DataAccessException e) {
            String msg = e.getMessage();
            if ("unauthorized".equals(msg)) {
                ctx.status(401);
            } else {
                ctx.status(500);
            }
            ctx.json(Map.of("message", "Error: " + msg));
        }
    }

    public void listGames(Context ctx){
        try{
            String authToken = ctx.header("authorization");
            Collection<GameData> games = gameService.listGames(authToken);
            ctx.status(200);
            ctx.json(Map.of("games", games));
        } catch (DataAccessException e) {
            if ("unauthorized".equals(e.getMessage())) {
                ctx.status(401);
                ctx.json(Map.of("message", "Error: unauthorized"));
            } else {
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
        }
    }

    public void createGame(Context ctx){
        try {
            String authToken = ctx.header("authorization");
            CreateGameRequest req = ctx.bodyAsClass(CreateGameRequest.class);
            int gameID = gameService.createGame(authToken);
            ctx.status(200);
            ctx.json(Map.of("gameID", gameID));
        } catch (DataAccessException e) {
            if ("unauthorized".equals(e.getMessage())) {
                ctx.status(401);
                ctx.json(Map.of("message", "Error: unauthorized"));
            } else {
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + e.getMessage()));
            }
        }
    }

    public void joinGame(Context ctx){
        try {
            String authToken = ctx.header("authorization");
            JoinGameRequest req = ctx.bodyAsClass(JoinGameRequest.class);
            gameService.joinGame(authToken, req.gameID, req.playerColor);
            ctx.status(200);
        } catch (DataAccessException e) {
            String msg = e.getMessage();
            switch (msg) {
                case "unauthorized" -> ctx.status(401);
                case "bad request" -> ctx.status(400);
                case "game not found" -> ctx.status(404);
                case "already taken" -> ctx.status(403);
                default -> ctx.status(500);
            }
            ctx.json(Map.of("message", "Error: " + msg));
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
        public int gameID;
        public String playerColor;
    }
}
