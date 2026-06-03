package client;

import org.junit.jupiter.api.*;
import server.Server;
import model.*;

import java.util.UUID;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + Integer.toString(port));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPositive() {
        String randomStr = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        AuthData auth = facade.register(new UserData(randomStr, randomStr, randomStr));
        assertNotNull(auth.authToken());
    }

    @Test
    public void registerNegative(){
        AuthData auth = facade.register(new UserData("bob", "bob", "bob"));
        assertNull(auth.authToken());
    }

    @Test
    public void loginPositive(){
        AuthData auth = facade.login(new UserData("bob", "bob"));
        assertNotNull(auth.authToken());
    }

    @Test
    public void loginNegative(){
        AuthData auth = facade.login(new UserData("nonexistant", "nonexistant"));
        assertNull(auth.authToken());
    }

    @Test
    public void logoutPositive(){
        facade.login(new UserData("bob", "bob"));
        assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void logoutNegative(){
        assertThrows(Exception.class, () -> facade.logout());
    }

    @Test
    public void createGamePositive(){
        AuthData auth = facade.login("bob", "bob");
        assertDoesNotThrow(() -> facade.createGame(auth, "hype_game"));
    }

    @Test
    public void createGameNegative(){
        assertThrows(Exception.class, () -> facade.createGame(null, "hype_game"));
    }

    @Test
    public void listGamesPositive(){
        AuthData auth = facade.login("bob", "bob");
        assertDoesNotThrow(() -> facade.listGames(auth));
    }

    @Test
    public void listGamesNegative(){
        assertThrows(Exception.class, () -> facade.listGames(null));
    }

    @Test
    public void joinGamePositive(){
        AuthData auth = facade.login("bob", "bob");
        int gameInt = facade.createGame(auth, "new_game_created");
        assertDoesNotThrow(() -> facade.joinGame(auth, "WHITE", gameInt));
    }

    @Test
    public void joinGameNegative(){
        AuthData auth = facade.login("bob", "bob");
        assertThrows(Exception.class, facade.joinGame(auth, "WHITE", 1000));
    }
}
