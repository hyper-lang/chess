package server;

import model.*;
import dataaccess.*;
import service.*;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;

public class Server {

    private final Javalin javalin;
    private Gson serializer;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    public Server() {
        serializer = new Gson();

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(authDAO, gameDAO);
        clearService = new ClearService(userDAO, authDAO, gameDAO);

        javalin = Javalin.create(config -> config.staticFiles.add("/server/web"));

        // Register your endpoints and exception handlers here.
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
        System.out.println(ctx.body());
        try{
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            AuthData auth = userService.register(user);
            ctx.status(200);
            ctx.json(auth);
        } catch (Exception e){
            ctx.status(400);
            ctx.json("Error");
        }
    }

    public void clear(Context ctx){
      try{
            clearService.clear();
            ctx.status(200);
        } catch (Exception e){
            ctx.status(400);
            ctx.json("Error");
        }
    }

    public void login(Context ctx){
        try{
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            AuthData auth = userService.login(user);
            ctx.status(200);
            ctx.json(auth);
        } catch (Exception e){
            ctx.status(400);
            ctx.json("Error");
        }
    }

    public void logout(Context ctx){
        try{
            String authToken = serializer.fromJson(ctx.body(), String.class);
            userService.logout(authToken);
            ctx.status(200);
        } catch (Exception e){
            ctx.status(400);
            ctx.json("Error");
        }
    }

    public void listGames(Context ctx){
        try{
            ctx.status(200);
            ctx.json(serializer.toJson(gameService.listGames()));
        } catch (Exception e){
            ctx.status(400);
            ctx.json("Error");
        }
    }

    public void createGame(Context ctx){
        try{
            String authToken = serializer.fromJson(ctx.body(), String.class);
            ctx.status(200);
            ctx.json(gameService.createGame(authToken));
        } catch (Exception e){
            ctx.status(400);
            ctx.json("Error");
        }
    }

    public void joinGame(Context ctx){
        try{
            String authToken = serializer.fromJson(ctx.body(), String.class);
            ctx.status(200);
            ctx.json(gameService.createGame(authToken));
        } catch (Exception e){
            ctx.status(400);
            ctx.json("Error");
        }
    }
}
