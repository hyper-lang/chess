package server;

import model.*;
import dataaccess.*;
import service.*;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;

public class Server {

    private final Javalin javalin;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    public Server() {
        var serializer = new Gson();

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        // GameDao gameDAO = new MemorayGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService();
        clearService = new ClearService();

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/register", this::register);
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
    }
}
